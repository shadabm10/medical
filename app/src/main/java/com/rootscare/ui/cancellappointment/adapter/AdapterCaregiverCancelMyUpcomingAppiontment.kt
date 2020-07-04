package com.rootscare.ui.cancellappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.CaregiverAppointmentItem
import com.rootscare.databinding.ItemCancellAppointmentBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import kotlinx.android.synthetic.main.item_cancell_appointment.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterCaregiverCancelMyUpcomingAppiontment (val caregiverAppointmentList: ArrayList<CaregiverAppointmentItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterCaregiverCancelMyUpcomingAppiontment.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemCancellAppointmentBinding>(
            LayoutInflater.from(context),
            R.layout.item_cancell_appointment, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return caregiverAppointmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemCancellAppointmentBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
//            })
//

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(caregiverAppointmentList?.get(pos)?.orderId!=null && !caregiverAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_appointment_name?.setText(caregiverAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_appointment_name?.setText("")
            }

            if(caregiverAppointmentList?.get(pos)?.patientName!=null && !caregiverAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_cancelappointment_patient_name?.setText(caregiverAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_cancelappointment_patient_name?.setText("")
            }
            itemView?.rootView?.txt_header?.setText("Caregiver Name :")
            if(caregiverAppointmentList?.get(pos)?.caregiverName!=null && !caregiverAppointmentList?.get(pos)?.caregiverName.equals("")){
                itemView?.rootView?.txt_cancelappointment_doctor_name?.setText(caregiverAppointmentList?.get(pos)?.caregiverName)
            }else{
                itemView?.rootView?.txt_cancelappointment_doctor_name?.setText("")
            }

            if(caregiverAppointmentList?.get(pos)?.bookingDate!=null && !caregiverAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_cancelappointment_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",caregiverAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_cancelappointment_booking_date?.setText("")
            }
            if(caregiverAppointmentList?.get(pos)?.fromDate!=null && !caregiverAppointmentList?.get(pos)?.fromDate.equals("")){
                itemView?.rootView?.txt_cancelappointment_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",caregiverAppointmentList?.get(pos)?.fromDate))
            }else{
                itemView?.rootView?.txt_cancelappointment_appointment_date?.setText("")
            }

            if (caregiverAppointmentList?.get(pos)?.patientContact!=null && !caregiverAppointmentList?.get(pos)?.patientContact.equals("")){
                itemView?.rootView?.txt_cancelappointment_phone_no?.setText(caregiverAppointmentList?.get(pos)?.patientContact)
            }else{
                itemView?.rootView?.txt_cancelappointment_phone_no?.setText("")
            }
            itemView?.rootView?.txt_appointment_status?.setText(caregiverAppointmentList?.get(pos)?.appointmentStatus)
            itemView?.rootView?.txt_appointment_acceptance?.setText(caregiverAppointmentList?.get(pos)?.acceptanceStatus)

        }

        fun formateDateFromstring(inputFormat: String?, outputFormat: String?, inputDate: String?): String? {
            var parsed: Date? = null
            var outputDate = ""
            val df_input =
                SimpleDateFormat(inputFormat, Locale.getDefault())
            val df_output =
                SimpleDateFormat(outputFormat, Locale.getDefault())
            try {
                parsed = df_input.parse(inputDate)
                outputDate = df_output.format(parsed)
            } catch (e: ParseException) {
            }
            return outputDate
        }
    }

}