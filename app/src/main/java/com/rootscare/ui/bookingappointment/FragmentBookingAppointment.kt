package com.rootscare.ui.bookingappointment

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.dialog.CommonDialog
import com.interfaces.OnAddPatientListClick
import com.interfaces.OnDoctorPrivateSlotClickListner
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.doctorrequest.doctordetailsrequest.DoctorDetailsRequest
import com.rootscare.data.model.api.request.doctorrequest.doctorprivatesotrequest.DoctorPrivateSlotRequest
import com.rootscare.data.model.api.request.doctorrequest.getpatientfamilymemberrequest.GetPatientFamilyMemberRequest
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingresponse.DoctorPrivateBooingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.DoctorPrivateSlotResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.getpatientfamilymemberlistresponse.GetPatientFamilyListResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.getpatientfamilymemberlistresponse.ResultItem
import com.rootscare.data.model.api.response.doctorallapiresponse.doctordetailsresponse.DoctorDetailsResponse
import com.rootscare.databinding.FragmentBookingBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.adapter.AdapterAddPatientListRecyclerview
import com.rootscare.ui.bookingappointment.adapter.AdapterDoctorSlotRecyclerview
import com.rootscare.ui.bookingappointment.adapter.AdapterFromTimeRecyclerview
import com.rootscare.ui.bookingappointment.adapter.AdapterToTimeRecyclerView
import com.rootscare.ui.bookingappointment.subfragment.FragmentAddPatientForDoctorBooking
import com.rootscare.ui.bookingcart.FragmentBookingCart
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.utils.ManagePermissions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class FragmentBookingAppointment : BaseFragment<FragmentBookingBinding, FragmentBookingAppointmentViewModel>(), FragmentBookingAppointmentNavigator {
    private var fragmentBookingBinding: FragmentBookingBinding? = null
    private var fragmentBookingAppointmentViewModel: FragmentBookingAppointmentViewModel? = null
    var monthstr: String=""
    var dayStr: String=""
    private var doctorId:String=""
    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    var imageFile: File? = null
    var prescriptionimage: RequestBody?=null
    var prescriptionimagemultipartBody: MultipartBody.Part?=null

    var patientid=""
    var doctorFees=""


    var symptomsRecordingFile: RequestBody?=null
    var symptomsRecordingFilemultipartBody: MultipartBody.Part?=null


    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileName: String? = null
    var recordingFile: File?=null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val RECORD_AUDIO_REQUEST_CODE = 123
    private var isPlaying = false

    private var familymemberid=""
    private var clinicId=""
    private var clinicFromTime=""
    private var clinicToTime=""


    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_booking
    override val viewModel: FragmentBookingAppointmentViewModel
        get() {
            fragmentBookingAppointmentViewModel =
                ViewModelProviders.of(this).get(FragmentBookingAppointmentViewModel::class.java!!)
            return fragmentBookingAppointmentViewModel as FragmentBookingAppointmentViewModel
        }
    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(doctorid: String): FragmentBookingAppointment {
            val args = Bundle()
            args.putString("doctorid",doctorid)
            val fragment = FragmentBookingAppointment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBookingAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBookingBinding = viewDataBinding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio()
        }
        if (arguments!=null && arguments?.getString("doctorid")!=null){
            doctorId = arguments?.getString("doctorid")!!
            Log.d("Doctor Id", ": " + doctorId )
        }
//        setUpFromTimeListingRecyclerview()
//        setUpToTimeListingRecyclerview()

//        setUpDoctorSloytListingRecyclerview()

//        fragmentBookingBinding?.btnAppointmentBooking?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentPatientbookPayNow.newInstance())
//        })
        if(isNetworkConnected){
            baseActivity?.showLoading()
//            patientProfileRequest?.userId="11"
            var getPatientFamilyMemberRequest= GetPatientFamilyMemberRequest()
            getPatientFamilyMemberRequest?.userId=fragmentBookingAppointmentViewModel?.appSharedPref?.userId
//            getPatientFamilyMemberRequest?.userId="11"
            fragmentBookingAppointmentViewModel?.apipatientfamilymember(getPatientFamilyMemberRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        //All Doctor Details Api Call
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var doctorDetailsRequest= DoctorDetailsRequest()
            doctorDetailsRequest?.id=doctorId
            fragmentBookingAppointmentViewModel?.apidoctordetails(doctorDetailsRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
        fragmentBookingBinding?.btnAddPatient?.setOnClickListener(View.OnClickListener {
//            CommonDialog.showDialogForAddPatient(this!!.activity!!,
//                "Add Patient",object : DialogClickCallback {
//                    override fun onConfirm() {
//                        // Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()
//
//                    }
//
//                    override fun onDismiss() {
//                    }
//
//                })
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentAddPatientForDoctorBooking.newInstance(doctorId))

        })

        fragmentBookingBinding?.btnAppointmentBooking?.setOnClickListener(View.OnClickListener {
//            CommonDialog.showDialogForAddPatient(this!!.activity!!,
//                "Doctor Booking",object : DialogClickCallback {
//                    override fun onConfirm() {
//                        // Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()
//
//
//                    }
//
//                    override fun onDismiss() {
//                    }
//
//                })
            if (familymemberid.equals("")){
//                familymemberid= fragmentBookingAppointmentViewModel?.appSharedPref?.userId!!
                familymemberid= "0"
            }
            CommonDialog.showDialog(this.activity!!, object :
                DialogClickCallback {
                override fun onDismiss() {
                }

                override fun onConfirm() {

                    if(!clinicId.equals("") && !clinicFromTime.equals("") && !clinicToTime.equals("")){
                        doctorPrivateBookingApiCall()
                    }else{
                        Toast.makeText(activity, "Please select slot", Toast.LENGTH_SHORT).show()
                    }

                }

            }, "Comfirm Appointment", "Are you sure for this doctor booking ?")
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentDoctorBookingDetails.newInstance())
        })


        // Set todays date and get clinic list and doctor according to todays date
        var c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        var df = SimpleDateFormat("yyyy-MM-dd");
        var formattedDate = df.format(c);
        fragmentBookingBinding?.txtDoctorBookingSelectdate?.setText(formattedDate)
        if(!fragmentBookingBinding?.txtDoctorBookingSelectdate?.text?.toString().equals("") && fragmentBookingBinding?.txtDoctorBookingSelectdate?.text!=null ){
            selectDoctorSlotApiCall(fragmentBookingBinding?.txtDoctorBookingSelectdate?.text?.toString()!!)
        }
        //End this section

        fragmentBookingBinding?.txtDoctorBookingSelectdate?.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
//            datePicker.setMinDate(System.currentTimeMillis() - 1000)


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
//                fragmentSeedStockedBinding?.txtLsfSeedstockDateofStocking?.setText("" + year + "-" + monthstr + "-" + dayStr)
                fragmentBookingBinding?.txtDoctorBookingSelectdate?.setText("" + year + "-" + monthstr + "-" + dayStr)

                if(!fragmentBookingBinding?.txtDoctorBookingSelectdate?.text?.toString().equals("") && fragmentBookingBinding?.txtDoctorBookingSelectdate?.text!=null ){
                    selectDoctorSlotApiCall(fragmentBookingBinding?.txtDoctorBookingSelectdate?.text?.toString()!!)
                }

            }, year, month, day)

            dpd.show()
            //Get the DatePicker instance from DatePickerDialog
            //Get the DatePicker instance from DatePickerDialog
            val dp = dpd.datePicker
            dp.minDate=System.currentTimeMillis() - 1000
        })

        fragmentBookingBinding?.txtDoctorbookingUploadPrescriptionimage?.setOnClickListener(View.OnClickListener {
            showPictureDialog()
        })

        //VOICE RECORDING SECTION

        fragmentBookingBinding?.imageViewRecord?.setOnClickListener(View.OnClickListener {
            prepareforRecording()
            startRecording()
        })

        fragmentBookingBinding?.imageViewStop?.setOnClickListener(View.OnClickListener {
            prepareforStop()
            stopRecording()
        })

        fragmentBookingBinding?.imageViewPlay?.setOnClickListener(View.OnClickListener {
            if (!isPlaying && fileName != null) {
                isPlaying = true
                startPlaying()
            } else {
                isPlaying = false
                stopPlaying()
            }
        })

    }

    private fun prepareforStop() {
        TransitionManager.beginDelayedTransition(fragmentBookingBinding?.linearLayoutRecorder)
        fragmentBookingBinding?.imageViewRecord?.setVisibility(View.VISIBLE)
        fragmentBookingBinding?.imageViewStop?.setVisibility(View.GONE)
        fragmentBookingBinding?.linearLayoutPlay?.setVisibility(View.VISIBLE)
    }


    private fun stopPlaying() {
        try {
            mPlayer!!.release()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        mPlayer = null
        //showing the play button
        fragmentBookingBinding?.imageViewPlay?.setImageResource(R.drawable.play)
        fragmentBookingBinding?.chronometerTimer?.stop()
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
        fragmentBookingBinding?.chronometerTimer?.stop()
        fragmentBookingBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime())
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
        fragmentBookingBinding?.imageViewPlay?.setImageResource(R.drawable.pause)
        fragmentBookingBinding?.seekBar?.setProgress(lastProgress)
        mPlayer?.seekTo(lastProgress)
        fragmentBookingBinding?.seekBar?.setMax(mPlayer?.getDuration()!!)
        seekUpdation()
        fragmentBookingBinding?.chronometerTimer?.start()
        mPlayer?.setOnCompletionListener(OnCompletionListener {
            fragmentBookingBinding?.imageViewPlay?.setImageResource(R.drawable.play)
            isPlaying = false
            fragmentBookingBinding?.chronometerTimer?.stop()
        })
        fragmentBookingBinding?.seekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                if (mPlayer != null && fromUser) {
                    mPlayer?.seekTo(progress)
                    fragmentBookingBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime() - mPlayer?.getCurrentPosition()!!)
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
            fragmentBookingBinding?.seekBar?.setProgress(mCurrentPosition!!)
            lastProgress = mCurrentPosition!!
        }
        mHandler.postDelayed(runnable, 100)
    }

    private fun prepareforRecording() {
        TransitionManager.beginDelayedTransition(fragmentBookingBinding?.linearLayoutRecorder)
        fragmentBookingBinding?.imageViewRecord?.setVisibility(View.GONE)
        fragmentBookingBinding?.imageViewStop?.setVisibility(View.VISIBLE)
        fragmentBookingBinding?.linearLayoutPlay?.setVisibility(View.GONE)
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
        recordingFile=File(fileName)
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
        fragmentBookingBinding?.seekBar?.setProgress(0)
        stopPlaying()
        // making the imageview a stop button
//starting the chronometer
        fragmentBookingBinding?.chronometerTimer?.setBase(SystemClock.elapsedRealtime())
        fragmentBookingBinding?.chronometerTimer?.start()
    }

    // Set up recycler view for service listing if available
    private fun setUpAddPatientListingRecyclerview(patientfamilymemberList: ArrayList<ResultItem?>?) {
        assert(fragmentBookingBinding!!.recyclerViewRootscareAddPatientList != null)
        val recyclerView = fragmentBookingBinding!!.recyclerViewRootscareAddPatientList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterAddPatientListRecyclerview(patientfamilymemberList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewOnAddPatientListClick= object : OnAddPatientListClick {
            override fun onItemClick(modelOfGetAddPatientList: ResultItem?) {
                familymemberid= modelOfGetAddPatientList?.id!!
            }


        }

    }
    // Set up recycler view for service listing if available
    private fun setUpFromTimeListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentBookingBinding!!.recyclerViewRootscareFromTimeRecyclerview != null)
        val recyclerView = fragmentBookingBinding!!.recyclerViewRootscareFromTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterFromTimeRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }
    // Set up recycler view for service listing if available
    private fun setUpToTimeListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentBookingBinding!!.recyclerViewRootscareToTimeRecyclerview != null)
        val recyclerView = fragmentBookingBinding!!.recyclerViewRootscareToTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterToTimeRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter

    }


    // Set up recycler view for service listing if available
    private fun setUpDoctorSloytListingRecyclerview(doctorprivateList: ArrayList<com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentBookingBinding!!.recyclerViewDoctorslot != null)
        val recyclerView = fragmentBookingBinding!!.recyclerViewDoctorslot
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterDoctorSlotRecyclerview(doctorprivateList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClick= object : OnDoctorPrivateSlotClickListner {
            override fun onItemClick(modelData: com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.ResultItem?) {
                clinicId= modelData?.clinicId!!
                clinicFromTime= modelData?.timeFrom!!
                clinicToTime= modelData?.timeTo!!
            }


        }

    }

    override fun successGetPatientFamilyListResponse(getPatientFamilyListResponse: GetPatientFamilyListResponse?) {
        baseActivity?.hideLoading()
        if(getPatientFamilyListResponse?.code.equals("200")){
            if(getPatientFamilyListResponse?.result!=null && getPatientFamilyListResponse?.result.size>0){
                fragmentBookingBinding?.recyclerViewRootscareAddPatientList?.visibility=View.VISIBLE
                setUpAddPatientListingRecyclerview(getPatientFamilyListResponse?.result)
            }
        }else{
            fragmentBookingBinding?.recyclerViewRootscareAddPatientList?.visibility=View.GONE
            fragmentBookingBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentBookingBinding?.tvNoDate?.setText(getPatientFamilyListResponse?.message)
//            Toast.makeText(activity, getPatientFamilyListResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun successDoctorPrivateSlotResponse(doctorPrivateSlotResponse: DoctorPrivateSlotResponse?) {
        baseActivity?.hideLoading()
        if(doctorPrivateSlotResponse?.code.equals("200")){
            Toast.makeText(activity, doctorPrivateSlotResponse?.message, Toast.LENGTH_SHORT).show()
            if (doctorPrivateSlotResponse?.result!=null && doctorPrivateSlotResponse?.result?.size>0){

                fragmentBookingBinding?.btnAppointmentBooking?.visibility=View.VISIBLE
                fragmentBookingBinding?.recyclerViewDoctorslot?.visibility=View.VISIBLE
                fragmentBookingBinding?.tvSelectDoctorSlotNoDate?.visibility=View.GONE
                clinicId= doctorPrivateSlotResponse?.result?.get(0)?.clinicId!!
                clinicFromTime= doctorPrivateSlotResponse?.result?.get(0)?.timeFrom!!
                clinicToTime= doctorPrivateSlotResponse?.result?.get(0)?.timeTo!!
                setUpDoctorSloytListingRecyclerview(doctorPrivateSlotResponse?.result)
            }else{
                fragmentBookingBinding?.btnAppointmentBooking?.visibility=View.GONE
                fragmentBookingBinding?.recyclerViewDoctorslot?.visibility=View.GONE
                fragmentBookingBinding?.tvSelectDoctorSlotNoDate?.visibility=View.VISIBLE
                fragmentBookingBinding?.tvSelectDoctorSlotNoDate?.setText("No Doctor Slot Found")
            }
        }else{
            fragmentBookingBinding?.btnAppointmentBooking?.visibility=View.GONE
            fragmentBookingBinding?.recyclerViewDoctorslot?.visibility=View.GONE
            fragmentBookingBinding?.tvSelectDoctorSlotNoDate?.visibility=View.VISIBLE
            fragmentBookingBinding?.tvSelectDoctorSlotNoDate?.setText("No Doctor Slot Found")
            Toast.makeText(activity, doctorPrivateSlotResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun successDoctorDetailsResponse(doctorDetailsResponse: DoctorDetailsResponse?) {
        baseActivity?.hideLoading()
        if(doctorDetailsResponse?.code.equals("200")){
            Toast.makeText(activity, doctorDetailsResponse?.message, Toast.LENGTH_SHORT).show()
            fragmentBookingBinding?.txtDoctorbookingDoctorname?.setText(doctorDetailsResponse?.result?.firstName+" "+doctorDetailsResponse?.result?.lastName)
            if(!doctorDetailsResponse?.result?.image.equals("") && doctorDetailsResponse?.result?.image!=null){
                Glide.with(this!!.activity!!)
                    .load(activity?.getString(R.string.api_base)+"uploads/images/" + (doctorDetailsResponse?.result?.image))
                    .into(fragmentBookingBinding?.imgDoctorbookingDoctorprofileimage!!)
            }
            if(!doctorDetailsResponse?.result?.qualification.equals("") || doctorDetailsResponse?.result?.qualification!=null){
                fragmentBookingBinding?.txtDoctorbookingdoctorbookingDoctorqualification?.setText(doctorDetailsResponse?.result?.qualification)
            }

            if(!doctorDetailsResponse?.result?.fees.equals("") || doctorDetailsResponse?.result?.fees!=null){
                doctorFees= doctorDetailsResponse?.result?.fees!!
                fragmentBookingBinding?.txtDoctorbookingDoctorfees?.setText("SAR "+" "+doctorDetailsResponse?.result?.fees)
            }
        }else{
            Toast.makeText(activity, doctorDetailsResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successDoctorPrivateBooingResponse(doctorPrivateBooingResponse: DoctorPrivateBooingResponse?) {
        baseActivity?.hideLoading()
        if(doctorPrivateBooingResponse?.code.equals("200")){
            Toast.makeText(activity, doctorPrivateBooingResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentBookingCart.newInstance())


        }else{
            Toast.makeText(activity, doctorPrivateBooingResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorGetPatientFamilyListResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectDoctorSlotApiCall(selectDate:String){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var doctorPrivateSlotRequest= DoctorPrivateSlotRequest()
            doctorPrivateSlotRequest?.date=selectDate
            doctorPrivateSlotRequest?.doctorId=doctorId

            fragmentBookingAppointmentViewModel?.apidoctorprivateslot(doctorPrivateSlotRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }



    //IMAGE SELECTION AND GET IMAGE PATH

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

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
        //    fragmentProfileBinding?.imgRootscareProfileImage?.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            bitmapToFile(thumbnail)
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + FragmentBookingAppointment.IMAGE_DIRECTORY)

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

        fragmentBookingBinding?.txtDoctorbookingUploadPrescriptionimage?.setText(imageFile?.name)



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
    private fun doctorPrivateBookingApiCall(){
        baseActivity?.showLoading()
        val patient_id = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentBookingAppointmentViewModel?.appSharedPref?.userId)
        val family_member_id = RequestBody.create(MediaType.parse("multipart/form-data"),familymemberid)
        val doctor_id = RequestBody.create(MediaType.parse("multipart/form-data"),doctorId)
        val private_clinic_id = RequestBody.create(MediaType.parse("multipart/form-data"),clinicId)
        val appointment_date = RequestBody.create(MediaType.parse("multipart/form-data"),fragmentBookingBinding?.txtDoctorBookingSelectdate?.text?.toString())
        val from_time = RequestBody.create(MediaType.parse("multipart/form-data"),clinicFromTime)
        val to_time = RequestBody.create(MediaType.parse("multipart/form-data"),clinicToTime)
        val price = RequestBody.create(MediaType.parse("multipart/form-data"),doctorFees)
        val symptom_text = RequestBody.create(MediaType.parse("multipart/form-data"),fragmentBookingBinding?.edtSymptomText?.text?.toString())
        val appointment_type = RequestBody.create(MediaType.parse("multipart/form-data"),fragmentBookingAppointmentViewModel?.appSharedPref?.appointmentType)


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

        fragmentBookingAppointmentViewModel?. apibookcartprivatedoctor(patient_id,family_member_id,doctor_id,private_clinic_id,appointment_date,from_time,to_time,price,prescriptionimagemultipartBody,symptom_text, symptomsRecordingFilemultipartBody,appointment_type)

    }

}