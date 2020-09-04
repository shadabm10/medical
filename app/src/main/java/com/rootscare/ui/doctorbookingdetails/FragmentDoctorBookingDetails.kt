package com.rootscare.ui.doctorbookingdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentCancellMyUpcomingAppointmentBinding
import com.rootscare.databinding.FragmentDoctorBookingDetailsBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingcart.FragmentBookingCart
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointment
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentNavigator
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentViewModel
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.login.LoginActivity
import com.rootscare.ui.patientbookpaynow.FragmentPatientbookPayNow

class FragmentDoctorBookingDetails  : BaseFragment<FragmentDoctorBookingDetailsBinding, FragmentDoctorBookingDetailsViewModel>(),
    FragmentDoctorBookingDetailsNavigator {
    private var fragmentDoctorBookingDetailsBinding: FragmentDoctorBookingDetailsBinding? = null
    private var fragmentDoctorBookingDetailsViewModel: FragmentDoctorBookingDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_booking_details
    override val viewModel: FragmentDoctorBookingDetailsViewModel
        get() {
            fragmentDoctorBookingDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentDoctorBookingDetailsViewModel::class.java!!)
            return fragmentDoctorBookingDetailsViewModel as FragmentDoctorBookingDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentDoctorBookingDetails {
            val args = Bundle()
            val fragment = FragmentDoctorBookingDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorBookingDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorBookingDetailsBinding = viewDataBinding
        fragmentDoctorBookingDetailsBinding?.btnBookingConfirm?.setOnClickListener(View.OnClickListener {

            CommonDialog.showDialog(this.activity!!, object :
                DialogClickCallback {
                override fun onDismiss() {
                }

                override fun onConfirm() {
                    (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentBookingCart.newInstance())

                }

            }, "Comfirm Appointment", "Are you sure for this doctor booking ?")

        })
    }
}