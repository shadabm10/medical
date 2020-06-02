package com.rootscare.ui.viewprescription

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentMyUpcommingAppointmentBinding
import com.rootscare.databinding.FragmentViewPrescriptionBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.myupcomingappointment.FragmentMyUpCommingAppointment
import com.rootscare.ui.myupcomingappointment.FragmentMyUpCommingAppointmentViewModel
import com.rootscare.ui.myupcomingappointment.FragmentMyUpCommingAppointmentnavigator
import com.rootscare.ui.myupcomingappointment.adapter.AdapteMyUpComingAppointment
import com.rootscare.ui.viewprescription.adapter.AdapterViewPrescriptionRecyclerview

class FragmnetViewPrespriction : BaseFragment<FragmentViewPrescriptionBinding, FragmnetViewPresprictionViewModel>(), FragmnetViewPresprictionNavigator {
    private var fragmentViewPrescriptionBinding: FragmentViewPrescriptionBinding? = null
    private var fragmnetViewPresprictionViewModel: FragmnetViewPresprictionViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_view_prescription
    override val viewModel: FragmnetViewPresprictionViewModel
        get() {
            fragmnetViewPresprictionViewModel =
                ViewModelProviders.of(this).get(FragmnetViewPresprictionViewModel::class.java!!)
            return fragmnetViewPresprictionViewModel as FragmnetViewPresprictionViewModel
        }

    companion object {
        fun newInstance(): FragmnetViewPrespriction {
            val args = Bundle()
            val fragment = FragmnetViewPrespriction()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmnetViewPresprictionViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewPrescriptionBinding = viewDataBinding
        setUpViewPrescriptionlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentViewPrescriptionBinding!!.recyclerViewRootscareViewprescription != null)
        val recyclerView = fragmentViewPrescriptionBinding!!.recyclerViewRootscareViewprescription
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterViewPrescriptionRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }

}