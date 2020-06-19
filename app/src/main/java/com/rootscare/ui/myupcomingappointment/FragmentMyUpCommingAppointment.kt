package com.rootscare.ui.myupcomingappointment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.google.android.material.tabs.TabLayout
import com.interfaces.OnClickWithTwoButton
import com.interfaces.OnUpcommingAppointmentBtnClickListner
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.appointmentrequest.AppointmentRequest
import com.rootscare.data.model.api.request.cancelappointmentrequest.CancelAppointmentRequest
import com.rootscare.data.model.api.response.appointcancelresponse.AppointmentCancelResponse
import com.rootscare.data.model.api.response.appointmenthistoryresponse.*
import com.rootscare.databinding.FragmentCancellMyUpcomingAppointmentBinding
import com.rootscare.databinding.FragmentMyUpcommingAppointmentBinding
import com.rootscare.databinding.FragmentUpcommingAppointmentNewBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.appointment.adapter.*
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetails
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.subfragment.editpatient.FragmentEditPatientFamilyMember
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointment
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentNavigator
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentViewModel
import com.rootscare.ui.cancellappointment.adapter.AdapterCancelMyUpcomingAppiontment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.myupcomingappointment.adapter.*
import com.rootscare.ui.profile.FragmentProfile
import com.rootscare.ui.recedule.doctor.FragmentDoctorAppointmentReschedule
import com.rootscare.ui.submitfeedback.FragmentSubmitReview
import java.util.*

class FragmentMyUpCommingAppointment : BaseFragment<FragmentUpcommingAppointmentNewBinding, FragmentMyUpCommingAppointmentViewModel>(), FragmentMyUpCommingAppointmentnavigator {
    private var fragmentUpcommingAppointmentNewBinding: FragmentUpcommingAppointmentNewBinding? = null
    private var fragmentMyUpCommingAppointmentViewModel: FragmentMyUpCommingAppointmentViewModel? = null
    var doctorAppointmentItemarrayList : ArrayList<DoctorAppointmentItem?>?=null
    var nursesAppointmentItemarrayList : ArrayList<NurseAppointmentItem?>? = null
    var physiotherapyAppointmentItemarrayList : ArrayList<PhysiotherapyAppointmentItem?>? = null
    var caregiverAppointmentItemArrayList: ArrayList<CaregiverAppointmentItem?>? = null
    var babysitterAppointmentItemArrayList: ArrayList<BabysitterAppointmentItem?>? = null
    var pathologyAppointmentItemArrayList: ArrayList<PathologyAppointmentItem?>? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_upcomming_appointment_new
    override val viewModel: FragmentMyUpCommingAppointmentViewModel
        get() {
            fragmentMyUpCommingAppointmentViewModel =
                ViewModelProviders.of(this).get(FragmentMyUpCommingAppointmentViewModel::class.java!!)
            return fragmentMyUpCommingAppointmentViewModel as FragmentMyUpCommingAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentMyUpCommingAppointment {
            val args = Bundle()
            val fragment = FragmentMyUpCommingAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMyUpCommingAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentUpcommingAppointmentNewBinding = viewDataBinding
        //setUpAppointmentlistingRecyclerview()

        if(isNetworkConnected){
            baseActivity?.showLoading()
            var appointmentRequest= AppointmentRequest()
           appointmentRequest?.userId=fragmentMyUpCommingAppointmentViewModel?.appSharedPref?.userId
//            appointmentRequest?.userId="11"

            fragmentMyUpCommingAppointmentViewModel?.apipatientupcomingappointment(appointmentRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
        setUpTabLayout()
    }





    private fun setUpTabLayout() {
        val tabTitles: MutableList<String> =
            ArrayList()
        tabTitles.add("Doctor")
        tabTitles.add("Nurses")
        tabTitles.add("Pathology")
        tabTitles.add("Physiotherapy")
        tabTitles.add("Caregiver")
        tabTitles.add("Babysitter")
        for (i in tabTitles.indices) {
            fragmentUpcommingAppointmentNewBinding?.tablayout?.addTab(
                fragmentUpcommingAppointmentNewBinding?.tablayout?.newTab()?.setText(
                    tabTitles[i]
                )!!, i
            )
        }
        //        activityOrderBinding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (i in 0 until fragmentUpcommingAppointmentNewBinding?.tablayout?.getTabCount()!!) {
            val view: View =
                LayoutInflater.from(activity).inflate(R.layout.tab_item_appointment_layout, null)
            val tab_item_tv = view.findViewById<TextView>(R.id.tab_item_tv)
            tab_item_tv.text = tabTitles[i]
            if (i == 0) {
                tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
                view.background = resources.getDrawable(R.drawable.tab_background_selected)
            } else {
                tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
//                tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                view.background = resources.getDrawable(R.drawable.tab_background_unselected)
            }
            fragmentUpcommingAppointmentNewBinding?.tablayout?.getTabAt(i)?.customView = view
        }
        fragmentUpcommingAppointmentNewBinding?.tablayout?.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until fragmentUpcommingAppointmentNewBinding?.tablayout?.getTabCount()!!) {
//                    val view: View = fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView!!
                    val view: View = Objects.requireNonNull<View>(
                        fragmentUpcommingAppointmentNewBinding?.tablayout?.getTabAt(i)?.customView
                    )

                    val tab_item_tv =
                        Objects.requireNonNull(view)
                            .findViewById<TextView>(R.id.tab_item_tv)
                    if (i == tab.position) {
                        tab_item_tv.setTextColor(resources.getColor(R.color.background_white))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_selected)
                    } else {
                        tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
//                        tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_unselected)
                    }
                }
                if (tab.position == 0) {
                    fragmentUpcommingAppointmentNewBinding?.llDoctorAppointmentList?.visibility=View.VISIBLE
                    fragmentUpcommingAppointmentNewBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llBabysitterAppointmentList?.visibility=View.GONE


//                    addFragment(
//                        PendingFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                } else if (tab.position == 1) {
                    fragmentUpcommingAppointmentNewBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llNursesAppointmentList?.visibility=View.VISIBLE
                    fragmentUpcommingAppointmentNewBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llBabysitterAppointmentList?.visibility=View.GONE


//                    )
                } else if (tab.position == 2) {
                    fragmentUpcommingAppointmentNewBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llHospitalAppointmentList?.visibility=View.VISIBLE
                    fragmentUpcommingAppointmentNewBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llBabysitterAppointmentList?.visibility=View.GONE
                }else if(tab.position == 3){
                    fragmentUpcommingAppointmentNewBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llPhysitherapyAppointmentList?.visibility=View.VISIBLE
                    fragmentUpcommingAppointmentNewBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llBabysitterAppointmentList?.visibility=View.GONE
                }else if(tab.position == 4){
                    fragmentUpcommingAppointmentNewBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llCaregiverAppointmentList?.visibility=View.VISIBLE
                    fragmentUpcommingAppointmentNewBinding?.llBabysitterAppointmentList?.visibility=View.GONE
                }else if(tab.position == 5){
                    fragmentUpcommingAppointmentNewBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentUpcommingAppointmentNewBinding?.llBabysitterAppointmentList?.visibility=View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        fragmentUpcommingAppointmentNewBinding?.llDoctorAppointmentList?.visibility=View.VISIBLE
        fragmentUpcommingAppointmentNewBinding?.llNursesAppointmentList?.visibility=View.GONE
        fragmentUpcommingAppointmentNewBinding?.llHospitalAppointmentList?.visibility=View.GONE
        fragmentUpcommingAppointmentNewBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
        fragmentUpcommingAppointmentNewBinding?.llCaregiverAppointmentList?.visibility=View.GONE
        fragmentUpcommingAppointmentNewBinding?.llBabysitterAppointmentList?.visibility=View.GONE

    }


    // Set up recycler view for Doctor Appointment listing if available
    private fun setUpAppointmentlistingRecyclerview(doctorAppointmentList: ArrayList<DoctorAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentUpcommingAppointmentNewBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist != null)
        val recyclerView = fragmentUpcommingAppointmentNewBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        val contactListAdapter = AdapteMyUpComingAppointment(doctorAppointmentList,context!!)
        recyclerView?.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object :
            OnUpcommingAppointmentBtnClickListner {
            override fun onCancelBtnClick(id: String) {
                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
//

                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var cancelAppointmentRequest= CancelAppointmentRequest()
                            cancelAppointmentRequest?.id=id
                            cancelAppointmentRequest?.serviceType="doctor"
                            fragmentMyUpCommingAppointmentViewModel?.apicancelappointment(cancelAppointmentRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Cancel Appointment", "Are you sure to cancel this appointment?")


            }

            override fun onRescheduleBtnClick(modelDoctorAppointmentItem: DoctorAppointmentItem) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorAppointmentReschedule.newInstance(modelDoctorAppointmentItem?.id!!,modelDoctorAppointmentItem?.doctorId!!,
                        modelDoctorAppointmentItem?.doctorName!!,
                        modelDoctorAppointmentItem?.patientName!!,
                        modelDoctorAppointmentItem?.appointmentDate!!,
                        modelDoctorAppointmentItem?.fromTime!!, modelDoctorAppointmentItem?.toTime!!))
            }


        }

    }

    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpNursesAppointmentlistingRecyclerview(nurseAppointmentList: ArrayList<NurseAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentUpcommingAppointmentNewBinding!!.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist != null)
        val recyclerView = fragmentUpcommingAppointmentNewBinding!!.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapteNurseUpComingAppointment(nurseAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
//

                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var cancelAppointmentRequest= CancelAppointmentRequest()
                            cancelAppointmentRequest?.id=id.toString()
                            cancelAppointmentRequest?.serviceType="nurse"
                            fragmentMyUpCommingAppointmentViewModel?.apicancelappointment(cancelAppointmentRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Cancel Appointment", "Are you sure to cancel this appointment?")
            }

        }

    }
//    "message": "User Type must be from the list. (doctor,hospital,nurse,caregiver,babysitter,physiotherapy,lab-technician)",
    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpHospitalAppointmentlistingRecyclerview(pathologyAppointmentList: ArrayList<PathologyAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentUpcommingAppointmentNewBinding!!.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist != null)
        val recyclerView = fragmentUpcommingAppointmentNewBinding!!.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapteHospitalUpComingAppointment(pathologyAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
//

                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var cancelAppointmentRequest= CancelAppointmentRequest()
                            cancelAppointmentRequest?.id=id.toString()
                            cancelAppointmentRequest?.serviceType="hospital"
                            fragmentMyUpCommingAppointmentViewModel?.apicancelappointment(cancelAppointmentRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Cancel Appointment", "Are you sure to cancel this appointment?")
            }

        }

    }

    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpPhysitherapyAppointmentlistingRecyclerview(physiotherapyAppointmentList: ArrayList<PhysiotherapyAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentUpcommingAppointmentNewBinding!!.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist != null)
        val recyclerView = fragmentUpcommingAppointmentNewBinding!!.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdaptePhysiotherapylUpComingAppointment(physiotherapyAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
//

                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var cancelAppointmentRequest= CancelAppointmentRequest()
                            cancelAppointmentRequest?.id=id.toString()
                            cancelAppointmentRequest?.serviceType="physiotherapy"
                            fragmentMyUpCommingAppointmentViewModel?.apicancelappointment(cancelAppointmentRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Cancel Appointment", "Are you sure to cancel this appointment?")
            }

        }

    }

    // Set up recycler view for Caregiger Appointment listing if available
    private fun setUpCaregiverAppointmentlistingRecyclerview(caregiverAppointmentList: ArrayList<CaregiverAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentUpcommingAppointmentNewBinding!!.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist != null)
        val recyclerView = fragmentUpcommingAppointmentNewBinding!!.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapteCareGiverUpComingAppointment(caregiverAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
//

                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var cancelAppointmentRequest= CancelAppointmentRequest()
                            cancelAppointmentRequest?.id=id.toString()
                            cancelAppointmentRequest?.serviceType="caregiver"
                            fragmentMyUpCommingAppointmentViewModel?.apicancelappointment(cancelAppointmentRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Cancel Appointment", "Are you sure to cancel this appointment?")
            }

        }

    }

    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpBabysitterAppointmentlistingRecyclerview(babysitterAppointmentList: ArrayList<BabysitterAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentUpcommingAppointmentNewBinding!!.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist != null)
        val recyclerView = fragmentUpcommingAppointmentNewBinding!!.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapteBabySitterUpComingAppointment(babysitterAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentAppiontmentDetails.newInstance("1"))

                CommonDialog.showDialog(context!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {
//

                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var cancelAppointmentRequest= CancelAppointmentRequest()
                            cancelAppointmentRequest?.id=id.toString()
                            cancelAppointmentRequest?.serviceType="babysitter"
                            fragmentMyUpCommingAppointmentViewModel?.apicancelappointment(cancelAppointmentRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, "Cancel Appointment", "Are you sure to cancel this appointment?")
            }

        }

    }

    override fun successAppointmentHistoryResponse(appointmentHistoryResponse: AppointmentHistoryResponse?) {
        baseActivity?.hideLoading()
        if(appointmentHistoryResponse?.code.equals("200")){


            if (appointmentHistoryResponse?.result?.doctorAppointment!=null && appointmentHistoryResponse?.result?.doctorAppointment.size>0){
                doctorAppointmentItemarrayList = ArrayList<DoctorAppointmentItem?>()
                doctorAppointmentItemarrayList=appointmentHistoryResponse?.result?.doctorAppointment
            }
            if (appointmentHistoryResponse?.result?.nurseAppointment!=null && appointmentHistoryResponse?.result?.nurseAppointment.size>0){
                nursesAppointmentItemarrayList = ArrayList<NurseAppointmentItem?>()
                nursesAppointmentItemarrayList=appointmentHistoryResponse?.result?.nurseAppointment
            }
            if(appointmentHistoryResponse?.result?.physiotherapyAppointment!=null && appointmentHistoryResponse?.result?.physiotherapyAppointment.size>0){
                physiotherapyAppointmentItemarrayList= ArrayList<PhysiotherapyAppointmentItem?>()
                physiotherapyAppointmentItemarrayList=appointmentHistoryResponse?.result?.physiotherapyAppointment
            }

            if(appointmentHistoryResponse?.result?.caregiverAppointment!=null && appointmentHistoryResponse?.result?.caregiverAppointment.size>0){
                caregiverAppointmentItemArrayList = ArrayList<CaregiverAppointmentItem?>()
                caregiverAppointmentItemArrayList=appointmentHistoryResponse?.result?.caregiverAppointment
            }
            if(appointmentHistoryResponse?.result?.babysitterAppointment!=null && appointmentHistoryResponse?.result?.babysitterAppointment?.size>0){
                babysitterAppointmentItemArrayList = ArrayList<BabysitterAppointmentItem?>()
                babysitterAppointmentItemArrayList = appointmentHistoryResponse?.result?.babysitterAppointment
            }

            if(appointmentHistoryResponse?.result?.pathologyAppointment!=null && appointmentHistoryResponse?.result?.pathologyAppointment?.size>0){
                pathologyAppointmentItemArrayList = ArrayList<PathologyAppointmentItem?>()
                pathologyAppointmentItemArrayList=appointmentHistoryResponse?.result?.pathologyAppointment
            }
            defaultDoctorListSetup(doctorAppointmentItemarrayList)
            defaultNursesListSetup(nursesAppointmentItemarrayList)
            defaultPhysitherapyListSetup(physiotherapyAppointmentItemarrayList)
            defaultCaregiverListSetup(caregiverAppointmentItemArrayList)
            defaultBabysitterListSetup(babysitterAppointmentItemArrayList)
            defaultPathologyListSetup(pathologyAppointmentItemArrayList)



        }else{
            doctorAppointmentItemarrayList = ArrayList<DoctorAppointmentItem?>()
            nursesAppointmentItemarrayList = ArrayList<NurseAppointmentItem?>()
            physiotherapyAppointmentItemarrayList= ArrayList<PhysiotherapyAppointmentItem?>()
            caregiverAppointmentItemArrayList = ArrayList<CaregiverAppointmentItem?>()
            babysitterAppointmentItemArrayList = ArrayList<BabysitterAppointmentItem?>()
            pathologyAppointmentItemArrayList = ArrayList<PathologyAppointmentItem?>()
            defaultDoctorListSetup(doctorAppointmentItemarrayList)
            defaultNursesListSetup(nursesAppointmentItemarrayList)
            defaultPhysitherapyListSetup(physiotherapyAppointmentItemarrayList)
            defaultCaregiverListSetup(caregiverAppointmentItemArrayList)
            defaultBabysitterListSetup(babysitterAppointmentItemArrayList)
            defaultPathologyListSetup(pathologyAppointmentItemArrayList)

        }

    }

    override fun successAppointmentCancelResponse(appointmentCancelResponse: AppointmentCancelResponse?) {
        baseActivity?.hideLoading()
        if(appointmentCancelResponse?.code.equals("200")){
            Toast.makeText(activity, appointmentCancelResponse?.message, Toast.LENGTH_SHORT).show()
            if(isNetworkConnected){
                baseActivity?.showLoading()
                var appointmentRequest= AppointmentRequest()
                appointmentRequest?.userId=fragmentMyUpCommingAppointmentViewModel?.appSharedPref?.userId
//            appointmentRequest?.userId="11"

                fragmentMyUpCommingAppointmentViewModel?.apipatientupcomingappointment(appointmentRequest)

            }else{
                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(activity, appointmentCancelResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorAppointmentHistoryResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentProfile.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun  defaultDoctorListSetup(doctorAppointmentList: ArrayList<DoctorAppointmentItem?>?){

        if(doctorAppointmentList!=null && doctorAppointmentList!!.size>0){
            fragmentUpcommingAppointmentNewBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutDoctorList?.tvNoDate?.visibility=View.GONE
            setUpAppointmentlistingRecyclerview(doctorAppointmentList)


        }else{
            fragmentUpcommingAppointmentNewBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist?.visibility=View.GONE
            fragmentUpcommingAppointmentNewBinding?.layoutDoctorList?.tvNoDate?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutDoctorList?.tvNoDate?.setText("No appointment for doctor booking.")
        }

    }

    private fun  defaultNursesListSetup(nurseAppointmentList: ArrayList<NurseAppointmentItem?>?){

        if(nurseAppointmentList!=null && nurseAppointmentList!!.size>0){
            fragmentUpcommingAppointmentNewBinding?.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutNursesList?.tvNoDateNuurses?.visibility=View.GONE
            setUpNursesAppointmentlistingRecyclerview(nurseAppointmentList)


        }else{
            fragmentUpcommingAppointmentNewBinding?.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist?.visibility=View.GONE
            fragmentUpcommingAppointmentNewBinding?.layoutNursesList?.tvNoDateNuurses?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutNursesList?.tvNoDateNuurses?.setText("No appointment for nurse booking.")
        }

    }

    private fun  defaultPhysitherapyListSetup(nphysiotherapyAppointmentList: ArrayList<PhysiotherapyAppointmentItem?>?){

        if(nphysiotherapyAppointmentList!=null && nphysiotherapyAppointmentList!!.size>0){
            fragmentUpcommingAppointmentNewBinding?.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutPhysitherapyList?.tvNoDatePhysitherapy?.visibility=View.GONE
            setUpPhysitherapyAppointmentlistingRecyclerview(nphysiotherapyAppointmentList)


        }else{
            fragmentUpcommingAppointmentNewBinding?.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist?.visibility=View.GONE
            fragmentUpcommingAppointmentNewBinding?.layoutPhysitherapyList?.tvNoDatePhysitherapy?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutPhysitherapyList?.tvNoDatePhysitherapy?.setText("No appointment for physiotherapy booking.")
        }

    }

    private fun  defaultCaregiverListSetup(caregiverAppointmentList: ArrayList<CaregiverAppointmentItem?>?){

        if(caregiverAppointmentList!=null && caregiverAppointmentList!!.size>0){
            fragmentUpcommingAppointmentNewBinding?.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutCaregiverList?.tvNoDateCaregiver?.visibility=View.GONE
            setUpCaregiverAppointmentlistingRecyclerview(caregiverAppointmentList)


        }else{
            fragmentUpcommingAppointmentNewBinding?.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist?.visibility=View.GONE
            fragmentUpcommingAppointmentNewBinding?.layoutCaregiverList?.tvNoDateCaregiver?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutCaregiverList?.tvNoDateCaregiver?.setText("No appointment for caregiver booking.")
        }

    }

    private fun  defaultBabysitterListSetup(babysitterAppointmentList: ArrayList<BabysitterAppointmentItem?>?){

        if(babysitterAppointmentList!=null && babysitterAppointmentList!!.size>0){
            fragmentUpcommingAppointmentNewBinding?.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutBabysitterList?.tvNoDateBabysitter?.visibility=View.GONE
            setUpBabysitterAppointmentlistingRecyclerview(babysitterAppointmentList)


        }else{
            fragmentUpcommingAppointmentNewBinding?.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist?.visibility=View.GONE
            fragmentUpcommingAppointmentNewBinding?.layoutBabysitterList?.tvNoDateBabysitter?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutBabysitterList?.tvNoDateBabysitter?.setText("No appointment for babysitter booking.")
        }

    }

    private fun  defaultPathologyListSetup(pathologyAppointmentList: ArrayList<PathologyAppointmentItem?>?){

        if(pathologyAppointmentList!=null && pathologyAppointmentList!!.size>0){
            fragmentUpcommingAppointmentNewBinding?.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutHospitalList?.tvNoDateHospital?.visibility=View.GONE
            setUpHospitalAppointmentlistingRecyclerview(pathologyAppointmentList)


        }else{
            fragmentUpcommingAppointmentNewBinding?.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist?.visibility=View.GONE
            fragmentUpcommingAppointmentNewBinding?.layoutHospitalList?.tvNoDateHospital?.visibility=View.VISIBLE
            fragmentUpcommingAppointmentNewBinding?.layoutHospitalList?.tvNoDateHospital?.setText("No appointment for pathology booking.")
        }

    }
}
