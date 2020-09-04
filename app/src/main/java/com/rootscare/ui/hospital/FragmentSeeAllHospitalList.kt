package com.rootscare.ui.hospital

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentSeeAllHospitalListBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.hospital.adapter.AdapterSeeAllHospitalList
import com.rootscare.ui.hospital.hospitalcategorylisting.FragmentHospitalCategoryList


class FragmentSeeAllHospitalList : BaseFragment<FragmentSeeAllHospitalListBinding, FragmentSeeAllHospitalListViewModel>(),
    FragmentSeeAllHospitalListNavigator {
    private var fragmentSeeAllHospitalListBinding: FragmentSeeAllHospitalListBinding? = null
    private var fragmentSeeAllHospitalListViewModel: FragmentSeeAllHospitalListViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_see_all_hospital_list
    override val viewModel: FragmentSeeAllHospitalListViewModel
        get() {
            fragmentSeeAllHospitalListViewModel =
                ViewModelProviders.of(this).get(FragmentSeeAllHospitalListViewModel::class.java!!)
            return fragmentSeeAllHospitalListViewModel as FragmentSeeAllHospitalListViewModel
        }

    companion object {
        fun newInstance(): FragmentSeeAllHospitalList {
            val args = Bundle()
            val fragment = FragmentSeeAllHospitalList()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentSeeAllHospitalListViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSeeAllHospitalListBinding = viewDataBinding
        setUpViewSeeAllHospitallistingRecyclerview()
        fragmentSeeAllHospitalListBinding?.btnRootscareNearbyHospital?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalCategoryList.newInstance())
        })

    }
    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllHospitallistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentSeeAllHospitalListBinding!!.recyclerViewRootscareSeeallhospitalList != null)
        val recyclerView = fragmentSeeAllHospitalListBinding!!.recyclerViewRootscareSeeallhospitalList
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterSeeAllHospitalList(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {
//
//            override fun onFirstItemClick(id: Int) {
////                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
////                    FragmentDoctorProfile.newInstance())
//            }
//
//            override fun onSecondItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentNursesProfile.newInstance())
//            }
//
//        }


    }

}