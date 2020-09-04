package com.rootscare.ui.appointment.subfragment

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
import com.rootscare.databinding.FragmentAppiontmentDetailsBinding
import com.rootscare.databinding.FragmentAppointmentBinding
import com.rootscare.ui.appointment.FragmentAppointment
import com.rootscare.ui.appointment.FragmentAppointmentNavigator
import com.rootscare.ui.appointment.FragmentAppointmentViewModel
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.subfragment.HomeFragment

class FragmentAppiontmentDetails  : BaseFragment<FragmentAppiontmentDetailsBinding, FragmentAppiontmentDetailsViewModel>(),
    FragmentAppiontmentDetailsNavigator {
    private var fragmentAppiontmentDetailsBinding: FragmentAppiontmentDetailsBinding? = null
    private var fragmentAppiontmentDetailsViewModel: FragmentAppiontmentDetailsViewModel? = null
    private var appointmentId:String=""
    var stratTime=""
    var endTime=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_appiontment_details
    override val viewModel: FragmentAppiontmentDetailsViewModel
        get() {
            fragmentAppiontmentDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentAppiontmentDetailsViewModel::class.java!!)
            return fragmentAppiontmentDetailsViewModel as FragmentAppiontmentDetailsViewModel
        }

    companion object {
        fun newInstance(id: String): FragmentAppiontmentDetails {
            val args = Bundle()
            args.putString("appointmentid",id)
            val fragment = FragmentAppiontmentDetails()
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
            appointmentDetailsRequest?.serviceType="doctor"
            fragmentAppiontmentDetailsViewModel?.apiappointmentdetails(appointmentDetailsRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun successDoctorAppointmentResponse(doctorAppointmentResponse: DoctorAppointmentResponse?) {
        baseActivity?.hideLoading()
        if(doctorAppointmentResponse?.code.equals("200")){
            if(doctorAppointmentResponse?.result!=null){

                if(!doctorAppointmentResponse?.result?.doctorImage.equals("") && doctorAppointmentResponse?.result?.doctorImage!=null){
                    Glide.with(this!!.activity!!)
                        .load(activity?.getString(R.string.api_base)+"uploads/images/" + (doctorAppointmentResponse?.result?.doctorImage))
                        .into(fragmentAppiontmentDetailsBinding?.imgAppointmentDetailsProfile!!)
                }
                if (doctorAppointmentResponse?.result?.doctorName!=null && !doctorAppointmentResponse?.result?.doctorName.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtDoctorName?.setText(doctorAppointmentResponse?.result?.doctorName)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtDoctorName?.setText("")
                }
                if (doctorAppointmentResponse?.result?.patientName!=null && !doctorAppointmentResponse?.result?.patientName.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtPatientName?.setText(doctorAppointmentResponse?.result?.patientName)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtPatientName?.setText("")
                }

                if(doctorAppointmentResponse?.result?.appointmentDate!=null && !doctorAppointmentResponse?.result?.appointmentDate.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtAppointmentDate?.setText(doctorAppointmentResponse?.result?.appointmentDate)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtAppointmentDate?.setText("")
                }
                if(doctorAppointmentResponse?.result?.hospitalName!=null && !doctorAppointmentResponse?.result?.hospitalName.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtPatientAddress?.setText(doctorAppointmentResponse?.result?.hospitalName)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtPatientAddress?.setText("")
                }

                if(doctorAppointmentResponse?.result?.price!=null && !doctorAppointmentResponse?.result?.price.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtAmount?.setText(doctorAppointmentResponse?.result?.price)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtAmount?.setText("")
                }

                if(doctorAppointmentResponse?.result?.fromTime!=null && !doctorAppointmentResponse?.result?.fromTime.equals("")){
                    stratTime=doctorAppointmentResponse?.result?.fromTime
                }else{
                    stratTime=""
                }

                if(doctorAppointmentResponse?.result?.toTime!=null && !doctorAppointmentResponse?.result?.toTime.equals("")){
                    endTime=doctorAppointmentResponse?.result?.toTime
                }else{
                    endTime=""
                }

                fragmentAppiontmentDetailsBinding?.txtBookingSlot?.text=stratTime+"-"+endTime

                if(doctorAppointmentResponse?.result?.doctorExperience!=null && !doctorAppointmentResponse?.result?.doctorExperience.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtDoctorExperience?.setText("Work Experience "+" "+doctorAppointmentResponse?.result?.doctorExperience+" "+"years")
                }else{
                    fragmentAppiontmentDetailsBinding?.txtDoctorExperience?.setText("")
                }

                if (doctorAppointmentResponse?.result?.patientContact!=null && !doctorAppointmentResponse?.result?.patientContact.equals("")){
                    fragmentAppiontmentDetailsBinding?.txtPatientContactnumber?.setText(doctorAppointmentResponse?.result?.patientContact)
                }else{
                    fragmentAppiontmentDetailsBinding?.txtPatientContactnumber?.setText("")
                }
            }

        }else{
            Toast.makeText(activity, doctorAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
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