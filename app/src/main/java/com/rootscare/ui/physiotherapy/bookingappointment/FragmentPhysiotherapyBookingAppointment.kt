package com.rootscare.ui.physiotherapy.bookingappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentPhysiotherapyBookingAppiontmentBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.adapter.AdapterFromTimeRecyclerview
import com.rootscare.ui.bookingappointment.adapter.AdapterToTimeRecyclerView
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.nursesbookingappointment.adapter.AdapterSelectHourytimeRecyclerView
import com.rootscare.ui.physiotherapy.adapter.AdapterPhysiotherapySlotRecyclerview
import com.rootscare.ui.physiotherapy.bookingdetails.FragmentPhysiotherapyBookingDetails

class FragmentPhysiotherapyBookingAppointment : BaseFragment<FragmentPhysiotherapyBookingAppiontmentBinding, FragmentPhysiotherapyBookingAppointmentViewModel>(),
    FragmentPhysiotherapyBookingAppointmentNavigator {
    private var fragmentPhysiotherapyBookingAppiontmentBinding: FragmentPhysiotherapyBookingAppiontmentBinding? = null
    private var fragmentPhysiotherapyBookingAppointmentViewModel: FragmentPhysiotherapyBookingAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physiotherapy_booking_appiontment
    override val viewModel: FragmentPhysiotherapyBookingAppointmentViewModel
        get() {
            fragmentPhysiotherapyBookingAppointmentViewModel =
                ViewModelProviders.of(this).get(FragmentPhysiotherapyBookingAppointmentViewModel::class.java!!)
            return fragmentPhysiotherapyBookingAppointmentViewModel as FragmentPhysiotherapyBookingAppointmentViewModel
        }
    companion object {
        fun newInstance(): FragmentPhysiotherapyBookingAppointment {
            val args = Bundle()
            val fragment = FragmentPhysiotherapyBookingAppointment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPhysiotherapyBookingAppointmentViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotherapyBookingAppiontmentBinding = viewDataBinding
//        setUpAddPatientListingRecyclerview()
        setUpFromTimeListingRecyclerview()
        setUpToTimeListingRecyclerview()
        setUpHourlyTimeListingRecyclerview()
        setUpAddPatientListingRecyclerview()
        setUpDoctorSloytListingRecyclerview()

        fragmentPhysiotherapyBookingAppiontmentBinding?.btnAppointmentBooking?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentPhysiotherapyBookingDetails.newInstance())
        })
    }
    // Set up recycler view for service listing if available
//    private fun setUpAddPatientListingRecyclerview() {
////        trainerList: ArrayList<TrainerListItem?>?
//        assert(fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareAddPatientList != null)
//        val recyclerView = fragmentNursesBookingAppointmentBinding!!.recyclerViewRootscareAddPatientList
//        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.setHasFixedSize(true)
////        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
////        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
//        val contactListAdapter = AdapterAddPatientListRecyclerview(context!!)
//        recyclerView.adapter = contactListAdapter
////        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
////            override fun onItemClick(id: Int) {
////                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
////                    FragmentAppiontmentDetails.newInstance())
////            }
////
////        }
//
//    }
    // Set up recycler view for service listing if available
    private fun setUpFromTimeListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareFromTimeRecyclerview != null)
        val recyclerView = fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareFromTimeRecyclerview
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
        assert(fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareToTimeRecyclerview != null)
        val recyclerView = fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareToTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterToTimeRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter

    }

    // Set up recycler view for service listing if available
    private fun setUpHourlyTimeListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareHourlyTimeRecyclerview != null)
        val recyclerView = fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareHourlyTimeRecyclerview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterSelectHourytimeRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter

    }


    // Set up recycler view for service listing if available
    private fun setUpAddPatientListingRecyclerview() {
        assert(fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareAddPatientList != null)
        val recyclerView = fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewRootscareAddPatientList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        val contactListAdapter = AdapterForNursesAddPatientListRecyclerview(context!!)
//        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewOnAddPatientListClick= object : OnAddPatientListClick {
//            override fun onItemClick(modelOfGetAddPatientList: ResultItem?) {
//                //  familymemberid= modelOfGetAddPatientList?.id!!
//            }
//
//
//        }

    }
    // Set up recycler view for service listing if available
    private fun setUpDoctorSloytListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewPhysiotherapyslot != null)
        val recyclerView = fragmentPhysiotherapyBookingAppiontmentBinding!!.recyclerViewPhysiotherapyslot
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterPhysiotherapySlotRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClick= object : OnDoctorPrivateSlotClickListner {
//            override fun onItemClick(modelData: com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.ResultItem?) {
//                clinicId= modelData?.clinicId!!
//                clinicFromTime= modelData?.timeFrom!!
//                clinicToTime= modelData?.timeTo!!
//            }
//
//
//        }

    }

}