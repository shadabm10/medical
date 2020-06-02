package com.rootscare.ui.hospital.hospitalbookingdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentHospitalBookingDetailsBinding
import com.rootscare.databinding.FragmentNursesAppointmentBookingDetailsBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.nurses.nursesappointmentbookingdetails.FragmentNursesAppointmentBookingDetails
import com.rootscare.ui.nurses.nursesappointmentbookingdetails.FragmentNursesAppointmentBookingDetailsNavigator
import com.rootscare.ui.nurses.nursesappointmentbookingdetails.FragmentNursesAppointmentBookingDetailsViewModel

class FragmentHospitalBookingDetails: BaseFragment<FragmentHospitalBookingDetailsBinding, FragmentHospitalBookingDetailsViewModel>(),
    FragmentHospitalBookingDetailsNavigator {
    private var fragmentHospitalBookingDetailsBinding: FragmentHospitalBookingDetailsBinding? = null
    private var fragmentHospitalBookingDetailsViewModel: FragmentHospitalBookingDetailsViewModel?=null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_booking_details
    override val viewModel: FragmentHospitalBookingDetailsViewModel
        get() {
            fragmentHospitalBookingDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentHospitalBookingDetailsViewModel::class.java!!)
            return fragmentHospitalBookingDetailsViewModel as FragmentHospitalBookingDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentHospitalBookingDetails {
            val args = Bundle()
            val fragment = FragmentHospitalBookingDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalBookingDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalBookingDetailsBinding = viewDataBinding
//        fragmentNursesAppointmentBookingDetailsBinding?.btnRootscareBookingDoctor?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentBookingAppointment.newInstance())
//        })
    }

}