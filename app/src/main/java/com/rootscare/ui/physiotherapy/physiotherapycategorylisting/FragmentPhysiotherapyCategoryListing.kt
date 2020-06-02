package com.rootscare.ui.physiotherapy.physiotherapycategorylisting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentNursesCategorylistingBinding
import com.rootscare.databinding.FragmentPhysiotherapyCategoryListingBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListingNavigator
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListingViewModel
import com.rootscare.ui.nurses.nursescategorylisting.adapter.AdapterNursesCtegoryListingRecyclerview
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetails
import com.rootscare.ui.physiotherapy.bookingappointment.FragmentPhysiotherapyBookingAppointment
import com.rootscare.ui.physiotherapy.physiotherapycategorylisting.adapter.AdapterPhysiotherapyCategoryLisytingRecyclerview
import com.rootscare.ui.physiotherapy.physiotherapylistingdetails.FragmentPhysiotherapyListingdetails

class FragmentPhysiotherapyCategoryListing : BaseFragment<FragmentPhysiotherapyCategoryListingBinding, FragmentPhysiotherapyCategoryListingViewModel>(),
    FragmentPhysiotherapyCategoryListingNavigator {
    private var fragmentPhysiotherapyCategoryListingBinding: FragmentPhysiotherapyCategoryListingBinding? = null
    private var fragmentPhysiotherapyCategoryListingViewModel: FragmentPhysiotherapyCategoryListingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physiotherapy_category_listing
    override val viewModel: FragmentPhysiotherapyCategoryListingViewModel
        get() {
            fragmentPhysiotherapyCategoryListingViewModel =
                ViewModelProviders.of(this).get(FragmentPhysiotherapyCategoryListingViewModel::class.java!!)
            return fragmentPhysiotherapyCategoryListingViewModel as FragmentPhysiotherapyCategoryListingViewModel
        }

    companion object {
        fun newInstance(): FragmentPhysiotherapyCategoryListing {
            val args = Bundle()
            val fragment = FragmentPhysiotherapyCategoryListing()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPhysiotherapyCategoryListingViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotherapyCategoryListingBinding = viewDataBinding
        setUpViewSeeAllNursesCategorieslistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllNursesCategorieslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentPhysiotherapyCategoryListingBinding!!.recyclerViewRootscareNursescategorieslisting != null)
        val recyclerView = fragmentPhysiotherapyCategoryListingBinding!!.recyclerViewRootscareNursescategorieslisting
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterPhysiotherapyCategoryLisytingRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {
            override fun onFirstItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentPhysiotherapyBookingAppointment.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentPhysiotherapyListingdetails.newInstance())
            }
//

        }


    }


}
