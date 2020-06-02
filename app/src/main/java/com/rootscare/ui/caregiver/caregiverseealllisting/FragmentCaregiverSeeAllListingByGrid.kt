package com.rootscare.ui.caregiver.caregiverseealllisting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentSeeAllCaregiverListByGridBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.caregiver.caregivercategorylisting.FragmentCaregiverCategoryListing
import com.rootscare.ui.caregiver.caregiverseealllisting.adapter.AdapterCaregiverSeeAllListRecyclerview
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.FragmentNursesListByGrid
import com.rootscare.ui.nurses.FragmentNursesListByGridViewModel
import com.rootscare.ui.nurses.adapter.AdapterSeeAllNursesByGridRecyclerView
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing

class FragmentCaregiverSeeAllListingByGrid: BaseFragment<FragmentSeeAllCaregiverListByGridBinding, FragmentCaregiverSeeAllListingByGridViewModel>(),
    FragmentCaregiverSeeAllListingByGridNavigator {
    private var fragmentSeeAllCaregiverListByGridBinding: FragmentSeeAllCaregiverListByGridBinding? = null
    private var fragmentCaregiverSeeAllListingByGridViewModel: FragmentCaregiverSeeAllListingByGridViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_see_all_caregiver_list_by_grid
    override val viewModel: FragmentCaregiverSeeAllListingByGridViewModel
        get() {
            fragmentCaregiverSeeAllListingByGridViewModel =
                ViewModelProviders.of(this).get(FragmentCaregiverSeeAllListingByGridViewModel::class.java!!)
            return fragmentCaregiverSeeAllListingByGridViewModel as FragmentCaregiverSeeAllListingByGridViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverSeeAllListingByGrid {
            val args = Bundle()
            val fragment = FragmentCaregiverSeeAllListingByGrid()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverSeeAllListingByGridViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSeeAllCaregiverListByGridBinding = viewDataBinding
        setUpViewSeeAllNursesByGridlistingRecyclerview()
        fragmentSeeAllCaregiverListByGridBinding?.btnRootscareMoreNurses?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverCategoryListing.newInstance())
        })

    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllNursesByGridlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentSeeAllCaregiverListByGridBinding!!.recyclerViewRootscareSeeallnursesByGrid != null)
        val recyclerView = fragmentSeeAllCaregiverListByGridBinding!!.recyclerViewRootscareSeeallnursesByGrid
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverSeeAllListRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {

            override fun onFirstItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorProfile.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentNursesProfile.newInstance())
            }

        }


    }

}