package com.rootscare.ui.nurses.appointmentdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.appointmentdetailsrequest.AppointmentDetailsRequest
import com.rootscare.data.model.api.response.appointmentdetails.DoctorAppointmentResponse
import com.rootscare.data.model.api.response.nurses.nurseappointmentdetails.NurseAppointmentDetailsResponse
import com.rootscare.databinding.FragmentAppiontmentDetailsBinding
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetails
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetailsNavigator
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetailsViewModel
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.subfragment.HomeFragment

class FragmentNurseAppointmentDetails : BaseFragment<FragmentAppiontmentDetailsBinding, FragmentNurseAppointmentDetailsViewModel>(),
    FragmentNurseAppointmentDetailsNavigator {
    private var fragmentAppiontmentDetailsBinding: FragmentAppiontmentDetailsBinding? = null
    private var fragmentAppiontmentDetailsViewModel: FragmentNurseAppointmentDetailsViewModel? = null
    private var appointmentId:String=""
    var stratTime=""
    var endTime=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_appiontment_details
    override val viewModel: FragmentNurseAppointmentDetailsViewModel
        get() {
            fragmentAppiontmentDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentNurseAppointmentDetailsViewModel::class.java!!)
            return fragmentAppiontmentDetailsViewModel as FragmentNurseAppointmentDetailsViewModel
        }

    companion object {
        fun newInstance(id: String): FragmentNurseAppointmentDetails {
            val args = Bundle()
            args.putString("appointmentid",id)
            val fragment = FragmentNurseAppointmentDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAppiontmentDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAppiontmentDetailsBinding = viewDataBinding
        if (arguments!=null && arguments?.getString("appointmentid")!=null){
            appointmentId = arguments?.getString("appointmentid")!!
            Log.d("AppointmentId Id", ": " + appointmentId )
        }

        if(isNetworkConnected){
            baseActivity?.showLoading()
            var appointmentDetailsRequest= AppointmentDetailsRequest()
            appointmentDetailsRequest?.id=appointmentId
            appointmentDetailsRequest?.serviceType="nurse"
            fragmentAppiontmentDetailsViewModel?.apiappointmentdetails(appointmentDetailsRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun successDoctorAppointmentResponse(surseAppointmentDetailsResponse: NurseAppointmentDetailsResponse?) {
        baseActivity?.hideLoading()
        if(surseAppointmentDetailsResponse?.code.equals("200")){
            if(surseAppointmentDetailsResponse?.result!=null){

                if(!surseAppointmentDetailsResponse?.result?.nurseImage.equals("") && surseAppointmentDetailsResponse?.result?.nurseImage!=null){
                    Glide.with(this!!.activity!!)
                        .load(activity?.getString(R.string.api_base)+"uploads/images/" + (surseAppointmentDetailsResponse?.result?.nurseImage))
                        .into(fragmentAppiontmentDetailsBinding?.imgAppointmentDetailsProfile!!)
                }
                if (surseAppointmentDetailsResponse?.result?.nurseName!=null && !surseAppointmentDetailsResponse?.result?.nurseName.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtDoctorName?.setText(surseAppointmentDetailsResponse?.result?.nurseName)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtDoctorName?.setText("")
                }
                if (surseAppointmentDetailsResponse?.result?.patientName!=null && !surseAppointmentDetailsResponse?.result?.patientName.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtPatientName?.setText(surseAppointmentDetailsResponse?.result?.patientName)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtPatientName?.setText("")
                }

                if(surseAppointmentDetailsResponse?.result?.appointmentDate!=null && !surseAppointmentDetailsResponse?.result?.appointmentDate.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtAppointmentDate?.setText(surseAppointmentDetailsResponse?.result?.appointmentDate)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtAppointmentDate?.setText("")
                }
                if(surseAppointmentDetailsResponse?.result?.appointmentStatus!=null && !surseAppointmentDetailsResponse?.result?.appointmentStatus.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtPatientAddress?.setText(surseAppointmentDetailsResponse?.result?.appointmentStatus)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtPatientAddress?.setText("")
                }

                if(surseAppointmentDetailsResponse?.result?.price!=null && !surseAppointmentDetailsResponse?.result?.price.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtAmount?.setText(surseAppointmentDetailsResponse?.result?.price)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtAmount?.setText("")
                }

                if(surseAppointmentDetailsResponse?.result?.fromTime!=null && !surseAppointmentDetailsResponse?.result?.fromTime.equals("")){
                    stratTime=surseAppointmentDetailsResponse?.result?.fromTime
                }else{
                    stratTime=""
                }

                if(surseAppointmentDetailsResponse?.result?.toTime!=null && !surseAppointmentDetailsResponse?.result?.toTime.equals("")){
                    endTime=surseAppointmentDetailsResponse?.result?.toTime
                }else{
                    endTime=""
                }

                fragmentAppiontmentDetailsBinding?.txtBookingSlot?.text=stratTime+"-"+endTime

                if(surseAppointmentDetailsResponse?.result?.nurseExperience!=null && !surseAppointmentDetailsResponse?.result?.nurseExperience.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtDoctorExperience?.setText("Work Experience "+" "+surseAppointmentDetailsResponse?.result?.nurseExperience+" "+"years")
                }else{
                    fragmentAppiontmentDetailsBinding?.txtDoctorExperience?.setText("")
                }

                if (surseAppointmentDetailsResponse?.result?.patientContact!=null && !surseAppointmentDetailsResponse?.result?.patientContact.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtPatientContactnumber?.setText(surseAppointmentDetailsResponse?.result?.patientContact)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtPatientContactnumber?.setText("")
                }
            }

        }else{
            Toast.makeText(activity, surseAppointmentDetailsResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorDoctorAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}