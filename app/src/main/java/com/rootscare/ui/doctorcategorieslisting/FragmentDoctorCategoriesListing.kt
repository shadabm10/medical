package com.rootscare.ui.doctorcategorieslisting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentCancellMyUpcomingAppointmentBinding
import com.rootscare.databinding.FragmentDoctorCategoriesListingBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetails
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.FragmentBookingAppointment
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointment
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentNavigator
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentViewModel
import com.rootscare.ui.doctorcategorieslisting.adapter.AdapterDoctorCategoriesLisingRecyclerview
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.seealldoctorbygrid.adapter.AdapterSeeAllDoctorByGridRecyclerView

class FragmentDoctorCategoriesListing : BaseFragment<FragmentDoctorCategoriesListingBinding, FragmentDoctorCategoriesListingViewModel>(),
    FragmentDoctorCategoriesListingNavigator {
        private var fragmentDoctorCategoriesListingBinding: FragmentDoctorCategoriesListingBinding? = null
        private var fragmentDoctorCategoriesListingViewModel: FragmentDoctorCategoriesListingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_categories_listing
    override val viewModel: FragmentDoctorCategoriesListingViewModel
        get() {
            fragmentDoctorCategoriesListingViewModel =
                ViewModelProviders.of(this).get(FragmentDoctorCategoriesListingViewModel::class.java!!)
            return fragmentDoctorCategoriesListingViewModel as FragmentDoctorCategoriesListingViewModel
        }

    companion object {
        fun newInstance(): FragmentDoctorCategoriesListing {
            val args = Bundle()
            val fragment = FragmentDoctorCategoriesListing()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorCategoriesListingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorCategoriesListingBinding = viewDataBinding
        setUpViewSeeAllDoctorCategorieslistingRecyclerview()
    }
    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllDoctorCategorieslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorCategoriesListingBinding!!.recyclerViewRootscareDoctorcategorieslisting != null)
        val recyclerView = fragmentDoctorCategoriesListingBinding!!.recyclerViewRootscareDoctorcategorieslisting
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterDoctorCategoriesLisingRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {
            override fun onFirstItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentBookingAppointment.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorListingDetails.newInstance(id.toString()))
            }
//

        }


    }


}