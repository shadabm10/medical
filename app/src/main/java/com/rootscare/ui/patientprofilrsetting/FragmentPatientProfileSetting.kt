package com.rootscare.ui.patientprofilrsetting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentPatientBookingPaynowBinding
import com.rootscare.databinding.FragmentPatientProfileSettingBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.patientbookpaynow.FragmentPatientbookPayNow
import com.rootscare.ui.patientbookpaynow.FragmentPatientbookPayNowNavigator
import com.rootscare.ui.patientbookpaynow.FragmentPatientbookPayNowViewModel
import com.rootscare.ui.profile.FragmentProfile

class FragmentPatientProfileSetting  : BaseFragment<FragmentPatientProfileSettingBinding, FragmentPatientProfileSettingViewModel>(), FragmentPatientProfileSettingNavigator {
    private var fragmentPatientProfileSettingBinding: FragmentPatientProfileSettingBinding? = null
    private var fragmentPatientProfileSettingViewModel: FragmentPatientProfileSettingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_patient_profile_setting
    override val viewModel: FragmentPatientProfileSettingViewModel
        get() {
            fragmentPatientProfileSettingViewModel = ViewModelProviders.of(this).get(FragmentPatientProfileSettingViewModel::class.java!!)
            return fragmentPatientProfileSettingViewModel as FragmentPatientProfileSettingViewModel
        }
    companion object {
        fun newInstance(): FragmentPatientProfileSetting {
            val args = Bundle()
            val fragment = FragmentPatientProfileSetting()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPatientProfileSettingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPatientProfileSettingBinding = viewDataBinding
        // setUpAppointmentlistingRecyclerview()

        fragmentPatientProfileSettingBinding?.imgEditProfilr?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentProfile.newInstance())

        })
        fragmentPatientProfileSettingBinding?.txtPatientsettingProfile?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentProfile.newInstance())

        })
    }
}