package com.rootscare.ui.settingprofile

import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentSettingProfileBinding
import com.rootscare.ui.base.BaseFragment

class FragmentSettingProfile : BaseFragment<FragmentSettingProfileBinding, FragmentSettingProfileViewModel>(),
    FragmentSettingProfileNavigator {
    private var fragmentSettingProfileBinding: FragmentSettingProfileBinding? = null
    private var fragmentSettingProfileViewModel: FragmentSettingProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_patient_profile_setting
    override val viewModel: FragmentSettingProfileViewModel
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}