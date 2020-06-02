package com.rootscare.ui.caregiver.caregiverlistingdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentCaregiverListingDetailsBinding
import com.rootscare.databinding.FragmentNursesListingDetailsBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.caregiver.bookingappointment.FragmentCaregiverBookingAppointment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetails
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetailsNavigator
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetailsViewModel
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.AdapterNursesFeesListingRecyclerView
import com.rootscare.ui.physiotherapy.submitreviewforservice.FragmentSubmitReview

class FragmentCaregiverListingDetails : BaseFragment<FragmentCaregiverListingDetailsBinding, FragmentCaregiverListingDetailsViewModel>(),
    FragmentCaregiverListingDetailsnavigator {
    private var fragmentCaregiverListingDetailsBinding: FragmentCaregiverListingDetailsBinding? = null
    private var fragmentCaregiverListingDetailsViewModel: FragmentCaregiverListingDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_listing_details
    override val viewModel: FragmentCaregiverListingDetailsViewModel
        get() {
            fragmentCaregiverListingDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentCaregiverListingDetailsViewModel::class.java!!)
            return fragmentCaregiverListingDetailsViewModel as FragmentCaregiverListingDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentCaregiverListingDetails {
            val args = Bundle()
            val fragment = FragmentCaregiverListingDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverListingDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverListingDetailsBinding = viewDataBinding
//        setUpViewSeeAllNursesCategorieslistingRecyclerview()
        setUpViewNursesFeeslistingRecyclerview()
        fragmentCaregiverListingDetailsBinding?.btnRootscareBookingNurses?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverBookingAppointment.newInstance())
        })
        fragmentCaregiverListingDetailsBinding?.btnBookingAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverBookingAppointment.newInstance())
        })
        fragmentCaregiverListingDetailsBinding?.txtWriteYourReview?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentSubmitReview.newInstance())
        })

    }

    // Set up recycler view for service listing if available
    private fun setUpViewNursesFeeslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing != null)
        val recyclerView = fragmentCaregiverListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNursesFeesListingRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter

    }
}