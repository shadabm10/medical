package com.rootscare.ui.doctorprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentDoctorCategoriesListingBinding
import com.rootscare.databinding.FragmentDoctorListingDetailsBinding
import com.rootscare.databinding.FragmentDoctorProfileBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.doctorcategorieslisting.FragmentDoctorCategoriesListingViewModel
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetailsNavigator
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetailsViewModel

class FragmentDoctorProfile : BaseFragment<FragmentDoctorProfileBinding, FragmentDoctorProfileViewModel>(),
    FragmentDoctorProfileNavigator {
    private var fragmentDoctorProfileBinding: FragmentDoctorProfileBinding? = null
    private var fragmentDoctorProfileViewModel: FragmentDoctorProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_profile
    override val viewModel: FragmentDoctorProfileViewModel
        get() {
            fragmentDoctorProfileViewModel =
                ViewModelProviders.of(this).get(FragmentDoctorProfileViewModel::class.java!!)
            return fragmentDoctorProfileViewModel as FragmentDoctorProfileViewModel
        }
    companion object {
        fun newInstance(): FragmentDoctorProfile {
            val args = Bundle()
            val fragment = FragmentDoctorProfile()
            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorProfileViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorProfileBinding = viewDataBinding
    }
}