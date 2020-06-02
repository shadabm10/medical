package com.rootscare.ui.nurses.nursescategorylisting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentNursesCategorylistingBinding
import com.rootscare.databinding.FragmentSeeAllNursesListByGridBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.FragmentBookingAppointment
import com.rootscare.ui.doctorcategorieslisting.adapter.AdapterDoctorCategoriesLisingRecyclerview
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.FragmentNursesListByGrid
import com.rootscare.ui.nurses.FragmentNursesListByGridNavigator
import com.rootscare.ui.nurses.FragmentNursesListByGridViewModel
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nursescategorylisting.adapter.AdapterNursesCtegoryListingRecyclerview
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetails

class FragmentNursesCategoryListing : BaseFragment<FragmentNursesCategorylistingBinding, FragmentNursesCategoryListingViewModel>(),
    FragmentNursesCategoryListingNavigator {
    private var fragmentNursesCategorylistingBinding: FragmentNursesCategorylistingBinding? = null
    private var fragmentNursesCategoryListingViewModel: FragmentNursesCategoryListingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_categorylisting
    override val viewModel: FragmentNursesCategoryListingViewModel
        get() {
            fragmentNursesCategoryListingViewModel =
                ViewModelProviders.of(this).get(FragmentNursesCategoryListingViewModel::class.java!!)
            return fragmentNursesCategoryListingViewModel as FragmentNursesCategoryListingViewModel
        }
    companion object {
        fun newInstance(): FragmentNursesCategoryListing {
            val args = Bundle()
            val fragment = FragmentNursesCategoryListing()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesCategoryListingViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesCategorylistingBinding = viewDataBinding
        setUpViewSeeAllNursesCategorieslistingRecyclerview()
    }

        // Set up recycler view for service listing if available
        private fun setUpViewSeeAllNursesCategorieslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
            assert(fragmentNursesCategorylistingBinding!!.recyclerViewRootscareNursescategorieslisting != null)
            val recyclerView = fragmentNursesCategorylistingBinding!!.recyclerViewRootscareNursescategorieslisting
            val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
            val contactListAdapter = AdapterNursesCtegoryListingRecyclerview(context!!)
            recyclerView.adapter = contactListAdapter
            contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {
                override fun onFirstItemClick(id: Int) {
                    (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                        FragmentNursesBookingAppointment.newInstance())
                }

                override fun onSecondItemClick(id: Int) {
                    (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                        FragmentNursesListingDetails.newInstance())
                }
//

            }


        }


    }