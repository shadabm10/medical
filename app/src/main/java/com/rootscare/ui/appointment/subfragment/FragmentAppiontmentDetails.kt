package com.rootscare.ui.appointment.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentAppiontmentDetailsBinding
import com.rootscare.databinding.FragmentAppointmentBinding
import com.rootscare.ui.appointment.FragmentAppointment
import com.rootscare.ui.appointment.FragmentAppointmentNavigator
import com.rootscare.ui.appointment.FragmentAppointmentViewModel
import com.rootscare.ui.base.BaseFragment

class FragmentAppiontmentDetails  : BaseFragment<FragmentAppiontmentDetailsBinding, FragmentAppiontmentDetailsViewModel>(),
    FragmentAppiontmentDetailsNavigator {
    private var fragmentAppiontmentDetailsBinding: FragmentAppiontmentDetailsBinding? = null
    private var fragmentAppiontmentDetailsViewModel: FragmentAppiontmentDetailsViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_appiontment_details
    override val viewModel: FragmentAppiontmentDetailsViewModel
        get() {
            fragmentAppiontmentDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentAppiontmentDetailsViewModel::class.java!!)
            return fragmentAppiontmentDetailsViewModel as FragmentAppiontmentDetailsViewModel
        }

    companion object {
        fun newInstance(): FragmentAppiontmentDetails {
            val args = Bundle()
            val fragment = FragmentAppiontmentDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAppiontmentDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAppiontmentDetailsBinding = viewDataBinding
    }
}