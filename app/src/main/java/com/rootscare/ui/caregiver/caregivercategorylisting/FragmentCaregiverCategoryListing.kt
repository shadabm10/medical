package com.rootscare.ui.caregiver.caregivercategorylisting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentCaregiverCategorylistingBinding
import com.rootscare.databinding.FragmentNursesCategorylistingBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.caregiver.bookingappointment.FragmentCaregiverBookingAppointment
import com.rootscare.ui.caregiver.caregivercategorylisting.adapter.AdapterCategoryListingRecyclerview
import com.rootscare.ui.caregiver.caregiverlistingdetails.FragmentCaregiverListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListingNavigator
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListingViewModel
import com.rootscare.ui.nurses.nursescategorylisting.adapter.AdapterNursesCtegoryListingRecyclerview
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetails

class FragmentCaregiverCategoryListing : BaseFragment<FragmentCaregiverCategorylistingBinding, FragmentCaregiverCategoryListingViewModel>(),
    FragmentCaregiverCategoryListingNavigator {
    private var fragmentCaregiverCategorylistingBinding: FragmentCaregiverCategorylistingBinding? = null
    private var fragmentCaregiverCategoryListingViewModel: FragmentCaregiverCategoryListingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_categorylisting
    override val viewModel: FragmentCaregiverCategoryListingViewModel
        get() {
            fragmentCaregiverCategoryListingViewModel =
                ViewModelProviders.of(this).get(FragmentCaregiverCategoryListingViewModel::class.java!!)
            return fragmentCaregiverCategoryListingViewModel as FragmentCaregiverCategoryListingViewModel
        }
    companion object {
        fun newInstance(): FragmentCaregiverCategoryListing {
            val args = Bundle()
            val fragment = FragmentCaregiverCategoryListing()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverCategoryListingViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverCategorylistingBinding = viewDataBinding
        setUpViewSeeAllNursesCategorieslistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllNursesCategorieslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverCategorylistingBinding!!.recyclerViewRootscareNursescategorieslisting != null)
        val recyclerView = fragmentCaregiverCategorylistingBinding!!.recyclerViewRootscareNursescategorieslisting
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCategoryListingRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {
            override fun onFirstItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentCaregiverBookingAppointment.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentCaregiverListingDetails.newInstance())
            }
//

        }


    }


}