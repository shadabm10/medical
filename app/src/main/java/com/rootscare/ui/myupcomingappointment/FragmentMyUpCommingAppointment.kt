package com.rootscare.ui.myupcomingappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentCancellMyUpcomingAppointmentBinding
import com.rootscare.databinding.FragmentMyUpcommingAppointmentBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointment
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentNavigator
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointmentViewModel
import com.rootscare.ui.cancellappointment.adapter.AdapterCancelMyUpcomingAppiontment
import com.rootscare.ui.myupcomingappointment.adapter.AdapteMyUpComingAppointment

class FragmentMyUpCommingAppointment : BaseFragment<FragmentMyUpcommingAppointmentBinding, FragmentMyUpCommingAppointmentViewModel>(), FragmentMyUpCommingAppointmentnavigator {
    private var fragmentMyUpcommingAppointmentBinding: FragmentMyUpcommingAppointmentBinding? = null
    private var fragmentMyUpCommingAppointmentViewModel: FragmentMyUpCommingAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_my_upcomming_appointment
    override val viewModel: FragmentMyUpCommingAppointmentViewModel
        get() {
            fragmentMyUpCommingAppointmentViewModel =
                ViewModelProviders.of(this).get(FragmentMyUpCommingAppointmentViewModel::class.java!!)
            return fragmentMyUpCommingAppointmentViewModel as FragmentMyUpCommingAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentMyUpCommingAppointment {
            val args = Bundle()
            val fragment = FragmentMyUpCommingAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMyUpCommingAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMyUpcommingAppointmentBinding = viewDataBinding
        setUpAppointmentlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentMyUpcommingAppointmentBinding!!.recyclerViewRootscareMyupcommingappointmentlist != null)
        val recyclerView = fragmentMyUpcommingAppointmentBinding!!.recyclerViewRootscareMyupcommingappointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapteMyUpComingAppointment(context!!)
        recyclerView.adapter = contactListAdapter


    }
}
