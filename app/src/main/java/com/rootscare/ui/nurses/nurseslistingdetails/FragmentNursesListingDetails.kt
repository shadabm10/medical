package com.rootscare.ui.nurses.nurseslistingdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentNursesCategorylistingBinding
import com.rootscare.databinding.FragmentNursesListingDetailsBinding
import com.rootscare.databinding.FragmentSeeAllNursesListByGridBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.FragmentNursesListByGridNavigator
import com.rootscare.ui.nurses.FragmentNursesListByGridViewModel
import com.rootscare.ui.nurses.adapter.AdapterSeeAllNursesByGridRecyclerView
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListingViewModel
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.AdapterNursesFeesListingRecyclerView
import com.rootscare.ui.physiotherapy.submitreviewforservice.FragmentSubmitReview

class FragmentNursesListingDetails : BaseFragment<FragmentNursesListingDetailsBinding, FragmentNursesListingDetailsViewModel>(),
    FragmentNursesListingDetailsNavigator {
    private var fragmentNursesListingDetailsBinding: FragmentNursesListingDetailsBinding? = null
    private var fragmentNursesListingDetailsViewModel: FragmentNursesListingDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_listing_details
    override val viewModel: FragmentNursesListingDetailsViewModel
        get() {
            fragmentNursesListingDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentNursesListingDetailsViewModel::class.java!!)
            return fragmentNursesListingDetailsViewModel as FragmentNursesListingDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentNursesListingDetails {
            val args = Bundle()
            val fragment = FragmentNursesListingDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesListingDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesListingDetailsBinding = viewDataBinding
//        setUpViewSeeAllNursesCategorieslistingRecyclerview()
        setUpViewNursesFeeslistingRecyclerview()
        fragmentNursesListingDetailsBinding?.btnRootscareBookingNurses?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesBookingAppointment.newInstance())
        })

        fragmentNursesListingDetailsBinding?.btnBookingAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesBookingAppointment.newInstance())
        })


        fragmentNursesListingDetailsBinding?.txtWriteYourReview?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentSubmitReview.newInstance())
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpViewNursesFeeslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNursesFeesListingRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter

    }
}