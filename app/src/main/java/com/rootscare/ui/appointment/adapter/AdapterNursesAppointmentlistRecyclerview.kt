package com.rootscare.ui.appointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.interfaces.OnClickWithTwoButton
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.NurseAppointmentItem
import com.rootscare.databinding.ItemAppointmentlistRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterNursesAppointmentlistRecyclerview (val nurseAppointmentList: ArrayList<NurseAppointmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterNursesAppointmentlistRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClick: OnClickWithTwoButton
    var fromtime=""
    var totime=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemAppointmentlistRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_appointmentlist_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return nurseAppointmentList!!.size
//        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemAppointmentlistRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.btn_appointment_view_details?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClick?.onFirstItemClick(nurseAppointmentList?.get(local_position)?.id?.toInt()!!)
            })
            itemView?.root?.btn_appointment_add_review?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClick?.onSecondItemClick(nurseAppointmentList?.get(local_position)?.nurseId?.toInt()!!)
            })
//


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            itemView?.rootView?.txt_appointment_id?.setText(nurseAppointmentList?.get(pos)?.orderId)
            itemView?.rootView?.txt_patient_name?.setText(nurseAppointmentList?.get(pos)?.patientName)
            itemView?.rootView?.txt_appoitment_header?.setText("Nurse Name : ")
            itemView?.rootView?.txt_doctor_name?.setText(nurseAppointmentList?.get(pos)?.nurseName)
            itemView?.rootView?.txt_phone_no?.setText(nurseAppointmentList?.get(pos)?.patientContact)
            itemView?.rootView?.txt_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",nurseAppointmentList?.get(pos)?.bookingDate))
            itemView?.rootView?.txt_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",nurseAppointmentList?.get(pos)?.fromDate))
//            +" "+"-"+" "+formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",nurseAppointmentList?.get(pos)?.toDate)
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
            itemView?.rootView?.txt_appointmenthistory_time?.setText(fromtime+"-"+totime)
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