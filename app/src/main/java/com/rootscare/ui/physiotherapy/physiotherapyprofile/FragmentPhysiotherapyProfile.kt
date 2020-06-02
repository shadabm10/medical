package com.rootscare.ui.physiotherapy.physiotherapyprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentNursesProfileBinding
import com.rootscare.databinding.FragmentPhysiotherapyProfileBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.nurses.nursesprofile.FragmentNursesProfile
import com.rootscare.ui.nurses.nursesprofile.FragmentNursesProfileNavigator
import com.rootscare.ui.nurses.nursesprofile.FragmentNursesProfileViewModel

class FragmentPhysiotherapyProfile : BaseFragment<FragmentPhysiotherapyProfileBinding, FragmentPhysiotherapyProfileViewModel>(),
    FragmentPhysiotherapyProfileNavigator {
    private var fragmentPhysiotherapyProfileBinding: FragmentPhysiotherapyProfileBinding? = null
    private var fragmentPhysiotherapyProfileViewModel: FragmentPhysiotherapyProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physiotherapy_profile
    override val viewModel: FragmentPhysiotherapyProfileViewModel
        get() {
            fragmentPhysiotherapyProfileViewModel =
                ViewModelProviders.of(this).get(FragmentPhysiotherapyProfileViewModel::class.java!!)
            return fragmentPhysiotherapyProfileViewModel as FragmentPhysiotherapyProfileViewModel
        }
    companion object {
        fun newInstance(): FragmentPhysiotherapyProfile {
            val args = Bundle()
            val fragment = FragmentPhysiotherapyProfile()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPhysiotherapyProfileViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotherapyProfileBinding = viewDataBinding
//        setUpViewSeeAllNursesByGridlistingRecyclerview()
//        fragmentSeeAllNursesListByGridBinding?.btnRootscareMoreNurses?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentNursesCategoryListing.newInstance())
//        })

    }
}