package com.rootscare.ui.myupcomingappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.BabysitterAppointmentItem
import com.rootscare.databinding.ItemMyUpcomingappointmentRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_cancell_appointment.view.*
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapteBabySitterUpComingAppointment  (val babysitterAppointmentList: ArrayList<BabysitterAppointmentItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapteBabySitterUpComingAppointment.ViewHolder>() {
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
        return babysitterAppointmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemMyUpcomingappointmentRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {

            itemView?.root?.btn_appointment_cancel?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(babysitterAppointmentList?.get(local_position)?.id?.toInt()!!)
            })
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
//            })
//            itemView?.root?.btn_view_trainner_profile?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(trainerList?.get(local_position)?.id?.toInt()!!)
//            })

//            itemView.root?.img_bid?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_bid,local_position)
//                    //serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_bid,it1) }
//                }
//            }
//
//            itemView.root?.img_details?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_details,local_position)
//                    // serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_details,it1) }
//                }
//            }


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(babysitterAppointmentList?.get(pos)?.orderId!=null && !babysitterAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_upcomming_appointment?.setText(babysitterAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_upcomming_appointment?.setText("")
            }

            if(babysitterAppointmentList?.get(pos)?.patientName!=null && !babysitterAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_patient_name?.setText(babysitterAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_patient_name?.setText("")
            }
            itemView?.rootView?.txt_name_header?.setText("Babysitter Name :")
            if(babysitterAppointmentList?.get(pos)?.babysitterName!=null && !babysitterAppointmentList?.get(pos)?.babysitterName.equals("")){
                itemView?.rootView?.txt_doctor_name?.setText(babysitterAppointmentList?.get(pos)?.babysitterName)
            }else{
                itemView?.rootView?.txt_doctor_name?.setText("")
            }

            if(babysitterAppointmentList?.get(pos)?.bookingDate!=null && !babysitterAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",babysitterAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_booking_date?.setText("")
            }

            if (babysitterAppointmentList?.get(pos)?.patientContact!=null && !babysitterAppointmentList?.get(pos)?.patientContact.equals("")){
                itemView?.rootView?.txt_upcoming_appointmentphone_no?.setText(babysitterAppointmentList?.get(pos)?.patientContact)
            }else{
                itemView?.rootView?.txt_upcoming_appointmentphone_no?.setText("")
            }

            if(babysitterAppointmentList?.get(pos)?.fromDate!=null && !babysitterAppointmentList?.get(pos)?.fromDate.equals("")){
                itemView?.rootView?.txt_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",babysitterAppointmentList?.get(pos)?.fromDate))
            }else{
                itemView?.rootView?.txt_appointment_date?.setText("")
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

}