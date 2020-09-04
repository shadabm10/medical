package com.rootscare.ui.bookingappointment.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.interfaces.OnDoctorSlotClickListner
import com.rootscare.R
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.SlotItem
import com.rootscare.databinding.ItemDoctorSlotDivissionBinding
import com.rootscare.databinding.ItemToTimeRecyclerviewBinding
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_doctor_slot_divission.view.*

class AdapterDoctorSlotDivision(val slotList: ArrayList<SlotItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterDoctorSlotDivision.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    val sdk = Build.VERSION.SDK_INT
    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    var selectedPosition =-1
    internal lateinit var recyclerViewItemClickWithView: OnDoctorSlotClickListner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorSlotDivissionBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_slot_divission, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return slotList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorSlotDivissionBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.ll_slot?.setOnClickListener(View.OnClickListener {
                selectedPosition = local_position
                notifyDataSetChanged()
                recyclerViewItemClickWithView?.onSloctClick(slotList?.get(local_position)!!)
            })
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
            if (selectedPosition == pos) {
//                itemView?.rootView?.ll_slot?.setBackgroundColor(Color.parseColor("#D2F2F5"))

                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    itemView?.rootView?.txt_start_time?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_with_green_bg) )
                    itemView?.rootView?.txt_end_time?.setBackground(ContextCompat.getDrawable(context, R.drawable.border_with_green_bg))
                } else {
                    itemView?.rootView?.txt_start_time?.setBackground(ContextCompat.getDrawable(context, R.drawable.border_with_green_bg))
                    itemView?.rootView?.txt_end_time?.setBackground(ContextCompat.getDrawable(context, R.drawable.border_with_green_bg))
                }
            } else{
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    itemView?.rootView?.txt_start_time?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_rounded_grey_border) )
                    itemView?.rootView?.txt_end_time?.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_grey_border))
                } else {
                    itemView?.rootView?.txt_start_time?.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_grey_border))
                    itemView?.rootView?.txt_end_time?.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_grey_border))
                }
//                itemView?.rootView?.ll_slot?.setBackgroundColor(Color.parseColor("#ffffff"))
        }
            itemView?.rootView?.txt_start_time?.text=slotList?.get(pos)?.timeFrom
            itemView?.rootView?.txt_end_time?.text=slotList?.get(pos)?.timeTo

            if(slotList?.get(pos)?.status.equals("Available")){
                itemView?.rootView?.ll_slot?.setAlpha(1.0F)
                itemView?.rootView?.ll_slot?.isClickable=true
            }else if(slotList?.get(pos)?.status.equals("Booked")){
                itemView?.rootView?.ll_slot?.setAlpha(0.4F)
                itemView?.rootView?.ll_slot?.isClickable=false
            }

//            itemView?.rootView?.txt_teacher_name?.text= trainerList?.get(pos)?.name
//            itemView?.rootView?.txt_teacher_qualification?.text= "Qualification : "+" "+trainerList?.get(pos)?.qualification
//            if(trainerList?.get(pos)?.avgRating!=null && !trainerList?.get(pos)?.avgRating.equals("")){
//                itemView?.rootView?.ratingBarteacher?.rating= trainerList?.get(pos)?.avgRating?.toFloat()!!
//            }





//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f


        }
    }

}