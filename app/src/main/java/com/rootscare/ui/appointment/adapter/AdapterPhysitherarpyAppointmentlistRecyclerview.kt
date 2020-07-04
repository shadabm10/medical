package com.rootscare.ui.appointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.PathologyAppointmentItem
import com.rootscare.data.model.api.response.appointmenthistoryresponse.PhysiotherapyAppointmentItem
import com.rootscare.databinding.ItemAppointmentlistRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterPhysitherarpyAppointmentlistRecyclerview (val pathologyAppointmentList: ArrayList<PhysiotherapyAppointmentItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterPhysitherarpyAppointmentlistRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemAppointmentlistRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_appointmentlist_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return pathologyAppointmentList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemAppointmentlistRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(1)
            })
        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            itemView?.rootView?.txt_appointment_id?.setText(pathologyAppointmentList?.get(pos)?.orderId)
            itemView?.rootView?.txt_patient_name?.setText(pathologyAppointmentList?.get(pos)?.patientName)
            itemView?.rootView?.txt_appoitment_header?.setText("Physiotherapist Name : ")
            itemView?.rootView?.txt_doctor_name?.setText(pathologyAppointmentList?.get(pos)?.physiotherapistName)
            itemView?.rootView?.txt_phone_no?.setText(pathologyAppointmentList?.get(pos)?.patientContact)
            itemView?.rootView?.txt_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.bookingDate))
            itemView?.rootView?.txt_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.fromDate)+" "+"-"+" "+formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.toDate))
            itemView?.rootView?.txt_appointment_status?.setText(pathologyAppointmentList?.get(pos)?.appointmentStatus)
            itemView?.rootView?.txt_appointment_acceptance?.setText(pathologyAppointmentList?.get(pos)?.acceptanceStatus)

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