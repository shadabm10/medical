package com.rootscare.ui.nurses

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentSeeAllNursesListByGridBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.adapter.AdapterSeeAllNursesByGridRecyclerView
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing

class FragmentNursesListByGrid : BaseFragment<FragmentSeeAllNursesListByGridBinding, FragmentNursesListByGridViewModel>(),
    FragmentNursesListByGridNavigator {
        private var fragmentSeeAllNursesListByGridBinding: FragmentSeeAllNursesListByGridBinding? = null
        private var fragmentNursesListByGridViewModel: FragmentNursesListByGridViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_see_all_nurses_list_by_grid
    override val viewModel: FragmentNursesListByGridViewModel
        get() {
            fragmentNursesListByGridViewModel =
                ViewModelProviders.of(this).get(FragmentNursesListByGridViewModel::class.java!!)
            return fragmentNursesListByGridViewModel as FragmentNursesListByGridViewModel
        }
    companion object {
        fun newInstance(): FragmentNursesListByGrid {
            val args = Bundle()
            val fragment = FragmentNursesListByGrid()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesListByGridViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSeeAllNursesListByGridBinding = viewDataBinding
        setUpViewSeeAllNursesByGridlistingRecyclerview()
        fragmentSeeAllNursesListByGridBinding?.btnRootscareMoreNurses?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesCategoryListing.newInstance())
        })

    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllNursesByGridlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentSeeAllNursesListByGridBinding!!.recyclerViewRootscareSeeallnursesByGrid != null)
        val recyclerView = fragmentSeeAllNursesListByGridBinding!!.recyclerViewRootscareSeeallnursesByGrid
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterSeeAllNursesByGridRecyclerView(context!!)
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