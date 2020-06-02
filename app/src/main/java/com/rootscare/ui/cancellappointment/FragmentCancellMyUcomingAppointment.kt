package com.rootscare.ui.cancellappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentAppointmentBinding
import com.rootscare.databinding.FragmentCancellMyUpcomingAppointmentBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.appointment.FragmentAppointment
import com.rootscare.ui.appointment.FragmentAppointmentNavigator
import com.rootscare.ui.appointment.FragmentAppointmentViewModel
import com.rootscare.ui.appointment.adapter.AdapterAppointmentListRecyclerView
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetails
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.cancellappointment.adapter.AdapterCancelMyUpcomingAppiontment
import com.rootscare.ui.home.HomeActivity

class FragmentCancellMyUcomingAppointment : BaseFragment<FragmentCancellMyUpcomingAppointmentBinding, FragmentCancellMyUcomingAppointmentViewModel>(),
    FragmentCancellMyUcomingAppointmentNavigator {

    private var fragmentCancellMyUpcomingAppointmentBinding: FragmentCancellMyUpcomingAppointmentBinding? = null
    private var fragmentCancellMyUcomingAppointmentViewModel: FragmentCancellMyUcomingAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_cancell_my_upcoming_appointment
    override val viewModel: FragmentCancellMyUcomingAppointmentViewModel
        get() {
            fragmentCancellMyUcomingAppointmentViewModel =
                ViewModelProviders.of(this).get(FragmentCancellMyUcomingAppointmentViewModel::class.java!!)
            return fragmentCancellMyUcomingAppointmentViewModel as FragmentCancellMyUcomingAppointmentViewModel
        }
    companion object {
        fun newInstance(): FragmentCancellMyUcomingAppointment {
            val args = Bundle()
            val fragment = FragmentCancellMyUcomingAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCancellMyUcomingAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCancellMyUpcomingAppointmentBinding = viewDataBinding
        setUpAppointmentlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCancellMyUpcomingAppointmentBinding!!.recyclerViewRootscareCancelappointmentlist != null)
        val recyclerView = fragmentCancellMyUpcomingAppointmentBinding!!.recyclerViewRootscareCancelappointmentlist
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCancelMyUpcomingAppiontment(context!!)
        recyclerView.adapter = contactListAdapter


    }
}