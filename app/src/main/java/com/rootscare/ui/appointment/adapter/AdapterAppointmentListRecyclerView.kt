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
import com.rootscare.data.model.api.response.appointmenthistoryresponse.DoctorAppointmentItem
import com.rootscare.databinding.ItemAppointmentlistRecyclerviewBinding
import com.rootscare.databinding.ItemHomeHospitalRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.txt_appointment_date
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.txt_booking_date
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.txt_doctor_name
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.txt_patient_name
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterAppointmentListRecyclerView (val doctorAppointmentList: ArrayList<DoctorAppointmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterAppointmentListRecyclerView.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }
    var fromtime=""
    var totime=""
    internal lateinit var recyclerViewItemClick: OnClickWithTwoButton
//
//    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemAppointmentlistRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_appointmentlist_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return doctorAppointmentList!!.size
//        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemAppointmentlistRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.btn_appointment_view_details?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClick?.onFirstItemClick(doctorAppointmentList?.get(local_position)?.id?.toInt()!!)
            })
            itemView?.root?.btn_appointment_add_review?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClick?.onSecondItemClick(doctorAppointmentList?.get(local_position)?.doctorId?.toInt()!!)
            })
//


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            itemView?.rootView?.txt_appointment_id?.setText(doctorAppointmentList?.get(pos)?.orderId)
            itemView?.rootView?.txt_patient_name?.setText(doctorAppointmentList?.get(pos)?.patientName)
            itemView?.rootView?.txt_doctor_name?.setText(doctorAppointmentList?.get(pos)?.doctorName)
            itemView?.rootView?.txt_phone_no?.setText(doctorAppointmentList?.get(pos)?.patientContact)
            itemView?.rootView?.txt_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",doctorAppointmentList?.get(pos)?.bookingDate))
            itemView?.rootView?.txt_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",doctorAppointmentList?.get(pos)?.appointmentDate))
            itemView?.rootView?.txt_appointment_status?.setText(doctorAppointmentList?.get(pos)?.appointmentStatus)
            itemView?.rootView?.txt_appointment_acceptance?.setText(doctorAppointmentList?.get(pos)?.acceptanceStatus)
            if(doctorAppointmentList?.get(pos)?.fromTime!=null && !doctorAppointmentList?.get(pos)?.fromTime.equals("")){
                fromtime=doctorAppointmentList?.get(pos)?.fromTime!!
            }else{
                fromtime=""
            }
            if(doctorAppointmentList?.get(pos)?.toTime!=null && !doctorAppointmentList?.get(pos)?.toTime.equals("")){
                totime=doctorAppointmentList?.get(pos)?.toTime!!
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