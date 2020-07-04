package com.rootscare.ui.cancellappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.BabysitterAppointmentItem
import com.rootscare.databinding.ItemCancellAppointmentBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import kotlinx.android.synthetic.main.item_cancell_appointment.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterBabySitterCancelMyUpcomingAppiontment(val babysitterAppointmentList: ArrayList<BabysitterAppointmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterBabySitterCancelMyUpcomingAppiontment.ViewHolder>() {
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
        return babysitterAppointmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemCancellAppointmentBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(babysitterAppointmentList?.get(pos)?.orderId!=null && !babysitterAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_appointment_name?.setText(babysitterAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_appointment_name?.setText("")
            }

            if(babysitterAppointmentList?.get(pos)?.patientName!=null && !babysitterAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_cancelappointment_patient_name?.setText(babysitterAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_cancelappointment_patient_name?.setText("")
            }
            itemView?.rootView?.txt_header?.setText("Babysitter Name :")
            if(babysitterAppointmentList?.get(pos)?.babysitterName!=null && !babysitterAppointmentList?.get(pos)?.babysitterName.equals("")){
                itemView?.rootView?.txt_cancelappointment_doctor_name?.setText(babysitterAppointmentList?.get(pos)?.babysitterName)
            }else{
                itemView?.rootView?.txt_cancelappointment_doctor_name?.setText("")
            }

            if(babysitterAppointmentList?.get(pos)?.bookingDate!=null && !babysitterAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_cancelappointment_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",babysitterAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_cancelappointment_booking_date?.setText("")
            }
            if(babysitterAppointmentList?.get(pos)?.fromDate!=null && !babysitterAppointmentList?.get(pos)?.fromDate.equals("")){
                itemView?.rootView?.txt_cancelappointment_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",babysitterAppointmentList?.get(pos)?.fromDate))
            }else{
                itemView?.rootView?.txt_cancelappointment_appointment_date?.setText("")
            }
            if (babysitterAppointmentList?.get(pos)?.patientContact!=null && !babysitterAppointmentList?.get(pos)?.patientContact.equals("")){
                itemView?.rootView?.txt_cancelappointment_phone_no?.setText(babysitterAppointmentList?.get(pos)?.patientContact)
            }else{
                itemView?.rootView?.txt_cancelappointment_phone_no?.setText("")
            }

            itemView?.rootView?.txt_appointment_status?.setText(babysitterAppointmentList?.get(pos)?.appointmentStatus)
            itemView?.rootView?.txt_appointment_acceptance?.setText(babysitterAppointmentList?.get(pos)?.acceptanceStatus)

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