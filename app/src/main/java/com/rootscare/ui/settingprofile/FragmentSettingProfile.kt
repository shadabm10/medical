package com.rootscare.ui.settingprofile

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.response.deactivateaccountresponse.DeactivateAccountResponse
import com.rootscare.databinding.FragmentSettingProfileBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.profile.FragmentProfile
import com.rootscare.ui.viewprescription.FragmnetViewPresprictionViewModel

class FragmentSettingProfile : BaseFragment<FragmentSettingProfileBinding, FragmentSettingProfileViewModel>(),
    FragmentSettingProfileNavigator {
    private var fragmentSettingProfileBinding: FragmentSettingProfileBinding? = null
    private var fragmentSettingProfileViewModel: FragmentSettingProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_patient_profile_setting
    override val viewModel: FragmentSettingProfileViewModel
        get() {
            fragmentSettingProfileViewModel =
                ViewModelProviders.of(this).get(FragmentSettingProfileViewModel::class.java!!)
            return fragmentSettingProfileViewModel as FragmentSettingProfileViewModel
        }

}