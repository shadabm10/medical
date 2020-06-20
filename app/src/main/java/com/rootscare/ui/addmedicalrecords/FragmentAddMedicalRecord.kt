package com.rootscare.ui.addmedicalrecords

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.FragmentAddMedicalRecordsBinding
import com.rootscare.databinding.FragmentAppointmentBinding
import com.rootscare.ui.appointment.FragmentAppointment
import com.rootscare.ui.appointment.FragmentAppointmentNavigator
import com.rootscare.ui.appointment.FragmentAppointmentViewModel
import com.rootscare.ui.base.BaseFragment

class FragmentAddMedicalRecord : BaseFragment<FragmentAddMedicalRecordsBinding, FragmentAddMedicalRecordViewModel>(),
    FragmentAddMedicalRecordNavigator {
    private var fragmentAddMedicalRecordsBinding: FragmentAddMedicalRecordsBinding? = null
    private var fragmentAddMedicalRecordViewModel: FragmentAddMedicalRecordViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_medical_records
    override val viewModel: FragmentAddMedicalRecordViewModel
        get() {
            fragmentAddMedicalRecordViewModel =
                ViewModelProviders.of(this).get(FragmentAddMedicalRecordViewModel::class.java!!)
            return fragmentAddMedicalRecordViewModel as FragmentAddMedicalRecordViewModel
        }

    companion object {
        fun newInstance(): FragmentAddMedicalRecord {
            val args = Bundle()
            val fragment = FragmentAddMedicalRecord()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAddMedicalRecordViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddMedicalRecordsBinding = viewDataBinding
    }
}