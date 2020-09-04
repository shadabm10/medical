package com.rootscare.ui.cancellappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.NurseAppointmentItem
import com.rootscare.databinding.ItemCancellAppointmentBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import kotlinx.android.synthetic.main.item_cancell_appointment.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterNurseCancelMyUpcomingAppiontment (val nurseAppointmentList: ArrayList<NurseAppointmentItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterNurseCancelMyUpcomingAppiontment.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    var fromtime=""
    var totime=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemCancellAppointmentBinding>(
            LayoutInflater.from(context),
            R.layout.item_cancell_appointment, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return nurseAppointmentList?.size!!
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

            if(nurseAppointmentList?.get(pos)?.orderId!=null && !nurseAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_appointment_name?.setText(nurseAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_appointment_name?.setText("")
            }

            if(nurseAppointmentList?.get(pos)?.patientName!=null && !nurseAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_cancelappointment_patient_name?.setText(nurseAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_cancelappointment_patient_name?.setText("")
            }
            itemView?.rootView?.txt_header?.setText("Nurse Name :")
            if(nurseAppointmentList?.get(pos)?.nurseName!=null && !nurseAppointmentList?.get(pos)?.nurseName.equals("")){
                itemView?.rootView?.txt_cancelappointment_doctor_name?.setText(nurseAppointmentList?.get(pos)?.nurseName)
            }else{
                itemView?.rootView?.txt_cancelappointment_doctor_name?.setText("")
            }

            if(nurseAppointmentList?.get(pos)?.bookingDate!=null && !nurseAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_cancelappointment_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",nurseAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_cancelappointment_booking_date?.setText("")
            }
            if(nurseAppointmentList?.get(pos)?.fromDate!=null && !nurseAppointmentList?.get(pos)?.fromDate.equals("")){
                itemView?.rootView?.txt_cancelappointment_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",nurseAppointmentList?.get(pos)?.fromDate))
            }else{
                itemView?.rootView?.txt_cancelappointment_appointment_date?.setText("")
            }

            if (nurseAppointmentList?.get(pos)?.patientContact!=null && !nurseAppointmentList?.get(pos)?.patientContact.equals("")){
                itemView?.rootView?.txt_cancelappointment_phone_no?.setText(nurseAppointmentList?.get(pos)?.patientContact)
            }else{
                itemView?.rootView?.txt_cancelappointment_phone_no?.setText("")
            }
            itemView?.rootView?.txt_appointment_status?.setText(nurseAppointmentList?.get(pos)?.appointmentStatus)
            itemView?.rootView?.txt_appointment_acceptance?.setText(nurseAppointmentList?.get(pos)?.acceptanceStatus)
            if(nurseAppointmentList?.get(pos)?.fromTime!=null && !nurseAppointmentList?.get(pos)?.fromTime.equals("")){
                fromtime=nurseAppointmentList?.get(pos)?.fromTime!!
            }else{
                fromtime=""
            }
            if(nurseAppointmentList?.get(pos)?.toTime!=null && !nurseAppointmentList?.get(pos)?.toTime.equals("")){
                totime=nurseAppointmentList?.get(pos)?.toTime!!
            }else{
                totime=""
            }
            itemView?.rootView?.txt_appointmentcancel_time?.setText(fromtime+"-"+totime)
        }
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

