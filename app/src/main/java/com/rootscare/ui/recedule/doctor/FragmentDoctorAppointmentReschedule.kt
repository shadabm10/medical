package com.rootscare.ui.recedule.doctor

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.interfaces.OnDoctorPrivateSlotClickListner
import com.interfaces.OnDoctorSlotClickListner
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.doctorrequest.doctorprivatesotrequest.DoctorPrivateSlotRequest
import com.rootscare.data.model.api.request.reschedule.DoctorAppointmentRescheduleRequest
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.DoctorPrivateSlotResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.ResultItem
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.SlotItem
import com.rootscare.data.model.api.response.reschedule.doctorreschedule.DoctorRescheduleResponse
import com.rootscare.databinding.FragmentDoctorAppointmentRescheduleBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.adapter.AdapterDoctorSlotDivision
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.myupcomingappointment.FragmentMyUpCommingAppointment
import com.rootscare.ui.recedule.doctor.adapter.AdapterDoctorPrivateSlot
import java.text.SimpleDateFormat
import java.util.*

class FragmentDoctorAppointmentReschedule : BaseFragment<FragmentDoctorAppointmentRescheduleBinding, FragmentDoctorAppointmentRescheduleViewModel>(),
    FragmentDoctorAppointmentRescheduleNavigator {
    private var fragmentDoctorAppointmentRescheduleBinding: FragmentDoctorAppointmentRescheduleBinding? = null
    private var fragmentDoctorAppointmentRescheduleViewModel: FragmentDoctorAppointmentRescheduleViewModel? = null
    var monthstr: String=""
    var dayStr: String=""
    private var doctorId:String=""
    private var patientName:String=""
    private var doctorName:String=""
    private var appointmentDate:String=""
    private var fromTime:String=""
    private var toTime:String=""
    private var clinicname:String=""
    private var appointmentId:String=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_appointment_reschedule
    override val viewModel: FragmentDoctorAppointmentRescheduleViewModel
        get() {
            fragmentDoctorAppointmentRescheduleViewModel =
                ViewModelProviders.of(this).get(FragmentDoctorAppointmentRescheduleViewModel::class.java!!)
            return fragmentDoctorAppointmentRescheduleViewModel as FragmentDoctorAppointmentRescheduleViewModel
        }

    companion object {
        fun newInstance(id: String,doctorid: String,patientname:String,doctorName:String,appointmentdate:String,fromdate:String,todate:String,clinicName:String): FragmentDoctorAppointmentReschedule {
            val args = Bundle()
            args.putString("id",id)
            args.putString("doctorid",doctorid)
            args.putString("patientname",patientname)
            args.putString("doctorName",doctorName)
            args.putString("appointmentdate",appointmentdate)
            args.putString("fromdate",fromdate)
            args.putString("todate",fromdate)
            args.putString("clinicName",clinicName)

            val fragment = FragmentDoctorAppointmentReschedule()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorAppointmentRescheduleViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorAppointmentRescheduleBinding = viewDataBinding

        if (arguments!=null && arguments?.getString("id")!=null){
            appointmentId = arguments?.getString("id")!!
            Log.d("Doctor Id", ": " + appointmentId )
        }
        if (arguments!=null && arguments?.getString("doctorid")!=null){
            doctorId = arguments?.getString("doctorid")!!
            Log.d("Doctor Id", ": " + doctorId )
        }

        if (arguments!=null && arguments?.getString("patientname")!=null){
            patientName = arguments?.getString("patientname")!!
            Log.d("patientname", ": " + patientName )
        }

        if (arguments!=null && arguments?.getString("doctorName")!=null){
            doctorName = arguments?.getString("doctorName")!!
            Log.d("doctorName", ": " + doctorName )
        }

        if (arguments!=null && arguments?.getString("appointmentdate")!=null){
            appointmentDate = arguments?.getString("appointmentdate")!!
            Log.d("appointmentdate", ": " + appointmentDate )
        }

        if (arguments!=null && arguments?.getString("fromdate")!=null){
            fromTime = arguments?.getString("fromdate")!!
            Log.d("fromdate", ": " + fromTime )
        }

        if (arguments!=null && arguments?.getString("todate")!=null){
            toTime = arguments?.getString("todate")!!
            Log.d("todate", ": " + toTime )
        }

        if (arguments!=null && arguments?.getString("clinicName")!=null){
            clinicname = arguments?.getString("clinicName")!!
            Log.d("clinicName", ": " + clinicname )
        }
        // Set todays date and get clinic list and doctor according to todays date
        var c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        var df = SimpleDateFormat("yyyy-MM-dd");
        var formattedDate = df.format(c);
        selectDoctorSlotApiCall(formattedDate)


        fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.setOnClickListener(View.OnClickListener {
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
                fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.setText("" + year + "-" + monthstr + "-" + dayStr)
                appointmentDate= fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text?.toString()!!
                if(! fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text?.toString().equals("") && fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text!=null ){
                    selectDoctorSlotApiCall( fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.text?.toString()!!)
                }

            }, year, month, day)

            dpd.show()
            //Get the DatePicker instance from DatePickerDialog
            //Get the DatePicker instance from DatePickerDialog
            val dp = dpd.datePicker
            dp.minDate=System.currentTimeMillis() - 1000
        })

        fragmentDoctorAppointmentRescheduleBinding?.edtReschedulePatientname?.setText(patientName)
        fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleAppointmentdate?.setText(appointmentDate)
        fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText(fromTime)
        fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText(toTime)
        fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleClinicName?.setText(clinicname)

        fragmentDoctorAppointmentRescheduleBinding?.btnAppointmentBooking?.setOnClickListener(View.OnClickListener {

//            fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText(fromTime)
//            fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText(toTime)
            fromTime = fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleStartTime?.text?.toString()!!
            toTime= fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleEndTime?.text?.toString()!!
            if(!fromTime.equals("") &&!toTime.equals("") ){
                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var doctorAppointmentRescheduleRequest= DoctorAppointmentRescheduleRequest()
                            doctorAppointmentRescheduleRequest?.id=appointmentId
                            doctorAppointmentRescheduleRequest?.serviceType="doctor"
                            doctorAppointmentRescheduleRequest?.fromDate=appointmentDate
                            doctorAppointmentRescheduleRequest?.toDate=appointmentDate
                            doctorAppointmentRescheduleRequest?.fromTime=fromTime
                            doctorAppointmentRescheduleRequest?.toTime=toTime

                            fragmentDoctorAppointmentRescheduleViewModel?.apirescheduleappointment(doctorAppointmentRescheduleRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Reschedule Appointment", "Are you sure to reschedule this family member?")
            }else{
                Toast.makeText(activity, "Please  select time slot to reschedule booking." , Toast.LENGTH_SHORT).show()
            }



        })


    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorSloytListingRecyclerview(doctorprivateList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorAppointmentRescheduleBinding!!.recyclerViewDoctorslot != null)
        val recyclerView = fragmentDoctorAppointmentRescheduleBinding!!.recyclerViewDoctorslot
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterDoctorPrivateSlot(doctorprivateList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClick= object : OnDoctorPrivateSlotClickListner {
            override fun onItemClick(modelData: com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.ResultItem?) {
//                clinicId= modelData?.clinicId!!
//                fromTime= modelData?.timeFrom!!
//                toTime= modelData?.timeTo!!
                fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleClinicName?.setText(modelData?.clinicName)
                fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText("")
                fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText("")
                if(modelData?.slot!=null && modelData?.slot?.size>0){
                    fragmentDoctorAppointmentRescheduleBinding?.recyclerViewTimeslotby30minute?.visibility=View.VISIBLE
                    fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.visibility=View.GONE
                    setUpToTimeListingRecyclerview(modelData?.slot)
                }else{
                    fragmentDoctorAppointmentRescheduleBinding?.recyclerViewTimeslotby30minute?.visibility=View.GONE
                    fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.visibility=View.VISIBLE
                    fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.text="No slot available for booking."
                }
//

            }


        }

    }

    // Set up recycler view for service listing if available
    private fun setUpToTimeListingRecyclerview(slotList: ArrayList<SlotItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorAppointmentRescheduleBinding!!.recyclerViewTimeslotby30minute != null)
        val recyclerView = fragmentDoctorAppointmentRescheduleBinding!!.recyclerViewTimeslotby30minute
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterDoctorSlotDivision(slotList,context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView= object : OnDoctorSlotClickListner {
            override fun onSloctClick(position: SlotItem) {
                fromTime= position?.timeFrom!!
                toTime= position?.timeTo!!
                fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleStartTime?.setText(fromTime)
                fragmentDoctorAppointmentRescheduleBinding?.edtRescheduleEndTime?.setText(toTime)
            }
//            override fun onItemClick(modelData: com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.ResultItem?) {
//                clinicId= modelData?.clinicId!!
////                clinicFromTime= modelData?.timeFrom!!
////                clinicToTime= modelData?.timeTo!!
//            }


        }

    }

    override fun successDoctorPrivateSlotResponse(doctorPrivateSlotResponse: DoctorPrivateSlotResponse?) {
        baseActivity?.hideLoading()
        if(doctorPrivateSlotResponse?.code.equals("200")){
            Toast.makeText(activity, doctorPrivateSlotResponse?.message, Toast.LENGTH_SHORT).show()
            if (doctorPrivateSlotResponse?.result!=null && doctorPrivateSlotResponse?.result?.size>0){

                fragmentDoctorAppointmentRescheduleBinding?.btnAppointmentBooking?.visibility=View.VISIBLE
                fragmentDoctorAppointmentRescheduleBinding?.recyclerViewDoctorslot?.visibility=View.VISIBLE
                fragmentDoctorAppointmentRescheduleBinding?.recyclerViewTimeslotby30minute?.visibility=View.VISIBLE
                fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.visibility=View.GONE
                fragmentDoctorAppointmentRescheduleBinding?.tvSelectDoctorSlotNoDate?.visibility=View.GONE
//                clinicId= doctorPrivateSlotResponse?.result?.get(0)?.clinicId!!
//                clinicFromTime= doctorPrivateSlotResponse?.result?.get(0)?.timeFrom!!
//                clinicToTime= doctorPrivateSlotResponse?.result?.get(0)?.timeTo!!
                setUpDoctorSloytListingRecyclerview(doctorPrivateSlotResponse?.result)

                if(doctorPrivateSlotResponse?.result?.get(0)?.slot!=null && doctorPrivateSlotResponse?.result?.get(0)?.slot?.size!!>0){
                    fragmentDoctorAppointmentRescheduleBinding?.recyclerViewTimeslotby30minute?.visibility=View.VISIBLE
                    fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.visibility=View.GONE
                    setUpToTimeListingRecyclerview(doctorPrivateSlotResponse?.result?.get(0)?.slot)

                }else{
                    fragmentDoctorAppointmentRescheduleBinding?.recyclerViewTimeslotby30minute?.visibility=View.GONE
                    fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.visibility=View.VISIBLE
                    fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.text="No slot available for booking."
                }
            }else{
                fragmentDoctorAppointmentRescheduleBinding?.btnAppointmentBooking?.visibility=View.GONE
                fragmentDoctorAppointmentRescheduleBinding?.recyclerViewDoctorslot?.visibility=View.GONE
                fragmentDoctorAppointmentRescheduleBinding?.recyclerViewTimeslotby30minute?.visibility=View.GONE
                fragmentDoctorAppointmentRescheduleBinding?.tvSelectTimeslotby30minuteNoDate?.visibility=View.GONE
                fragmentDoctorAppointmentRescheduleBinding?.tvSelectDoctorSlotNoDate?.visibility=View.VISIBLE
                fragmentDoctorAppointmentRescheduleBinding?.tvSelectDoctorSlotNoDate?.setText("No Doctor Slot Found")
            }
        }else{
            fragmentDoctorAppointmentRescheduleBinding?.btnAppointmentBooking?.visibility=View.GONE
            fragmentDoctorAppointmentRescheduleBinding?.recyclerViewDoctorslot?.visibility=View.GONE
            fragmentDoctorAppointmentRescheduleBinding?.tvSelectDoctorSlotNoDate?.visibility=View.VISIBLE
            fragmentDoctorAppointmentRescheduleBinding?.tvSelectDoctorSlotNoDate?.setText("No Doctor Slot Found")
            Toast.makeText(activity, doctorPrivateSlotResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun successDoctorRescheduleResponse(doctorRescheduleResponse: DoctorRescheduleResponse?) {
        baseActivity?.hideLoading()
        if (doctorRescheduleResponse?.code.equals("200")){
            Toast.makeText(activity, doctorRescheduleResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentMyUpCommingAppointment.newInstance())
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


    private fun selectDoctorSlotApiCall(selectDate:String){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var doctorPrivateSlotRequest= DoctorPrivateSlotRequest()
            doctorPrivateSlotRequest?.date=selectDate
            doctorPrivateSlotRequest?.doctorId=doctorId

            fragmentDoctorAppointmentRescheduleViewModel?.apidoctorprivateslot(doctorPrivateSlotRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }
}