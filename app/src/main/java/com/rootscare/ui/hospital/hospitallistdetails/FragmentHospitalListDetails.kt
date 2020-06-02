package com.rootscare.ui.hospital.hospitallistdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentHospitalListDetailsBinding
import com.rootscare.databinding.FragmentSeeAllHospitalListBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.adapter.AdapterAddPatientListRecyclerview
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.hospital.FragmentSeeAllHospitalList
import com.rootscare.ui.hospital.FragmentSeeAllHospitalListNavigator
import com.rootscare.ui.hospital.FragmentSeeAllHospitalListViewModel
import com.rootscare.ui.hospital.hospitalbooking.FragmentHospitalBooking
import com.rootscare.ui.hospital.hospitalcategorylisting.FragmentHospitalCategoryList
import com.rootscare.ui.hospital.hospitallistdetails.adapter.AdapterHospitalPhotoRecyclerView
import com.rootscare.ui.hospital.hospitallistdetails.adapter.AdapterHospitalServiceListingRecyclerview
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.physiotherapy.submitreviewforservice.FragmentSubmitReview

class FragmentHospitalListDetails: BaseFragment<FragmentHospitalListDetailsBinding, FragmentHospitalListDetailsViewModel>(),
    FragmentHospitalListDetailsNavigator {
    private var fragmentHospitalListDetailsBinding: FragmentHospitalListDetailsBinding? = null
    private var fragmentHospitalListDetailsViewModel: FragmentHospitalListDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_list_details
    override val viewModel: FragmentHospitalListDetailsViewModel
        get() {
            fragmentHospitalListDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentHospitalListDetailsViewModel::class.java!!)
            return fragmentHospitalListDetailsViewModel as FragmentHospitalListDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentHospitalListDetails {
            val args = Bundle()
            val fragment = FragmentHospitalListDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalListDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalListDetailsBinding = viewDataBinding
        setUpHospitalImageListingRecyclerview()
        setUpHospitalServiceListingRecyclerview()
//        fragmentSeeAllHospitalListBinding?.btnRootscareMoreHospita?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentHospitalCategoryList.newInstance())
//        })

        fragmentHospitalListDetailsBinding?.btnHospitalBooking?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalBooking.newInstance())
        })

        fragmentHospitalListDetailsBinding?.txtWriteYourReview?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentSubmitReview.newInstance())
        })

    }
    // Set up recycler view for service listing if available
    private fun setUpHospitalImageListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalListDetailsBinding!!.recyclerViewRootscareHospitalImage != null)
        val recyclerView = fragmentHospitalListDetailsBinding!!.recyclerViewRootscareHospitalImage
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalPhotoRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentAppiontmentDetails.newInstance())
//            }
//
//        }

    }


    // Set up recycler view for service listing if available
    private fun setUpHospitalServiceListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalListDetailsBinding!!.recyclerViewRootscareHospitalService != null)
        val recyclerView = fragmentHospitalListDetailsBinding!!.recyclerViewRootscareHospitalService
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalServiceListingRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentAppiontmentDetails.newInstance())
//            }
//
//        }

    }

}