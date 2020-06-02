package com.rootscare.ui.medicalrecords

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentMedicalRecordsBinding
import com.rootscare.databinding.FragmentViewPrescriptionBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.medicalrecords.adapter.AdapterMedicalRecordsRecyclerview


class FragmentMedicalRecords : BaseFragment<FragmentMedicalRecordsBinding, FragmentMedicalRecordsViewModel>(),
    FragmentMedicalRecordsNavigator {
    private var fragmentMedicalRecordsBinding: FragmentMedicalRecordsBinding? = null
    private var fragmentMedicalRecordsViewModel: FragmentMedicalRecordsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_medical_records
    override val viewModel: FragmentMedicalRecordsViewModel
        get() {
            fragmentMedicalRecordsViewModel =
                ViewModelProviders.of(this).get(FragmentMedicalRecordsViewModel::class.java!!)
            return fragmentMedicalRecordsViewModel as FragmentMedicalRecordsViewModel
        }

    companion object {
        fun newInstance(): FragmentMedicalRecords {
            val args = Bundle()
            val fragment = FragmentMedicalRecords()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMedicalRecordsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMedicalRecordsBinding = viewDataBinding
        setUpViewPrescriptionlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentMedicalRecordsBinding!!.recyclerViewRootscareMedicalrecord != null)
        val recyclerView = fragmentMedicalRecordsBinding!!.recyclerViewRootscareMedicalrecord
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterMedicalRecordsRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }

}