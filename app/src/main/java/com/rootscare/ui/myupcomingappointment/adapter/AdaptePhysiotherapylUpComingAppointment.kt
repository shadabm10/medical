package com.rootscare.ui.myupcomingappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.PhysiotherapyAppointmentItem
import com.rootscare.databinding.ItemMyUpcomingappointmentRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.*
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_appointment_date
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_booking_date
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_doctor_name
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_patient_name
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdaptePhysiotherapylUpComingAppointment  (val pathologyAppointmentList: ArrayList<PhysiotherapyAppointmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdaptePhysiotherapylUpComingAppointment.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemMyUpcomingappointmentRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_my_upcomingappointment_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return pathologyAppointmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemMyUpcomingappointmentRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.btn_appointment_cancel?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(pathologyAppointmentList?.get(local_position)?.id?.toInt()!!)
            })
//


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(pathologyAppointmentList?.get(pos)?.orderId!=null && !pathologyAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_upcomming_appointment?.setText(pathologyAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_upcomming_appointment?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.patientName!=null && !pathologyAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_patient_name?.setText(pathologyAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_patient_name?.setText("")
            }
            itemView?.rootView?.txt_name_header?.setText("Physiotherapist Name :")
            if(pathologyAppointmentList?.get(pos)?.physiotherapistName!=null && !pathologyAppointmentList?.get(pos)?.physiotherapistName.equals("")){
                itemView?.rootView?.txt_doctor_name?.setText(pathologyAppointmentList?.get(pos)?.physiotherapistName)
            }else{
                itemView?.rootView?.txt_doctor_name?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.bookingDate!=null && !pathologyAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_booking_date?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.fromDate!=null && !pathologyAppointmentList?.get(pos)?.fromDate.equals("")){
                itemView?.rootView?.txt_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.fromDate))
            }else{
                itemView?.rootView?.txt_appointment_date?.setText("")
            }

            if (pathologyAppointmentList?.get(pos)?.patientContact!=null && !pathologyAppointmentList?.get(pos)?.patientContact.equals("")){
                itemView?.rootView?.txt_upcoming_appointmentphone_no?.setText(pathologyAppointmentList?.get(pos)?.patientContact)
            }else{
                itemView?.rootView?.txt_upcoming_appointmentphone_no?.setText("")
            }

            itemView?.rootView?.txt_upappointment_status?.setText(pathologyAppointmentList?.get(pos)?.appointmentStatus)
            itemView?.rootView?.txt_upappointment_acceptance?.setText(pathologyAppointmentList?.get(pos)?.acceptanceStatus)


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