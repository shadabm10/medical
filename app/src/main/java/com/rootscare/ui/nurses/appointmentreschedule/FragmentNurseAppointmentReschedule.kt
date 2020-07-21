package com.rootscare.ui.nurses.appointmentreschedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.interfaces.DropDownDialogCallBack
import com.interfaces.OnHourlyItemClick
import com.interfaces.OnNurseSlotClick
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.nurse.hourlyslot.NurseHourlySlotRequest
import com.rootscare.data.model.api.request.nurse.nurseslots.NurseSlotRequest
import com.rootscare.data.model.api.request.nurse.searchbyname.NurseSearchByNameRequest
import com.rootscare.data.model.api.request.reschedule.DoctorAppointmentRescheduleRequest
import com.rootscare.data.model.api.response.nurses.nursehourlyslot.GetNurseHourlySlotResponse
import com.rootscare.data.model.api.response.nurses.nurseviewtiming.NueseViewTimingsResponse
import com.rootscare.data.model.api.response.nurses.nurseviewtiming.ResultItem
import com.rootscare.data.model.api.response.reschedule.doctorreschedule.DoctorRescheduleResponse
import com.rootscare.databinding.FragmentNurseAppointmentRescheduleBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.myappointment.FragmentMyAppointment
import com.rootscare.ui.myupcomingappointment.FragmentMyUpCommingAppointment
import com.rootscare.ui.nurses.nursesbookingappointment.adapter.AdapterNurseHourlySlotRecycllerview
import com.rootscare.ui.nurses.nursesbookingappointment.adapter.AdapterNurseSlotTiimeRecyclerview
import com.rootscare.ui.recedule.doctor.FragmentDoctorAppointmentReschedule
import com.rootscare.ui.todaysappointment.FragmentTodaysAppointment
import com.rootscare.utils.AppConstants
import java.text.SimpleDateFormat
import java.util.*

class FragmentNurseAppointmentReschedule: BaseFragment<FragmentNurseAppointmentRescheduleBinding, FragmentNurseAppointmentRescheduleViewModel>(),
    FragmentNurseAppointmentRescheduleNavigator {
    private var fragmentNurseAppointmentRescheduleBinding: FragmentNurseAppointmentRescheduleBinding? = null
    private var fragmentNurseAppointmentRescheduleViewModel: FragmentNurseAppointmentRescheduleViewModel? = null

    private var nurseId:String=""
    private var patientName:String=""
    private var bookingfromTime:String=""
    private var bookingTotime:String=""
    private var fromDate:String=""
    private var toDate:String=""
    private var appointmentId:String=""
    var displayHourOfTheDay=""
    var displaySecondHourOfTheDay=""
    var displayMinute=""
    var displaySecondMinute=""
    var nextHour:Int=0
    var nextminute:Int=0
    var hourlyDuration=0
    var monthstr: String=""
    var dayStr: String=""
    var format: String? = null

    var selectedYear=0
    var selectedmonth=0
    var selectedday=0
    var nationalityDropdownlist:ArrayList<String?>?=null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurse_appointment_reschedule
    override val viewModel: FragmentNurseAppointmentRescheduleViewModel
        get() {
            fragmentNurseAppointmentRescheduleViewModel =
                ViewModelProviders.of(this).get(FragmentNurseAppointmentRescheduleViewModel::class.java!!)
            return fragmentNurseAppointmentRescheduleViewModel as FragmentNurseAppointmentRescheduleViewModel
        }

    companion object {
        fun newInstance(id: String,nurseid: String,patientname:String,fromtime:String,totime:String,fromdate:String,todate:String): FragmentNurseAppointmentReschedule {
            val args = Bundle()
            args.putString("id",id)
            args.putString("nurseid",nurseid)
            args.putString("patientname",patientname)
            args.putString("fromtime",fromtime)
            args.putString("totime",totime)
            args.putString("fromdate",fromdate)
            args.putString("todate",todate)
            val fragment = FragmentNurseAppointmentReschedule()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNurseAppointmentRescheduleViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNurseAppointmentRescheduleBinding = viewDataBinding

        if (arguments != null && arguments?.getString("id") != null) {
            appointmentId = arguments?.getString("id")!!
            Log.d("Doctor Id", ": " + appointmentId)
        }
        if (arguments != null && arguments?.getString("nurseid") != null) {
            nurseId = arguments?.getString("nurseid")!!
            Log.d("Nurse Id", ": " + nurseId)
        }

        if (arguments != null && arguments?.getString("patientname") != null) {
            patientName = arguments?.getString("patientname")!!
            Log.d("patientname", ": " + patientName)
        }

        if (arguments != null && arguments?.getString("fromtime") != null) {
            bookingfromTime = arguments?.getString("fromtime")!!
            Log.d("totime", ": " + bookingfromTime)
        }

        if (arguments != null && arguments?.getString("totime") != null) {
            bookingTotime = arguments?.getString("totime")!!
            Log.d("bookingTotime", ": " + bookingTotime)
        }

        if (arguments != null && arguments?.getString("fromdate") != null) {
            fromDate = arguments?.getString("fromdate")!!
            Log.d("fromDate", ": " + fromDate)
        }

        if (arguments != null && arguments?.getString("todate") != null) {
            toDate = arguments?.getString("todate")!!
            Log.d("todate", ": " + toDate)
        }

        if(patientName!=null && !patientName.equals("")){
            fragmentNurseAppointmentRescheduleBinding?.edtReschedulePatientname?.setText(patientName)
        }else{
            fragmentNurseAppointmentRescheduleBinding?.edtReschedulePatientname?.setText("")
        }

        if(fromDate!=null && !fromDate.equals("")){
            fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.setText(fromDate)
        }else{
            fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.setText("")
        }

        var dt = fromDate // Start date

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = sdf.parse(dt)
        c.add(Calendar.DATE, 0) // number of days to add

        dt = sdf.format(c.time) // dt is now the new date
        selectedmonth=c[Calendar.MONTH]
        println("SELECTED Month - " + String.format("%02d", selectedmonth + 1))
        selectedday=c[Calendar.DAY_OF_MONTH]
        println("SELECTED Day - " + String.format("%02d", selectedday))
        selectedYear=c[Calendar.YEAR]
        println("SELECTED Year - $selectedYear")


        if(bookingfromTime!=null && !bookingfromTime.equals("")){
            fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText(bookingfromTime)
        }else{
            fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText("")
        }

        if(bookingTotime!=null && !bookingTotime.equals("")){
            fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText(bookingTotime)
        }else{
            fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText("")
        }

        nationalityDropdownlist=ArrayList<String?>()
        nationalityDropdownlist?.add("Slots")
        nationalityDropdownlist?.add("Hourly")

        fragmentNurseAppointmentRescheduleBinding?.txtSelectSlotOrHour?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Setlect Time",nationalityDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentNurseAppointmentRescheduleBinding?.txtSelectSlotOrHour?.setText(text)
                    if(text.equals("Slots")){
                        apiHitForNurseViewTiming()
                    }else if(text.equals("Hourly")){
                        apiHitForNurseHourlySlot()
                    }
                }

            })
        })

        apiHitForNurseViewTiming()



        fragmentNurseAppointmentRescheduleBinding?.edtNurseFromTime?.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val CalendarHour = calendar.get(Calendar.HOUR_OF_DAY)
            val  CalendarMinute = calendar.get(Calendar.MINUTE)
            val  timepickerdialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    var hourOfDay = hourOfDay
                    nextHour = hourOfDay + hourlyDuration
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

                    if (hourOfDay < 10) {
                        displayHourOfTheDay = "0" + hourOfDay

                    } else {
                        displayHourOfTheDay = hourOfDay.toString()
                    }

                    if (minute < 10) {
                        displayMinute = "0" + minute

                    } else {
                        displayMinute = minute.toString()
                    }

                    if (nextHour < 10) {
                        displaySecondHourOfTheDay = "0" + nextHour

                    } else {
                        displaySecondHourOfTheDay = nextHour.toString()
                    }
                    if (minute < 10) {
                        displaySecondMinute = "0" + minute

                    } else {
                        displaySecondMinute = minute.toString()
                    }

                    fragmentNurseAppointmentRescheduleBinding?.edtNurseFromTime?.setText("$displayHourOfTheDay:$displayMinute$format")
                    fragmentNurseAppointmentRescheduleBinding?.edtNurseToTime?.setText("$displaySecondHourOfTheDay:$displaySecondMinute$format")

                    fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText("$displayHourOfTheDay:$displayMinute$format")
                    fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText("$displaySecondHourOfTheDay:$displaySecondMinute$format")
                }, CalendarHour, CalendarMinute, false
            )
            timepickerdialog.show()
        })

//        fragmentNurseAppointmentRescheduleBinding?.edtNurseFromTime?.setOnClickListener(View.OnClickListener {
//            val calendar = Calendar.getInstance()
//            val CalendarHour = calendar.get(Calendar.HOUR_OF_DAY)
//            val  CalendarMinute = calendar.get(Calendar.MINUTE)
//            val  timepickerdialog = TimePickerDialog(
//                context,
//                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
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
//                    fragmentNurseAppointmentRescheduleBinding?.edtNurseFromTime?.setText("$hourOfDay:$minute$format")
//                    fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText("$hourOfDay:$minute$format")
//                }, CalendarHour, CalendarMinute, false
//            )
//            timepickerdialog.show()
//        })
//
//
//        fragmentNurseAppointmentRescheduleBinding?.edtNurseToTime?.setOnClickListener(View.OnClickListener {
//            val calendar = Calendar.getInstance()
//            val CalendarHour = calendar.get(Calendar.HOUR_OF_DAY)
//            val  CalendarMinute = calendar.get(Calendar.MINUTE)
//            val  timepickerdialog = TimePickerDialog(
//                context,
//                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
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
//                    fragmentNurseAppointmentRescheduleBinding?.edtNurseToTime?.setText("$hourOfDay:$minute$format")
//                    fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText("$hourOfDay:$minute$format")
//                }, CalendarHour, CalendarMinute, false
//            )
//            timepickerdialog.show()
//        })



        fragmentNurseAppointmentRescheduleBinding?.edtNurseToTime?.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable) { // you can call or do what you want with your EditText here
// yourEditText...
                if(s.toString().length>0){
                    fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText(s.toString())
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
//                if(s.toString().length>0){
//                 fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText(s.toString())
//                }
            }
        })


        fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.setOnClickListener(View.OnClickListener {
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
                selectedYear=year
                selectedmonth=monthOfYear
                selectedday=dayOfMonth
//                fragmentSeedStockedBinding?.txtLsfSeedstockDateofStocking?.setText("" + year + "-" + monthstr + "-" + dayStr)
                fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.setText("" + year + "-" + monthstr + "-" + dayStr)
                fromDate= fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text?.toString()!!
                toDate=fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text?.toString()!!
                if(fragmentNurseAppointmentRescheduleBinding?.txtSelectSlotOrHour?.text?.toString().equals("Slots")){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        val nurseSlotRequest= NurseSlotRequest()
                        nurseSlotRequest?.userId=fragmentNurseAppointmentRescheduleViewModel?.appSharedPref?.userId
                        nurseSlotRequest?.serviceProviderId=nurseId
                        nurseSlotRequest?.serviceType="nurse"
                        nurseSlotRequest?.fromDate=fromDate
                        nurseSlotRequest?.toDate=toDate
                        fragmentNurseAppointmentRescheduleViewModel?.taskbasedslots(nurseSlotRequest)
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }

            }, selectedYear, selectedmonth, selectedday)

            dpd.show()
            //Get the DatePicker instance from DatePickerDialog
            //Get the DatePicker instance from DatePickerDialog
            val dp = dpd.datePicker
//            if(selectedYear!=0 && selectedmonth!=0 && selectedday!=0){
//                dp.updateDate(selectedYear, selectedmonth, selectedday)
//            }else{
//                dp.updateDate(year,  c.get(Calendar.MONTH), c.get(Calendar.DATE))
////                c.set(year, c.get(Calendar.MONTH), c.get(Calendar.DATE))
//            }
            dp.minDate=System.currentTimeMillis() - 1000
        })

        fragmentNurseAppointmentRescheduleBinding?.btnNurseBookNow?.setOnClickListener(View.OnClickListener {
            bookingfromTime=fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.text?.toString()!!
            bookingTotime=fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.text?.toString()!!
            if(!bookingfromTime.equals("") &&!bookingTotime.equals("") ){
                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
                        AppConstants.IS_NURSE_RESCHEDULE=true
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var doctorAppointmentRescheduleRequest= DoctorAppointmentRescheduleRequest()
                            doctorAppointmentRescheduleRequest?.id=appointmentId
                            doctorAppointmentRescheduleRequest?.serviceType="nurse"
                            doctorAppointmentRescheduleRequest?.fromDate=fromDate
                            doctorAppointmentRescheduleRequest?.toDate=fromDate
                            doctorAppointmentRescheduleRequest?.fromTime=bookingfromTime
                            doctorAppointmentRescheduleRequest?.toTime=bookingTotime
                            fragmentNurseAppointmentRescheduleViewModel?.apirescheduleappointment(doctorAppointmentRescheduleRequest)
                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Reschedule Appointment", "Are you sure to reschedule this family member?")
            }else{
                Toast.makeText(activity, "Please  select time slot/hourly to reschedule booking." , Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun successNueseViewTimingsResponse(nueseViewTimingsResponse: NueseViewTimingsResponse?) {
        baseActivity?.hideLoading()
        if(nueseViewTimingsResponse?.code.equals("200")){

            if(nueseViewTimingsResponse?.result!=null && nueseViewTimingsResponse?.result.size>0){
                fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.VISIBLE
                fragmentNurseAppointmentRescheduleBinding?.tvNoDateSlottime?.visibility=View.GONE
                setNurseViewTimingListing(nueseViewTimingsResponse?.result)
            }else{
                fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.GONE
                fragmentNurseAppointmentRescheduleBinding?.tvNoDateSlottime?.visibility=View.VISIBLE
                fragmentNurseAppointmentRescheduleBinding?.tvNoDateSlottime?.setText("No timings found.")
            }

        }else{
            fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.GONE
            fragmentNurseAppointmentRescheduleBinding?.tvNoDateSlottime?.visibility=View.VISIBLE
            fragmentNurseAppointmentRescheduleBinding?.tvNoDateSlottime?.setText("No timings found.")
        }


    }

    override fun successGetNurseHourlySlotResponse(getNurseHourlySlotResponse: GetNurseHourlySlotResponse?) {
        baseActivity?.hideLoading()
        if (getNurseHourlySlotResponse?.code.equals("200")){
            if (getNurseHourlySlotResponse?.result!=null && getNurseHourlySlotResponse?.result.size>0){
                fragmentNurseAppointmentRescheduleBinding?.llHourlyTime?.visibility=View.VISIBLE
                fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.VISIBLE
                fragmentNurseAppointmentRescheduleBinding?.tvNoDateHourlytime?.visibility=View.GONE
                val index: Int = getNurseHourlySlotResponse?.result?.get(0)?.duration?.indexOf(" ")!!
                val firstString: String =  getNurseHourlySlotResponse?.result?.get(0)?.duration?.substring(0, index)!!
//                Toast.makeText(activity,firstString, Toast.LENGTH_SHORT).show()
                hourlyDuration=firstString.toInt()
                setUpHourlyTimeListingRecyclerview(getNurseHourlySlotResponse?.result)
            }else{
                fragmentNurseAppointmentRescheduleBinding?.llHourlyTime?.visibility=View.VISIBLE
                fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.GONE
                fragmentNurseAppointmentRescheduleBinding?.tvNoDateHourlytime?.visibility=View.VISIBLE
                fragmentNurseAppointmentRescheduleBinding?.tvNoDateHourlytime?.setText("No hourly slot found.")
            }

        }else{
            fragmentNurseAppointmentRescheduleBinding?.llHourlyTime?.visibility=View.VISIBLE
            fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.GONE
            fragmentNurseAppointmentRescheduleBinding?.tvNoDateHourlytime?.visibility=View.VISIBLE
            fragmentNurseAppointmentRescheduleBinding?.tvNoDateHourlytime?.setText("No hourly slot found.")
        }
    }

    override fun successDoctorRescheduleResponse(doctorRescheduleResponse: DoctorRescheduleResponse?) {
        baseActivity?.hideLoading()
        if (doctorRescheduleResponse?.code.equals("200")){
            Toast.makeText(activity, doctorRescheduleResponse?.message, Toast.LENGTH_SHORT).show()
            if(!AppConstants.recsheduleFrom.equals("")){
                if(AppConstants.recsheduleFrom.equals("Todays Appointment")){
                    (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                        FragmentTodaysAppointment.newInstance())
                    AppConstants.recsheduleFrom=""
                }else if(AppConstants.recsheduleFrom.equals("Upcoming Appointment")){
                    (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                        FragmentMyUpCommingAppointment.newInstance())
                    AppConstants.recsheduleFrom=""
                }else if(AppConstants.recsheduleFrom.equals("My Appointment")){
                    (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                        FragmentMyAppointment.newInstance())
                    AppConstants.recsheduleFrom=""
                }
            }else{
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentMyAppointment.newInstance())
                AppConstants.recsheduleFrom=""
            }


        }else{
            Toast.makeText(activity, doctorRescheduleResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }


    override fun errorGetPatientFamilyListResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    //Setup recyclerview for nurse view timing recyclerview
    private fun setNurseViewTimingListing(nurseTimingList: ArrayList<ResultItem?>?) {
        assert(fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareFromTimeRecyclerview != null)
        val recyclerView = fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareFromTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        val contactListAdapter = AdapterNurseSlotTiimeRecyclerview(nurseTimingList,context!!)
        recyclerView?.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnNurseSlotClick {
            override fun onConfirm(modelItem: com.rootscare.data.model.api.response.nurses.nurseviewtiming.ResultItem) {
//                nurseFromTime=modelItem?.startTime!!
//                nurseToTime=modelItem?.endTime!!

                fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText(modelItem?.startTime)
                fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText(modelItem?.endTime)

            }

        }
    }

    // Set up recycler view for service listing if available
    private fun setUpHourlyTimeListingRecyclerview(hourlyList: ArrayList<com.rootscare.data.model.api.response.nurses.nursehourlyslot.ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNurseAppointmentRescheduleBinding!!.recyclerViewRootscareHourlyTimeRecyclerview != null)
        val recyclerView = fragmentNurseAppointmentRescheduleBinding!!.recyclerViewRootscareHourlyTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNurseHourlySlotRecycllerview(hourlyList,context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView= object : OnHourlyItemClick {
            override fun onConfirm(modelItem: com.rootscare.data.model.api.response.nurses.nursehourlyslot.ResultItem) {
//                nurseHourlyprice=modelItem?.price!!
//                fragmentNursesBookingAppointmentBinding?.txtHourlyPrice?.setText("SR"+" "+modelItem?.price)
                fragmentNurseAppointmentRescheduleBinding?.edtNurseFromTime?.setText("")
                fragmentNurseAppointmentRescheduleBinding?.edtNurseToTime?.setText("")
                fragmentNurseAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText("")
                fragmentNurseAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText("")
                val index: Int = modelItem?.duration?.indexOf(" ")!!
                val firstString: String =  modelItem?.duration?.substring(0, index)!!
//                Toast.makeText(activity,firstString, Toast.LENGTH_SHORT).show()
                hourlyDuration=firstString.toInt()
            }
        }

    }

    fun apiHitForNurseViewTiming(){
        fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.GONE
        fragmentNurseAppointmentRescheduleBinding?.tvNoDateHourlytime?.visibility=View.GONE
        fragmentNurseAppointmentRescheduleBinding?.llHourlyTime?.visibility=View.GONE
        fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.VISIBLE
        if(isNetworkConnected){
            baseActivity?.showLoading()
            val nurseSlotRequest= NurseSlotRequest()
            nurseSlotRequest?.userId=fragmentNurseAppointmentRescheduleViewModel?.appSharedPref?.userId
            nurseSlotRequest?.serviceProviderId=nurseId
            nurseSlotRequest?.serviceType="nurse"
            nurseSlotRequest?.fromDate=fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text.toString()
            nurseSlotRequest?.toDate=fragmentNurseAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text?.toString()
            fragmentNurseAppointmentRescheduleViewModel?.taskbasedslots(nurseSlotRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    fun apiHitForNurseHourlySlot(){
        fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareFromTimeRecyclerview?.visibility=View.GONE
        fragmentNurseAppointmentRescheduleBinding?.tvNoDateSlottime?.visibility=View.GONE
        fragmentNurseAppointmentRescheduleBinding?.recyclerViewRootscareHourlyTimeRecyclerview?.visibility=View.VISIBLE
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var nurseHourlySlotRequest= NurseHourlySlotRequest()
            nurseHourlySlotRequest.userId=nurseId.toInt()
            fragmentNurseAppointmentRescheduleViewModel?.apigethourlyrates(nurseHourlySlotRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    fun apiHitForNurseAppointmentReschedule(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var doctorAppointmentRescheduleRequest= DoctorAppointmentRescheduleRequest()
            doctorAppointmentRescheduleRequest?.id=appointmentId
            doctorAppointmentRescheduleRequest?.serviceType="nurse"
            doctorAppointmentRescheduleRequest?.fromDate=fromDate
            doctorAppointmentRescheduleRequest?.toDate=fromDate
            doctorAppointmentRescheduleRequest?.fromTime=bookingfromTime
            doctorAppointmentRescheduleRequest?.toTime=bookingTotime

            fragmentNurseAppointmentRescheduleViewModel?.apirescheduleappointment(doctorAppointmentRescheduleRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

}