package com.rootscare.ui.hospital.hospitalcategorylisting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentDoctorCategoriesListingBinding
import com.rootscare.databinding.FragmentHospitalCategoryListBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.FragmentBookingAppointment
import com.rootscare.ui.doctorcategorieslisting.FragmentDoctorCategoriesListing
import com.rootscare.ui.doctorcategorieslisting.FragmentDoctorCategoriesListingNavigator
import com.rootscare.ui.doctorcategorieslisting.FragmentDoctorCategoriesListingViewModel
import com.rootscare.ui.doctorcategorieslisting.adapter.AdapterDoctorCategoriesLisingRecyclerview
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.hospital.hospitalbooking.FragmentHospitalBooking
import com.rootscare.ui.hospital.hospitalcategorylisting.adapter.AdapterHospitalCategoryListingRecyclerView
import com.rootscare.ui.hospital.hospitallistdetails.FragmentHospitalListDetails

class FragmentHospitalCategoryList : BaseFragment<FragmentHospitalCategoryListBinding, FragmentHospitalCategoryListViewModel>(),
    FragmentHospitalCategoryListNavigator {
    private var fragmentHospitalCategoryListBinding: FragmentHospitalCategoryListBinding? = null
    private var fragmentHospitalCategoryListViewModel: FragmentHospitalCategoryListViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_category_list
    override val viewModel: FragmentHospitalCategoryListViewModel
        get() {
            fragmentHospitalCategoryListViewModel =
                ViewModelProviders.of(this).get(FragmentHospitalCategoryListViewModel::class.java!!)
            return fragmentHospitalCategoryListViewModel as FragmentHospitalCategoryListViewModel
        }
    companion object {
        fun newInstance(): FragmentHospitalCategoryList {
            val args = Bundle()
            val fragment = FragmentHospitalCategoryList()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalCategoryListViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalCategoryListBinding = viewDataBinding
        setUpViewSeeAllDoctorCategorieslistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllDoctorCategorieslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalCategoryListBinding!!.recyclerViewRootscareHospitalcategorieslisting != null)
        val recyclerView = fragmentHospitalCategoryListBinding!!.recyclerViewRootscareHospitalcategorieslisting
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalCategoryListingRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {
            override fun onFirstItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentHospitalBooking.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentHospitalListDetails.newInstance())
            }
//

        }


    }


}