package com.rootscare.ui.physiotherapy.physiotherapylistingdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentNursesListingDetailsBinding
import com.rootscare.databinding.FragmentPhysiotherapyListingDetailsBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetails
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetailsNavigator
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetailsViewModel
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.AdapterNursesFeesListingRecyclerView
import com.rootscare.ui.physiotherapy.submitreviewforservice.FragmentSubmitReview

class FragmentPhysiotherapyListingdetails : BaseFragment<FragmentPhysiotherapyListingDetailsBinding, FragmentPhysiotherapyListingdetailsViewModel>(),
    FragmentPhysiotherapyListingdetailsNavigator {
    private var fragmentPhysiotherapyListingDetailsBinding: FragmentPhysiotherapyListingDetailsBinding? = null
    private var fragmentPhysiotherapyListingdetailsViewModel: FragmentPhysiotherapyListingdetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physiotherapy_listing_details
    override val viewModel: FragmentPhysiotherapyListingdetailsViewModel
        get() {
            fragmentPhysiotherapyListingdetailsViewModel =
                ViewModelProviders.of(this).get(FragmentPhysiotherapyListingdetailsViewModel::class.java!!)
            return fragmentPhysiotherapyListingdetailsViewModel as FragmentPhysiotherapyListingdetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentPhysiotherapyListingdetails {
            val args = Bundle()
            val fragment = FragmentPhysiotherapyListingdetails  ()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPhysiotherapyListingdetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotherapyListingDetailsBinding = viewDataBinding
//        setUpViewSeeAllNursesCategorieslistingRecyclerview()
        setUpViewNursesFeeslistingRecyclerview()
        fragmentPhysiotherapyListingDetailsBinding?.btnRootscareBookingPhysiotherapy?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesBookingAppointment.newInstance())
        })

        fragmentPhysiotherapyListingDetailsBinding?.btnBookingAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesBookingAppointment.newInstance())
        })

        fragmentPhysiotherapyListingDetailsBinding?.txtWriteYourReview?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentSubmitReview.newInstance())
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpViewNursesFeeslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentPhysiotherapyListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing != null)
        val recyclerView = fragmentPhysiotherapyListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
//        val contactListAdapter = AdapterNursesFeesListingRecyclerView(context!!)
//        recyclerView.adapter = contactListAdapter


    }
}