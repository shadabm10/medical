package com.rootscare.ui.appointment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.appointmentrequest.AppointmentRequest
import com.rootscare.data.model.api.request.patientpaymenthistoryreuest.PatientPaymentHistoryRequest
import com.rootscare.data.model.api.response.appointmenthistoryresponse.*
import com.rootscare.databinding.FragmentAppointmentBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.appointment.adapter.*
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetails
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import com.rootscare.ui.nurses.appointmentdetails.FragmentNurseAppointmentDetails
import com.rootscare.ui.nurses.review.FragmentNurseReviewSubmit
import com.rootscare.ui.profile.FragmentProfile
import com.rootscare.ui.profile.FragmentProfileNavigator
import com.rootscare.ui.profile.FragmentProfileViewModel
import com.rootscare.ui.submitfeedback.FragmentSubmitReview
import java.util.*

class FragmentAppointment : BaseFragment<FragmentAppointmentBinding, FragmentAppointmentViewModel>(), FragmentAppointmentNavigator {
    private var fragmentAppointmentBinding: FragmentAppointmentBinding? = null
    private var fragmentAppointmentViewModel: FragmentAppointmentViewModel? = null
     var doctorAppointmentItemarrayList : ArrayList<DoctorAppointmentItem?>?=null
    var nursesAppointmentItemarrayList : ArrayList<NurseAppointmentItem?>? = null
    var physiotherapyAppointmentItemarrayList : ArrayList<PhysiotherapyAppointmentItem?>? = null
    var caregiverAppointmentItemArrayList: ArrayList<CaregiverAppointmentItem?>? = null
    var babysitterAppointmentItemArrayList: ArrayList<BabysitterAppointmentItem?>? = null
    var pathologyAppointmentItemArrayList: ArrayList<PathologyAppointmentItem?>? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_appointment
    override val viewModel: FragmentAppointmentViewModel
        get() {
            fragmentAppointmentViewModel =
                ViewModelProviders.of(this).get(FragmentAppointmentViewModel::class.java!!)
            return fragmentAppointmentViewModel as FragmentAppointmentViewModel
        }
    companion object {
        fun newInstance(): FragmentAppointment {
            val args = Bundle()
            val fragment = FragmentAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAppointmentBinding = viewDataBinding



        if(isNetworkConnected){
            baseActivity?.showLoading()
            var appointmentRequest= AppointmentRequest()
            appointmentRequest?.userId=fragmentAppointmentViewModel?.appSharedPref?.userId
//            appointmentRequest?.userId="11"

            fragmentAppointmentViewModel?.apipatientappointmenthistory(appointmentRequest)

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
            fragmentAppointmentBinding?.tablayout?.addTab(
                fragmentAppointmentBinding?.tablayout?.newTab()?.setText(
                    tabTitles[i]
                )!!, i
            )
        }
        //        activityOrderBinding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (i in 0 until fragmentAppointmentBinding?.tablayout?.getTabCount()!!) {
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
            fragmentAppointmentBinding?.tablayout?.getTabAt(i)?.customView = view
        }
        fragmentAppointmentBinding?.tablayout?.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until fragmentAppointmentBinding?.tablayout?.getTabCount()!!) {
//                    val view: View = fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView!!
                    val view: View = Objects.requireNonNull<View>(
                        fragmentAppointmentBinding?.tablayout?.getTabAt(i)?.customView
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
                    fragmentAppointmentBinding?.llDoctorAppointmentList?.visibility=View.VISIBLE
                    fragmentAppointmentBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llBabysitterAppointmentList?.visibility=View.GONE


//                    addFragment(
//                        PendingFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                } else if (tab.position == 1) {
                    fragmentAppointmentBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llNursesAppointmentList?.visibility=View.VISIBLE
                    fragmentAppointmentBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llBabysitterAppointmentList?.visibility=View.GONE


//                    )
                } else if (tab.position == 2) {
                    fragmentAppointmentBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llHospitalAppointmentList?.visibility=View.VISIBLE
                    fragmentAppointmentBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llBabysitterAppointmentList?.visibility=View.GONE
                }else if(tab.position == 3){
                    fragmentAppointmentBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llPhysitherapyAppointmentList?.visibility=View.VISIBLE
                    fragmentAppointmentBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llBabysitterAppointmentList?.visibility=View.GONE
                }else if(tab.position == 4){
                    fragmentAppointmentBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llCaregiverAppointmentList?.visibility=View.VISIBLE
                    fragmentAppointmentBinding?.llBabysitterAppointmentList?.visibility=View.GONE
                }else if(tab.position == 5){
                    fragmentAppointmentBinding?.llDoctorAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llNursesAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llHospitalAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llCaregiverAppointmentList?.visibility=View.GONE
                    fragmentAppointmentBinding?.llBabysitterAppointmentList?.visibility=View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        fragmentAppointmentBinding?.llDoctorAppointmentList?.visibility=View.VISIBLE
        fragmentAppointmentBinding?.llNursesAppointmentList?.visibility=View.GONE
        fragmentAppointmentBinding?.llHospitalAppointmentList?.visibility=View.GONE
        fragmentAppointmentBinding?.llPhysitherapyAppointmentList?.visibility=View.GONE
        fragmentAppointmentBinding?.llCaregiverAppointmentList?.visibility=View.GONE
        fragmentAppointmentBinding?.llBabysitterAppointmentList?.visibility=View.GONE



    }

    // Set up recycler view for Doctor Appointment listing if available
    private fun setUpAppointmentlistingRecyclerview(doctorAppointmentList: ArrayList<DoctorAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentAppointmentBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist != null)
        val recyclerView = fragmentAppointmentBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        val contactListAdapter = AdapterAppointmentListRecyclerView(doctorAppointmentList,context!!)
        recyclerView?.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClick= object : OnClickWithTwoButton {
            override fun onFirstItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentAppiontmentDetails.newInstance(id.toString()))
            }

            override fun onSecondItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentSubmitReview.newInstance(id.toString()))
            }

        }

    }

    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpNursesAppointmentlistingRecyclerview(nurseAppointmentList: ArrayList<NurseAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentAppointmentBinding!!.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist != null)
        val recyclerView = fragmentAppointmentBinding!!.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNursesAppointmentlistRecyclerview(nurseAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClick= object : OnClickWithTwoButton {
            override fun onFirstItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentNurseAppointmentDetails.newInstance(id.toString()))
            }

            override fun onSecondItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentNurseReviewSubmit.newInstance(id.toString()))
            }

        }

    }

    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpHospitalAppointmentlistingRecyclerview(pathologyAppointmentList: ArrayList<PathologyAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentAppointmentBinding!!.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist != null)
        val recyclerView = fragmentAppointmentBinding!!.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalAppointmentlistRecyclerview(pathologyAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentAppiontmentDetails.newInstance("1"))
            }

        }

    }

    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpPhysitherapyAppointmentlistingRecyclerview(physiotherapyAppointmentList: ArrayList<PhysiotherapyAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentAppointmentBinding!!.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist != null)
        val recyclerView = fragmentAppointmentBinding!!.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterPhysitherarpyAppointmentlistRecyclerview(physiotherapyAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentAppiontmentDetails.newInstance("1"))
            }

        }

    }

    // Set up recycler view for Caregiger Appointment listing if available
    private fun setUpCaregiverAppointmentlistingRecyclerview(caregiverAppointmentList: ArrayList<CaregiverAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentAppointmentBinding!!.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist != null)
        val recyclerView = fragmentAppointmentBinding!!.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverAppointmentlistRecyclerview(caregiverAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentAppiontmentDetails.newInstance("1"))
            }

        }

    }

    // Set up recycler view for Nurses Appointment listing if available
    private fun setUpBabysitterAppointmentlistingRecyclerview(babysitterAppointmentList: ArrayList<BabysitterAppointmentItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentAppointmentBinding!!.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist != null)
        val recyclerView = fragmentAppointmentBinding!!.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterBabysitterAppointmentlistRecyclerview(babysitterAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentAppiontmentDetails.newInstance("1"))
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

    override fun errorAppointmentHistoryResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentProfile.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun  defaultDoctorListSetup(doctorAppointmentList: ArrayList<DoctorAppointmentItem?>?){

        if(doctorAppointmentList!=null && doctorAppointmentList!!.size>0){
            fragmentAppointmentBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutDoctorList?.tvNoDate?.visibility=View.GONE
            setUpAppointmentlistingRecyclerview(doctorAppointmentList)


        }else{
            fragmentAppointmentBinding?.layoutDoctorList?.recyclerViewRootscareAppointmentlist?.visibility=View.GONE
            fragmentAppointmentBinding?.layoutDoctorList?.tvNoDate?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutDoctorList?.tvNoDate?.setText("No appointment for doctor booking.")
        }

    }

    private fun  defaultNursesListSetup(nurseAppointmentList: ArrayList<NurseAppointmentItem?>?){

        if(nurseAppointmentList!=null && nurseAppointmentList!!.size>0){
            fragmentAppointmentBinding?.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutNursesList?.tvNoDateNuurses?.visibility=View.GONE
            setUpNursesAppointmentlistingRecyclerview(nurseAppointmentList)


        }else{
            fragmentAppointmentBinding?.layoutNursesList?.recyclerViewRootscareNursesAppointmentlist?.visibility=View.GONE
            fragmentAppointmentBinding?.layoutNursesList?.tvNoDateNuurses?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutNursesList?.tvNoDateNuurses?.setText("No appointment for nurse booking.")
        }

    }

    private fun  defaultPhysitherapyListSetup(nphysiotherapyAppointmentList: ArrayList<PhysiotherapyAppointmentItem?>?){

        if(nphysiotherapyAppointmentList!=null && nphysiotherapyAppointmentList!!.size>0){
            fragmentAppointmentBinding?.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutPhysitherapyList?.tvNoDatePhysitherapy?.visibility=View.GONE
            setUpPhysitherapyAppointmentlistingRecyclerview(nphysiotherapyAppointmentList)


        }else{
            fragmentAppointmentBinding?.layoutPhysitherapyList?.recyclerViewRootscarePhysitherapyAppointmentlist?.visibility=View.GONE
            fragmentAppointmentBinding?.layoutPhysitherapyList?.tvNoDatePhysitherapy?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutPhysitherapyList?.tvNoDatePhysitherapy?.setText("No appointment for physiotherapy booking.")
        }

    }

    private fun  defaultCaregiverListSetup(caregiverAppointmentList: ArrayList<CaregiverAppointmentItem?>?){

        if(caregiverAppointmentList!=null && caregiverAppointmentList!!.size>0){
            fragmentAppointmentBinding?.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutCaregiverList?.tvNoDateCaregiver?.visibility=View.GONE
            setUpCaregiverAppointmentlistingRecyclerview(caregiverAppointmentList)


        }else{
            fragmentAppointmentBinding?.layoutCaregiverList?.recyclerViewRootscareCaregiverAppointmentlist?.visibility=View.GONE
            fragmentAppointmentBinding?.layoutCaregiverList?.tvNoDateCaregiver?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutCaregiverList?.tvNoDateCaregiver?.setText("No appointment for caregiver booking.")
        }

    }

    private fun  defaultBabysitterListSetup(babysitterAppointmentList: ArrayList<BabysitterAppointmentItem?>?){

        if(babysitterAppointmentList!=null && babysitterAppointmentList!!.size>0){
            fragmentAppointmentBinding?.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutBabysitterList?.tvNoDateBabysitter?.visibility=View.GONE
            setUpBabysitterAppointmentlistingRecyclerview(babysitterAppointmentList)


        }else{
            fragmentAppointmentBinding?.layoutBabysitterList?.recyclerViewRootscareBabysitterAppointmentlist?.visibility=View.GONE
            fragmentAppointmentBinding?.layoutBabysitterList?.tvNoDateBabysitter?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutBabysitterList?.tvNoDateBabysitter?.setText("No appointment for babysitter booking.")
        }

    }

    private fun  defaultPathologyListSetup(pathologyAppointmentList: ArrayList<PathologyAppointmentItem?>?){

        if(pathologyAppointmentList!=null && pathologyAppointmentList!!.size>0){
            fragmentAppointmentBinding?.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutHospitalList?.tvNoDateHospital?.visibility=View.GONE
            setUpHospitalAppointmentlistingRecyclerview(pathologyAppointmentList)


        }else{
            fragmentAppointmentBinding?.layoutHospitalList?.recyclerViewRootscareHospitalAppointmentlist?.visibility=View.GONE
            fragmentAppointmentBinding?.layoutHospitalList?.tvNoDateHospital?.visibility=View.VISIBLE
            fragmentAppointmentBinding?.layoutHospitalList?.tvNoDateHospital?.setText("No appointment for pathology booking.")
        }

    }
}