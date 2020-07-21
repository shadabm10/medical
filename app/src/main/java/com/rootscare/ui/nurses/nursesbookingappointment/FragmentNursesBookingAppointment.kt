package com.rootscare.ui.nurses.nursesbookingappointment

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.dialog.CommonDialog
import com.interfaces.DropDownDialogCallBack
import com.interfaces.OnHourlyItemClick
import com.interfaces.OnNurseSlotClick
import com.interfaces.OnPatientFamilyMemberListener
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.deletepatientfamilymemberrequest.DeletePatientFamilyMemberRequest
import com.rootscare.data.model.api.request.doctorrequest.getpatientfamilymemberrequest.GetPatientFamilyMemberRequest
import com.rootscare.data.model.api.request.nurse.hourlyslot.NurseHourlySlotRequest
import com.rootscare.data.model.api.request.nurse.nursedetailsrequest.NurseDetailsRequest
import com.rootscare.data.model.api.request.nurse.nurseslots.NurseSlotRequest
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.getpatientfamilymemberlistresponse.GetPatientFamilyListResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.getpatientfamilymemberlistresponse.ResultItem
import com.rootscare.data.model.api.response.nurses.nursebookappointment.NurseBookAppointmentResponse
import com.rootscare.data.model.api.response.nurses.nursedetails.NurseDetailsResponse
import com.rootscare.data.model.api.response.nurses.nursehourlyslot.GetNurseHourlySlotResponse
import com.rootscare.data.model.api.response.nurses.nurseviewtiming.NueseViewTimingsResponse
import com.rootscare.databinding.FragmentNursesBookingAppointmentBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.adapter.AdapterFromTimeRecyclerview
import com.rootscare.ui.bookingappointment.subfragment.editpatient.FragmentEditPatientFamilyMember
import com.rootscare.ui.bookingcart.FragmentBookingCart
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.nurses.addpatient.FragmentNurseAddPatient
import com.rootscare.ui.nurses.nursesappointmentbookingdetails.FragmentNursesAppointmentBookingDetails
import com.rootscare.ui.nurses.nursesbookingappointment.adapter.AdapterForNursesAddPatientListRecyclerview
import com.rootscare.ui.nurses.nursesbookingappointment.adapter.AdapterNurseHourlySlotRecycllerview
import com.rootscare.ui.nurses.nursesbookingappointment.adapter.AdapterNurseSlotTiimeRecyclerview
import com.rootscare.utils.ManagePermissions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentNursesBookingAppointment: BaseFragment<FragmentNursesBookingAppointmentBinding, FragmentNursesBookingAppointmentViewModel>(),
    FragmentNursesBookingAppointmentNavigator {
    private var fragmentNursesBookingAppointmentBinding: FragmentNursesBookingAppointmentBinding? = null
    private var fragmentNursesBookingAppointmentViewModel: FragmentNursesBookingAppointmentViewModel? = null
    private var nurseId:String=""
    private var familymemberid=""

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileName: String? = null
    var recordingFile: File?=null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val RECORD_AUDIO_REQUEST_CODE = 123
    private var isPlaying = false

    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    var imageFile: File? = null
    var monthstr: String=""
    var dayStr: String=""
    var nurseFirstNmae=""
    var nurseLastName=""
    var dailyrate=""
    var fromBookingTime:String=""
    var toBookingTime:String=""
    var nurseVisitPrice=""
    var nurseHourlyprice=""
    var hourlyDuration=0
    var nationalityDropdownlist:ArrayList<String?>?=null

    var prescriptionimage: RequestBody?=null
    var prescriptionimagemultipartBody: MultipartBody.Part?=null

    var symptomsRecordingFile: RequestBody?=null
    var symptomsRecordingFilemultipartBody: MultipartBody.Part?=null

    var nurseFromTime=""
    var nurseToTime=""

    var format: String? = null
    var displayHourOfTheDay=""
    var displaySecondHourOfTheDay=""
    var displayMinute=""
    var displaySecondMinute=""
    var nextHour:Int=0
    var nextminute:Int=0

    var selectedYear=0
    var selectedmonth=0
    var selectedday=0

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_booking_appointment
    override val viewModel: FragmentNursesBookingAppointmentViewModel
        get() {
            fragmentNursesBookingAppointmentViewModel =
                ViewModelProviders.of(this).get(FragmentNursesBookingAppointmentViewModel::class.java!!)
            return fragmentNursesBookingAppointmentViewModel as FragmentNursesBookingAppointmentViewModel
        }
    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(nurseid: String): FragmentNursesBookingAppointment {
            val args = Bundle()
            args.putString("nurseid",nurseid)
            val fragment = FragmentNursesBookingAppointment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesBookingAppointmentViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesBookingAppointmentBinding = viewDataBinding

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio()
        }

        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this!!.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

        if (arguments!=null && arguments?.getString("nurseid")!=null){
            nurseId = arguments?.getString("nurseid")!!
            Log.d("nurseid Id", ": " + nurseId )
        }
        nationalityDropdownlist=ArrayList<String?>()
        nationalityDropdownlist?.add("Slots")
        nationalityDropdownlist?.add("Hourly")

        apiHitForFamilyMemberList()
        apiHitForNurseViewTiming()
        apiHitForNurseDetails()
//        setUpFromTimeListingRecyclerview()
        fragmentNursesBookingAppointmentBinding?.llMain?.setOnClickListener(View.OnClickListener {
            baseActivity?.hideKeyboard()
        })
        fragmentNursesBookingAppointmentBinding?.btnAddPatient?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNurseAddPatient.newInstance(nurseId))
        })

        fragmentNursesBookingAppointmentBinding?.btnNurseBookNow?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesAppointmentBookingDetails.newInstance())
        })

        //VOICE RECORDING SECTION

        fragmentNursesBookingAppointmentBinding?.imageViewRecord?.setOnClickListener(View.OnClickListener {
            prepareforRecording()
            startRecording()
        })

        fragmentNursesBookingAppointmentBinding?.imageViewStop?.setOnClickListener(View.OnClickListener {
            prepareforStop()
            stopRecording()
        })

        fragmentNursesBookingAppointmentBinding?.imageViewPlay?.setOnClickListener(View.OnClickListener {
            if (!isPlaying && fileName != null) {
                isPlaying = true
                startPlaying()
            } else {
                fragmentNursesBookingAppointmentBinding?.chronometerTimer?.stop()
                isPlaying = false
                stopPlaying()
            }
        })





        //End of Voice recording section


        fragmentNursesBookingAppointmentBinding?.edtNurseFromTime?.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val CalendarHour = calendar.get(Calendar.HOUR_OF_DAY)
            val  CalendarMinute = calendar.get(Calendar.MINUTE)
            val  timepickerdialog = TimePickerDialog(
                context,
                OnTimeSetListener { view, hourOfDay, minute ->
                    var hourOfDay = hourOfDay
                    nextHour=hourOfDay+hourlyDuration
                    if (hourOfDay == 0) {
                        hourOfDay += 12
                        format = "AM"
                    } else if (hourOfDay == 12) {
                        format = "PM"
                    } else if (hourOfDay > 12) {
                        hourOfDay -= 12
                        format = "PM"
                    } else {
                        format = "AM"
                    }
                    if (nextHour == 0) {
                        nextHour += 12
                        format = "AM"
                    } else if (nextHour == 12) {
                        format = "PM"
                    } else if (nextHour > 12) {
                        nextHour -= 12
                        format = "PM"
                    } else {
                        format = "AM"
                    }

                    if(hourOfDay<10){
                    displayHourOfTheDay="0"+hourOfDay

                }else{
                    displayHourOfTheDay=hourOfDay.toString()
                }

                    if(minute<10){
                        displayMinute="0"+minute

                    }else{
                        displayMinute=minute.toString()
                    }

                    if(nextHour<10){
                        displaySecondHourOfTheDay="0"+nextHour

                    }else{
                        displaySecondHourOfTheDay=nextHour.toString()
                    }
                    if(minute<10){
                        displaySecondMinute="0"+minute

                    }else{
                        displaySecondMinute=minute.toString()
                    }

                    fragmentNursesBookingAppointmentBinding?.edtNurseFromTime?.setText("$displayHourOfTheDay:$displayMinute$format")
                    fragmentNursesBookingAppointmentBinding?.edtNurseToTime?.setText("$displaySecondHourOfTheDay:$displaySecondMinute$format")
                }, CalendarHour, CalendarMinute, false
            )
            timepickerdialog.show()
        })


//        fragmentNursesBookingAppointmentBinding?.edtNurseToTime?.setOnClickListener(View.OnClickListener {
//            val calendar = Calendar.getInstance()
//            val CalendarHour = calendar.get(Calendar.HOUR_OF_DAY)
//            val  CalendarMinute = calendar.get(Calendar.MINUTE)
//            val  timepickerdialog = TimePickerDialog(
//                context,
//                OnTimeSetListener { view, hourOfDay, minute ->
//                    var hourOfDay = hourOfDay
//                    if (hourOfDay == 0) {
//                        hourOfDay += 12
//                        format = "AM"
//                    } else if (hourOfDay == 12) {
//                        format = "PM"
//                    } else if (hourOfDay > 12) {
//                        hourOfDay -= 12
//                        format = "PM"
//                    } else {
//                        format = "AM"
//                    }
//
//
//                    if(hourOfDay<10){
//                        displaySecondHourOfTheDay="0"+hourOfDay
//
//                    }else{
//                        displaySecondHourOfTheDay=hourOfDay.toString()
//                    }
//                    if(minute<10){
//                        displaySecondMinute="0"+minute
//
//                    }else{
//                        displaySecondMinute=minute.toString()
//                    }
//                    fragmentNursesBookingAppointmentBinding?.edtNurseToTime?.setText("$displaySecondHourOfTheDay:$displaySecondMinute$format")
//                }, CalendarHour, CalendarMinute, false
//            )
//            timepickerdialog.show()
//        })

        fragmentNursesBookingAppointmentBinding?.txtDoctorbookingUploadPrescriptionimage?.setOnClickListener(View.OnClickListener {
            showPictureDialog()
        })

        // Set todays date and get clinic list and doctor according to todays date
        var c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        var df = SimpleDateFormat("yyyy-MM-dd");
        var formattedDate = df.format(c);
        fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.setText(formattedDate)
        if(!fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text?.toString().equals("") && fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text!=null ){
           // selectDoctorSlotApiCall(fragmentBookingBinding?.txtDoctorBookingSelectdate?.text?.toString()!!)
            apiHitForNurseViewTiming()
        }
        //End this section

        fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
//            datePicker.setMinDate(System.currentTimeMillis() - 1000)
            if(selectedYear!=0 && selectedmonth!=0 && selectedday!=0){
                c.set(selectedYear, selectedmonth, selectedday);
            }else{
                c.set(year, c.get(Calendar.MONTH), c.get(Calendar.DATE));
            }

            val dpd = DatePickerDialog(this!!.activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                if((monthOfYear+1)<10){
                    monthstr= "0" + (monthOfYear+1)
                }else{
                    monthstr=(monthOfYear+1).toString()
                }

                if(dayOfMonth<10){
                    dayStr="0"+dayOfMonth

                }else{
                    dayStr=dayOfMonth.toString()
                }

                selectedYear=year
                selectedmonth=monthOfYear
                selectedday=dayOfMonth
//                fragmentSeedStockedBinding?.txtLsfSeedstockDateofStocking?.setText("" + year + "-" + monthstr + "-" + dayStr)
                fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.setText("" + year + "-" + monthstr + "-" + dayStr)

                if(!fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text?.toString().equals("") && fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text!=null ){
                    if(fragmentNursesBookingAppointmentBinding?.txtSelectSlotOrHour?.text?.toString().equals("Slots")){
                        apiHitForNurseViewTiming()
                    }

                    //selectDoctorSlotApiCall(fragmentBookingBinding?.txtDoctorBookingSelectdate?.text?.toString()!!)
                }

            }, year, month, day)

            dpd.show()
            //Get the DatePicker instance from DatePickerDialog
            //Get the DatePicker instance from DatePickerDialog
            val dp = dpd.datePicker
            if(selectedYear!=0 && selectedmonth!=0 && selectedday!=0){
                dp.updateDate(selectedYear, selectedmonth, selectedday)
            }else{
                dp.updateDate(year,  c.get(Calendar.MONTH), c.get(Calendar.DATE))
//                c.set(year, c.get(Calendar.MONTH), c.get(Calendar.DATE))
            }
            dp.minDate=System.currentTimeMillis() - 1000
        })
//        apiHitForNurseHourlySlot()


        fragmentNursesBookingAppointmentBinding?.txtSelectSlotOrHour?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Setlect Time",nationalityDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentNursesBookingAppointmentBinding?.txtSelectSlotOrHour?.setText(text)
                    if(text.equals("Slots")){
                        apiHitForNurseViewTiming()
                    }else if(text.equals("Hourly")){
                        apiHitForNurseHourlySlot()
                    }
                }

            })
        })

        fragmentNursesBookingAppointmentBinding?.btnNurseBookNow?.setOnClickListener(View.OnClickListener {
//
            if (familymemberid.equals("")){
//                familymemberid= fragmentBookingAppointmentViewModel?.appSharedPref?.userId!!
                familymemberid= "0"
            }

            if (fragmentNursesBookingAppointmentBinding?.txtSelectSlotOrHour?.text?.toString().equals("Slots")){
                nurseVisitPrice=dailyrate
            }else if(fragmentNursesBookingAppointmentBinding?.txtSelectSlotOrHour?.text?.toString().equals("Hourly")){
                nurseVisitPrice=nurseHourlyprice
            }

            if (fragmentNursesBookingAppointmentBinding?.txtSelectSlotOrHour?.text?.toString().equals("Slots")){
                fromBookingTime=nurseFromTime
                toBookingTime=nurseToTime
            }else if(fragmentNursesBookingAppointmentBinding?.txtSelectSlotOrHour?.text?.toString().equals("Hourly")){
                fromBookingTime=fragmentNursesBookingAppointmentBinding?.edtNurseFromTime?.text?.toString()!!
                toBookingTime=fragmentNursesBookingAppointmentBinding?.edtNurseToTime?.text?.toString()!!
            }
            if(!nurseVisitPrice.equals("") && !fromBookingTime.equals("") && !toBookingTime.equals("")){
                CommonDialog.showDialog(this.activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
                        apiHitForNurseBookingAppointment()
                    }

                }, "Comfirm Appointment", "Are you sure for this nurse booking ?")
            }else{
                Toast.makeText(activity, "Please select slot/hour to continue booking.", Toast.LENGTH_SHORT).show()
            }

//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentDoctorBookingDetails.newInstance())
        })
    }

    private fun prepareforStop() {
        TransitionManager.beginDelayedTransition(fragmentNursesBookingAppointmentBinding?.linearLayoutRecorder)
        fragmentNursesBookingAppointmentBinding?.imageViewRecord?.setVisibility(View.VISIBLE)
        fragmentNursesBookingAppointmentBinding?.imageViewStop?.setVisibility(View.GONE)
        fragmentNursesBookingAppointmentBinding?.linearLayoutPlay?.setVisibility(View.VISIBLE)
    }


    private fun stopPlaying() {
        try {
            mPlayer!!.release()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        mPlayer = null
        //showing the play button
        fragmentNursesBookingAppointmentBinding?.imageViewPlay?.setImageResource(R.drawable.play)
        fragmentNursesBookingAppointmentBinding?.chronometerTimer?.stop()
    }
    private fun stopRecording() {
        try {
            mRecorder!!.stop()
            mRecorder!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mRecorder = null
        //starting the chronometer
        fragmentNursesBookingAppointmentBinding?.chronometerTimer?.stop()
        fragmentNursesBookingAppointmentBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime())
        //showing the play button
        Toast.makeText(activity, "Recording saved successfully.", Toast.LENGTH_SHORT).show()
    }

    private fun startPlaying() {
        mPlayer = MediaPlayer()
        try {
            mPlayer?.setDataSource(fileName)
            mPlayer?.prepare()
            mPlayer?.start()
        } catch (e: IOException) {
            Log.e("LOG_TAG", "prepare() failed")
        }
        //making the imageview pause button
        fragmentNursesBookingAppointmentBinding?.imageViewPlay?.setImageResource(R.drawable.pause)
        fragmentNursesBookingAppointmentBinding?.seekBar?.setProgress(lastProgress)
        mPlayer?.seekTo(lastProgress)
        fragmentNursesBookingAppointmentBinding?.seekBar?.setMax(mPlayer?.getDuration()!!)
//        seekUpdation()
        seekUpdation()

//        fragmentBookingBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime())
        fragmentNursesBookingAppointmentBinding?.chronometerTimer?.start()
        mPlayer?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            fragmentNursesBookingAppointmentBinding?.imageViewPlay?.setImageResource(R.drawable.play)
            isPlaying = false
//            mPlayer!!.seekTo(mPlayer?.getDuration()!!)
//            fragmentBookingBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime() -mPlayer?.getDuration()!!)
            lastProgress = mPlayer?.getDuration()!!
            fragmentNursesBookingAppointmentBinding?.chronometerTimer?.stop()
            val handler = Handler()
            handler.postDelayed({
                fragmentNursesBookingAppointmentBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime())
                mPlayer!!.seekTo(0) }, 100)

        })
        fragmentNursesBookingAppointmentBinding?.seekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                if (mPlayer != null && fromUser) {
                    mPlayer?.seekTo(progress)
                    fragmentNursesBookingAppointmentBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime() - mPlayer?.getCurrentPosition()!!)
                    lastProgress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    var runnable = Runnable { seekUpdation() }

    private fun seekUpdation() {
        if (mPlayer != null) {
            val mCurrentPosition = mPlayer?.getCurrentPosition()
            fragmentNursesBookingAppointmentBinding?.seekBar?.setProgress(mCurrentPosition!!)
            fragmentNursesBookingAppointmentBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime() - mPlayer?.getCurrentPosition()!!)
            lastProgress = mCurrentPosition!!
        }
        mHandler.postDelayed(runnable, 100)
    }

    private fun prepareforRecording() {
        TransitionManager.beginDelayedTransition(fragmentNursesBookingAppointmentBinding?.linearLayoutRecorder)
        fragmentNursesBookingAppointmentBinding?.imageViewRecord?.setVisibility(View.GONE)
        fragmentNursesBookingAppointmentBinding?.imageViewStop?.setVisibility(View.VISIBLE)
        fragmentNursesBookingAppointmentBinding?.linearLayoutPlay?.setVisibility(View.GONE)
    }

    private fun startRecording() {
        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        val root = Environment.getExternalStorageDirectory()
        val file  =
            File(root.absolutePath + "/VoiceRecorderSimplifiedCoding/Audios")



        fileName =
            root.absolutePath + "/VoiceRecorderSimplifiedCoding/Audios/" + (System.currentTimeMillis().toString() + ".mp4")
        Log.d("filename", fileName)
        recordingFile= File(fileName)
        Log.d("recording file",  recordingFile!!.name)
        if (!file!!.exists()) {
            file!!.mkdirs()
        }
        recordingFile!!.name
        mRecorder?.setOutputFile(fileName)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            mRecorder?.prepare()
            mRecorder?.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        lastProgress = 0
        fragmentNursesBookingAppointmentBinding?.seekBar?.setProgress(0)
        stopPlaying()
        // making the imageview a stop button
//starting the chronometer
        fragmentNursesBookingAppointmentBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime())
        fragmentNursesBookingAppointmentBinding?.chronometerTimer?.start()
    }

    //End of voice recording
    // Set up recycler view for service listing if available
    private fun setUpFromTimeListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareFromTimeRecyclerview != null)
        val recyclerView = fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareFromTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterFromTimeRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }
    // Set up recycler view for service listing if available


    // Set up recycler view for service listing if available
    private fun setUpHourlyTimeListingRecyclerview(hourlyList: ArrayList<com.rootscare.data.model.api.response.nurses.nursehourlyslot.ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareHourlyTimeRecyclerview != null)
        val recyclerView = fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareHourlyTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setNestedScrollingEnabled(false)
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNurseHourlySlotRecycllerview(hourlyList,context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView= object : OnHourlyItemClick {
            override fun onConfirm(modelItem: com.rootscare.data.model.api.response.nurses.nursehourlyslot.ResultItem) {
                fragmentNursesBookingAppointmentBinding?.edtNurseFromTime?.setText("")
                fragmentNursesBookingAppointmentBinding?.edtNurseToTime?.setText("")
                nurseHourlyprice=modelItem?.price!!
                val index: Int = modelItem.duration?.indexOf(" ")!!
                val firstString: String =  modelItem?.duration.substring(0, index)!!
                hourlyDuration=firstString.toInt()
             fragmentNursesBookingAppointmentBinding?.txtHourlyPrice?.setText("SR"+" "+modelItem?.price)
            }
        }

    }
    private fun setUpAddPatientListingRecyclerview(patientfamilymemberList: ArrayList<ResultItem?>?) {
        assert(fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareAddPatientList != null)
        val recyclerView = fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareAddPatientList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setNestedScrollingEnabled(false)
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterForNursesAddPatientListRecyclerview(patientfamilymemberList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewOnAddPatientListClick= object :
            OnPatientFamilyMemberListener {
            override fun onItemClick(modelOfGetAddPatientList: ResultItem) {
                familymemberid= modelOfGetAddPatientList?.id!!
            }



            override fun onEditButtonClick(modelOfGetAddPatientList: ResultItem) {
                var id=modelOfGetAddPatientList?.id
                var imagename=modelOfGetAddPatientList?.image
                var firstname=modelOfGetAddPatientList?.firstName
                var lastname=modelOfGetAddPatientList?.lastName
//                var email=modelOfGetAddPatientList?.email
//                var phoneno=modelOfGetAddPatientList?.phoneNumber
                var age=modelOfGetAddPatientList?.age
                var gender=modelOfGetAddPatientList?.gender

                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentEditPatientFamilyMember.newInstance(nurseId, id!!, imagename!!,
                        firstname!!, lastname!!,age!!, gender!!))



//                CommonDialog.showDialog(context!!, object :
//                    DialogClickCallback {
//                    override fun onDismiss() {
//                    }
//
//                    override fun onConfirm() {
////                        email!!, phoneno!!,
//
//                    }
//
//                }, "Edit Member", "Are you sure to edit this family member?")
            }

            override fun onDeleteButtonClick(id: String) {

                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
//            patientProfileRequest?.userId="11"
                            var deletePatientFamilyMemberRequest= DeletePatientFamilyMemberRequest()
                            deletePatientFamilyMemberRequest?.id=id
//            getPatientFamilyMemberRequest?.userId="11"
                            fragmentNursesBookingAppointmentViewModel?.apideletepatientfamilymember(deletePatientFamilyMemberRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Delete Member", "Are you sure to delete this family member?")
            }


        }

    }

    override fun successGetPatientFamilyListResponse(getPatientFamilyListResponse: GetPatientFamilyListResponse?) {
        baseActivity?.hideLoading()
        if(getPatientFamilyListResponse?.code.equals("200")){
            if(getPatientFamilyListResponse?.result!=null && getPatientFamilyListResponse?.result.size>0){
                fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareAddPatientList?.visibility=View.VISIBLE
                setUpAddPatientListingRecyclerview(getPatientFamilyListResponse?.result)
            }
        }else{
            fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareAddPatientList?.visibility=View.GONE
            fragmentNursesBookingAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentNursesBookingAppointmentBinding?.tvNoDate?.setText(getPatientFamilyListResponse?.message)
//            Toast.makeText(activity, getPatientFamilyListResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successDeletePatientFamilyListResponse(getPatientFamilyListResponse: GetPatientFamilyListResponse?) {
        baseActivity?.hideLoading()
         apiHitForFamilyMemberList()
    }

    override fun successNueseViewTimingsResponse(nueseViewTimingsResponse: NueseViewTimingsResponse?) {
        baseActivity?.hideLoading()
        if(nueseViewTimingsResponse?.code.equals("200")){

            if(nueseViewTimingsResponse?.result!=null && nueseViewTimingsResponse?.result.size>0){
                fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.VISIBLE
                fragmentNursesBookingAppointmentBinding?.tvNoDateSlottime?.visibility=View.GONE
                setNurseViewTimingListing(nueseViewTimingsResponse?.result)
            }else{
                fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.GONE
                fragmentNursesBookingAppointmentBinding?.tvNoDateSlottime?.visibility=View.VISIBLE
                fragmentNursesBookingAppointmentBinding?.tvNoDateSlottime?.setText("No timings found.")
            }

        }else{
            fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.GONE
            fragmentNursesBookingAppointmentBinding?.tvNoDateSlottime?.visibility=View.VISIBLE
            fragmentNursesBookingAppointmentBinding?.tvNoDateSlottime?.setText("No timings found.")
        }
    }

    override fun successGetNurseHourlySlotResponse(getNurseHourlySlotResponse: GetNurseHourlySlotResponse?) {
        baseActivity?.hideLoading()
        if (getNurseHourlySlotResponse?.code.equals("200")){
            if (getNurseHourlySlotResponse?.result!=null && getNurseHourlySlotResponse?.result.size>0){
                fragmentNursesBookingAppointmentBinding?.llHourlyTime?.visibility=View.VISIBLE
                fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.VISIBLE
                fragmentNursesBookingAppointmentBinding?.tvNoDateHourlytime?.visibility=View.GONE
                nurseHourlyprice=getNurseHourlySlotResponse?.result?.get(0)?.price!!
                val index: Int = getNurseHourlySlotResponse?.result?.get(0)?.duration?.indexOf(" ")!!
                val firstString: String =  getNurseHourlySlotResponse?.result?.get(0)?.duration?.substring(0, index)!!
//                Toast.makeText(activity,firstString, Toast.LENGTH_SHORT).show()
                hourlyDuration=firstString.toInt()
                setUpHourlyTimeListingRecyclerview(getNurseHourlySlotResponse?.result)
            }else{
                fragmentNursesBookingAppointmentBinding?.llHourlyTime?.visibility=View.VISIBLE
                fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.GONE
                fragmentNursesBookingAppointmentBinding?.tvNoDateHourlytime?.visibility=View.VISIBLE
                fragmentNursesBookingAppointmentBinding?.tvNoDateHourlytime?.setText("No hourly slot found.")
            }

        }else{
            fragmentNursesBookingAppointmentBinding?.llHourlyTime?.visibility=View.VISIBLE
            fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.GONE
            fragmentNursesBookingAppointmentBinding?.tvNoDateHourlytime?.visibility=View.VISIBLE
            fragmentNursesBookingAppointmentBinding?.tvNoDateHourlytime?.setText("No hourly slot found.")
        }
    }

    override fun successNurseDetailsResponse(nurseDetailsResponse: NurseDetailsResponse?) {
        baseActivity?.hideLoading()
        if(nurseDetailsResponse?.code.equals("200")){
            Toast.makeText(activity, nurseDetailsResponse?.message, Toast.LENGTH_SHORT).show()
            if(nurseDetailsResponse?.result!=null){
                if(nurseDetailsResponse?.result?.firstName!=null && !nurseDetailsResponse?.result?.firstName.equals("")){
                    nurseFirstNmae=nurseDetailsResponse?.result?.firstName
                }else{
                    nurseFirstNmae=""
                }
                if(nurseDetailsResponse?.result?.lastName!=null && !nurseDetailsResponse?.result?.lastName.equals("")){
                    nurseLastName=nurseDetailsResponse?.result?.lastName
                }else{
                    nurseLastName=""
                }
                fragmentNursesBookingAppointmentBinding?.txtNursedetailsName?.setText(nurseFirstNmae+" "+nurseLastName)
                fragmentNursesBookingAppointmentBinding?.txtNursedetailsQualification?.setText(nurseDetailsResponse?.result?.qualification)

                fragmentNursesBookingAppointmentBinding?.cardPrice?.visibility=View.VISIBLE
                fragmentNursesBookingAppointmentBinding?.rlSlotPrice?.visibility=View.VISIBLE
                dailyrate=nurseDetailsResponse?.result?.dailyRate!!
                nurseVisitPrice=dailyrate
                fragmentNursesBookingAppointmentBinding?.txtSlotPrice?.setText("SR"+" "+nurseDetailsResponse?.result?.dailyRate)
//                fragmentNursesBookingAppointmentBinding?.txt
                if(nurseDetailsResponse?.result?.image!=null && !nurseDetailsResponse?.result?.image.equals("")){

                    Glide.with(this!!.activity!!)
                        .load(this!!.activity!!.getString(R.string.api_base)+"uploads/images/" + (nurseDetailsResponse?.result?.image))
                        .into(fragmentNursesBookingAppointmentBinding?.imgNursedetailsProfile!!)
                }else{
                    Glide.with(this!!.activity!!)
                        .load(R.drawable.no_image)
                        .into(fragmentNursesBookingAppointmentBinding?.imgNursedetailsProfile!!)
                }

            }

        }else{
            Toast.makeText(activity, nurseDetailsResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successNurseBookAppointmentResponse(nurseBookAppointmentResponse: NurseBookAppointmentResponse?) {
        baseActivity?.hideLoading()
        if(nurseBookAppointmentResponse?.code.equals("200")){
            Toast.makeText(activity, nurseBookAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentBookingCart.newInstance())
        }else{
            Toast.makeText(activity, nurseBookAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    //Setup recyclerview for nurse view timing recyclerview
    private fun setNurseViewTimingListing(nurseTimingList: ArrayList<com.rootscare.data.model.api.response.nurses.nurseviewtiming.ResultItem?>?) {
        assert(fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareFromTimeRecyclerview != null)
        val recyclerView = fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareFromTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        val contactListAdapter = AdapterNurseSlotTiimeRecyclerview(nurseTimingList,context!!)
        recyclerView?.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnNurseSlotClick {
            override fun onConfirm(modelItem: com.rootscare.data.model.api.response.nurses.nurseviewtiming.ResultItem) {
                nurseFromTime=modelItem?.startTime!!
                nurseToTime=modelItem?.endTime!!

            }

        }
    }

    override fun errorGetPatientFamilyListResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    fun apiHitForFamilyMemberList(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var getPatientFamilyMemberRequest= GetPatientFamilyMemberRequest()
            getPatientFamilyMemberRequest?.userId=fragmentNursesBookingAppointmentViewModel?.appSharedPref?.userId
//            getPatientFamilyMemberRequest?.userId="11"
            fragmentNursesBookingAppointmentViewModel?.apipatientfamilymember(getPatientFamilyMemberRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if(resultCode != RESULT_CANCELED){
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data!!.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                        val path = saveImage(bitmap)
                        bitmapToFile(bitmap)
                        Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()

//                    fra?.imgRootscareProfileImage?.setImageBitmap(bitmap)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                    }

                }

            }

            else if (requestCode == CAMERA) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                //    fragmentProfileBinding?.imgRootscareProfileImage?.setImageBitmap(thumbnail)
                saveImage(thumbnail)
                bitmapToFile(thumbnail)
                Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()


            }
        }

    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + FragmentNursesBookingAppointment.IMAGE_DIRECTORY)

        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            //     File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");
            f.createNewFile()
            // imageFile=f

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())


            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    Toast.makeText(activity, "Permissions granted.", Toast.LENGTH_SHORT).show()
                    //  toast("Permissions granted.")
                } else {
                    Toast.makeText(activity, "Permissions denied.", Toast.LENGTH_SHORT).show()
                    // toast("Permissions denied.")
                }
                return
            }
        }

        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) { //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                    activity,
                    "You must give permissions to use this app. App is exiting.",
                    Toast.LENGTH_SHORT
                ).show()
                //  finishAffinity()
            }
        }
    }
    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(activity)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        imageFile = file

        fragmentNursesBookingAppointmentBinding?.txtDoctorbookingUploadPrescriptionimage?.setText(imageFile?.name)



        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //updateProfileImageApiCall(imageFile!!)
        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getPermissionToRecordAudio() { // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
// checking the build version since Context.checkSelfPermission(...) is only available
// in Marshmallow
// 2) Always check for permission (even if permission has already been granted)
// since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(
                this!!.activity!!,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this!!.activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this!!.activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) { // The permission is NOT already granted.
// Check if the user has been asked about this permission already and denied
// it. If so, we want to give more explanation about why the permission is needed.
// Fire off an async request to actually get the permission
// This will show the standard permission request dialog UI
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                RECORD_AUDIO_REQUEST_CODE
            )
        }
    }
    fun apiHitForNurseViewTiming(){
        fragmentNursesBookingAppointmentBinding?.rlSlotPrice?.visibility=View.VISIBLE
        fragmentNursesBookingAppointmentBinding?.rlHourlyPrice?.visibility=View.GONE
        fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.GONE
        fragmentNursesBookingAppointmentBinding?.tvNoDateHourlytime?.visibility=View.GONE
        fragmentNursesBookingAppointmentBinding?.llHourlyTime?.visibility=View.GONE
        fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.VISIBLE
        fragmentNursesBookingAppointmentBinding?.txtSlotPrice?.setText("SR"+" "+dailyrate)
        if(isNetworkConnected){
            baseActivity?.showLoading()
            val nurseSlotRequest= NurseSlotRequest()
//            nurseSlotRequest?.userId="11"
            nurseSlotRequest?.userId=fragmentNursesBookingAppointmentViewModel?.appSharedPref?.userId
            nurseSlotRequest?.serviceProviderId=nurseId
            nurseSlotRequest?.serviceType="nurse"
            nurseSlotRequest?.fromDate=fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text.toString()
            nurseSlotRequest?.toDate=fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text?.toString()
            fragmentNursesBookingAppointmentViewModel?.taskbasedslots(nurseSlotRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    fun apiHitForNurseHourlySlot(){
        fragmentNursesBookingAppointmentBinding?.rlSlotPrice?.visibility=View.GONE
        fragmentNursesBookingAppointmentBinding?.rlHourlyPrice?.visibility=View.VISIBLE
        fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.GONE
        fragmentNursesBookingAppointmentBinding?.tvNoDateSlottime?.visibility=View.GONE
        fragmentNursesBookingAppointmentBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.VISIBLE
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var nurseHourlySlotRequest= NurseHourlySlotRequest()
            nurseHourlySlotRequest.userId=nurseId.toInt()
            fragmentNursesBookingAppointmentViewModel?.apigethourlyrates(nurseHourlySlotRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    fun apiHitForNurseDetails(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var nurseDetailsRequest= NurseDetailsRequest()
            nurseDetailsRequest.id=nurseId.toInt()
            nurseDetailsRequest.userId=fragmentNursesBookingAppointmentViewModel?.appSharedPref?.userId?.toInt()

            fragmentNursesBookingAppointmentViewModel?.apinursedetails(nurseDetailsRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }
//Book Nurse Api Call
    private fun apiHitForNurseBookingAppointment(){


        baseActivity?.showLoading()
        val patient_id = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentNursesBookingAppointmentViewModel?.appSharedPref?.userId)
        val family_member_id = RequestBody.create(MediaType.parse("multipart/form-data"),familymemberid)
        val nurse_id = RequestBody.create(MediaType.parse("multipart/form-data"),nurseId)
        val from_date = RequestBody.create(MediaType.parse("multipart/form-data"),fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text?.toString())
        val to_date = RequestBody.create(MediaType.parse("multipart/form-data"),fragmentNursesBookingAppointmentBinding?.txtDoctorBookingSelectdate?.text?.toString())
        val from_time = RequestBody.create(MediaType.parse("multipart/form-data"),fromBookingTime)
        val to_time = RequestBody.create(MediaType.parse("multipart/form-data"),toBookingTime)
        val price = RequestBody.create(MediaType.parse("multipart/form-data"),nurseVisitPrice)
        val symptom_text = RequestBody.create(MediaType.parse("multipart/form-data"),fragmentNursesBookingAppointmentBinding?.edtSymptomText?.text?.toString())
        val appointment_type = RequestBody.create(MediaType.parse("multipart/form-data"),"offline")


        if (imageFile != null) {
            prescriptionimage = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            prescriptionimagemultipartBody = MultipartBody.Part.createFormData("upload_prescription", imageFile?.name, prescriptionimage)

        } else{
            prescriptionimage = RequestBody.create(MediaType.parse("multipart/form-data"), "")
            prescriptionimagemultipartBody = MultipartBody.Part.createFormData("upload_prescription", "", prescriptionimage)
        }

        if (recordingFile != null) {
            symptomsRecordingFile = RequestBody.create(MediaType.parse("multipart/form-data"), recordingFile)
            symptomsRecordingFilemultipartBody = MultipartBody.Part.createFormData("symptom_recording", recordingFile?.name, symptomsRecordingFile)
        } else{
            symptomsRecordingFile = RequestBody.create(MediaType.parse("multipart/form-data"), "")
            symptomsRecordingFilemultipartBody = MultipartBody.Part.createFormData("symptom_recording", "", symptomsRecordingFile)
        }

        fragmentNursesBookingAppointmentViewModel?. apibookcartnurse(patient_id,family_member_id,nurse_id,from_date,to_date,from_time,to_time,price,prescriptionimagemultipartBody,symptom_text, symptomsRecordingFilemultipartBody,appointment_type)

    }
    override fun onPause() {
        super.onPause()
        if (mRecorder != null) {
            mRecorder?.stop();
            mRecorder?.reset();
            mRecorder?.release();
            mRecorder = null;
        }
        if(mPlayer!=null){
            try {
                mPlayer?.stop()
                mPlayer?.reset()
                mPlayer!!.release()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            mPlayer = null
        }
    }
}