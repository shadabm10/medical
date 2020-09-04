package com.rootscare.ui.notification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentNotificationBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.notification.adapter.AdapterNotificationListRecyclerview

class FragmentNotification : BaseFragment<FragmentNotificationBinding, FragmentNotificationViewModel>(), FragmentNotificationNavigator{
    private var fragmentNotificationBinding: FragmentNotificationBinding? = null
    private var fragmentNotificationViewModel: FragmentNotificationViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_notification
    override val viewModel: FragmentNotificationViewModel
        get() {
            fragmentNotificationViewModel =
                ViewModelProviders.of(this).get(FragmentNotificationViewModel::class.java!!)
            return fragmentNotificationViewModel as FragmentNotificationViewModel
        }

    companion object {
        fun newInstance(): FragmentNotification {
            val args = Bundle()
            val fragment = FragmentNotification()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNotificationViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNotificationBinding = viewDataBinding
        setUpNotificationRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpNotificationRecyclerview() {
        assert(fragmentNotificationBinding!!.recyclerViewRootscareNotification!= null)
        val recyclerView =fragmentNotificationBinding!!.recyclerViewRootscareNotification
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterNotificationListRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }


}