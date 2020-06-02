package com.rootscare.ui.physiotherapy

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentSeeAllNursesListByGridBinding
import com.rootscare.databinding.FragmentSeeAllPhysiotherapyListingBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.FragmentNursesListByGrid
import com.rootscare.ui.nurses.FragmentNursesListByGridNavigator
import com.rootscare.ui.nurses.FragmentNursesListByGridViewModel
import com.rootscare.ui.nurses.adapter.AdapterSeeAllNursesByGridRecyclerView
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing
import com.rootscare.ui.nurses.nursesprofile.FragmentNursesProfile
import com.rootscare.ui.physiotherapy.adapter.AdapterPhysiotherapyListRecyclerView
import com.rootscare.ui.physiotherapy.physiotherapycategorylisting.FragmentPhysiotherapyCategoryListing
import com.rootscare.ui.physiotherapy.physiotherapyprofile.FragmentPhysiotherapyProfile

class FragmentSeeAllPhysiotherapyListing: BaseFragment<FragmentSeeAllPhysiotherapyListingBinding, FragmentSeeAllPhysiotherapyListingViewModel>(),
    FragmentSeeAllPhysiotherapyListingNavigator {
    private var fragmentSeeAllPhysiotherapyListingBinding: FragmentSeeAllPhysiotherapyListingBinding? = null
    private var fragmentSeeAllPhysiotherapyListingViewModel: FragmentSeeAllPhysiotherapyListingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_see_all_physiotherapy_listing
    override val viewModel: FragmentSeeAllPhysiotherapyListingViewModel
        get() {
            fragmentSeeAllPhysiotherapyListingViewModel =
                ViewModelProviders.of(this).get(FragmentSeeAllPhysiotherapyListingViewModel::class.java!!)
            return fragmentSeeAllPhysiotherapyListingViewModel as FragmentSeeAllPhysiotherapyListingViewModel
        }
    companion object {
        fun newInstance(): FragmentSeeAllPhysiotherapyListing {
            val args = Bundle()
            val fragment = FragmentSeeAllPhysiotherapyListing()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentSeeAllPhysiotherapyListingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSeeAllPhysiotherapyListingBinding = viewDataBinding
        setUpViewSeeAllPhysiotherapyByGridlistingRecyclerview()
        fragmentSeeAllPhysiotherapyListingBinding?.btnRootscareMorePhysiotherapy?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentPhysiotherapyCategoryListing.newInstance())
        })

    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllPhysiotherapyByGridlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentSeeAllPhysiotherapyListingBinding!!.recyclerViewRootscareSeeallphysiotherapyByGrid != null)
        val recyclerView = fragmentSeeAllPhysiotherapyListingBinding!!.recyclerViewRootscareSeeallphysiotherapyByGrid
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterPhysiotherapyListRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {

            override fun onFirstItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorProfile.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentPhysiotherapyProfile.newInstance())
            }

        }


    }

}