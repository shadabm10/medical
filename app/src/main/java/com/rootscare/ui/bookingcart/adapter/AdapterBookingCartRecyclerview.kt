package com.rootscare.ui.bookingcart.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.interfaces.OnClickOfCartItem
import com.interfaces.OnClickWithTwoButton
import com.rootscare.R
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.ResultItem
import com.rootscare.databinding.ItemBookingCartRecyclerviewBinding
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_booking_cart_recyclerview.view.*


class AdapterBookingCartRecyclerview (val cartItemList: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterBookingCartRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnClickOfCartItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemBookingCartRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_booking_cart_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return cartItemList!!.size
//        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemBookingCartRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.img_remove?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onFirstItemClick(cartItemList?.get(local_position)?.id?.toInt()!!)
            })
            itemView?.root?.checkbox_cartitem?.setOnClickListener(View.OnClickListener {


             //   recyclerViewItemClickWithView?.onFirstItemClick(cartItemList?.get(local_position)?.id?.toInt()!!)
            })
            itemView?.root?.checkbox_cartitem?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    //set your object's last status

                    cartItemList?.get(local_position)?.isselectitem=isChecked

                    recyclerViewItemClickWithView?.onSecondItemClick(cartItemList)
                }

            })


        }

        fun onBind(pos: Int) {

            Log.d(TAG, " true")
            local_position = pos
            if(cartItemList?.get(pos)?.isselectitem!!){
                itemView?.rootView?.checkbox_cartitem?.isChecked=true
            }else{
                itemView?.rootView?.checkbox_cartitem?.isChecked=false
            }
            itemView?.rootView?.txt_booked_doctor_name?.setText(cartItemList?.get(pos)?.doctorDetails?.firstName+" "+cartItemList?.get(pos)?.doctorDetails?.lastName)
            itemView?.rootView?.txt_booked_doctor_qualification?.setText(cartItemList?.get(pos)?.doctorDetails?.qualification)
            itemView?.rootView?.txt_booked_doctor_description?.setText(cartItemList?.get(pos)?.doctorDetails?.description)
            itemView?.rootView?.txt_booked_doctor_appointmentdate?.setText(cartItemList?.get(pos)?.fromDate)
            itemView?.rootView?.txt_booked_doctor_time?.setText(cartItemList?.get(pos)?.fromTime+"-"+cartItemList?.get(pos)?.toTime)
            itemView?.rootView?.txt_booked_doctor_fees?.setText("Rs."+""+cartItemList?.get(pos)?.price)
            Glide.with(context)
                .load(context.getString(R.string.api_base)+"uploads/images/" + (cartItemList?.get(pos)?.doctorDetails?.image))
                .into(itemView?.rootView?.img_doctor_photo!!)

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

    fun geteCartItemList(): ArrayList<ResultItem?>? {
        var items: ArrayList<ResultItem?> = ArrayList<ResultItem?>()
        items= this!!.cartItemList!!

        return items
    }

}