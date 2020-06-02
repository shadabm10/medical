package com.rootscare.ui.nurses.nursesprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentNursesProfileBinding
import com.rootscare.ui.base.BaseFragment

class FragmentNursesProfile: BaseFragment<FragmentNursesProfileBinding, FragmentNursesProfileViewModel>(),
    FragmentNursesProfileNavigator {
    private var fragmentNursesProfileBinding: FragmentNursesProfileBinding? = null
    private var fragmentNursesProfileViewModel: FragmentNursesProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_profile
    override val viewModel: FragmentNursesProfileViewModel
        get() {
            fragmentNursesProfileViewModel =
                ViewModelProviders.of(this).get(FragmentNursesProfileViewModel::class.java!!)
            return fragmentNursesProfileViewModel as FragmentNursesProfileViewModel
        }
    companion object {
        fun newInstance(): FragmentNursesProfile {
            val args = Bundle()
            val fragment = FragmentNursesProfile()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesProfileViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesProfileBinding = viewDataBinding
//        setUpViewSeeAllNursesByGridlistingRecyclerview()
//        fragmentSeeAllNursesListByGridBinding?.btnRootscareMoreNurses?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentNursesCategoryListing.newInstance())
//        })

    }
}