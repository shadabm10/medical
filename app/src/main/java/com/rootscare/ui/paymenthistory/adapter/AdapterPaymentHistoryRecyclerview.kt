package com.rootscare.ui.paymenthistory.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.paymenthistoryresponse.ResultItem
import com.rootscare.databinding.ItemRootscarePaymentHistoryRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_rootscare_payment_history_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AdapterPaymentHistoryRecyclerview  (val paymentHistoryList: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterPaymentHistoryRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemRootscarePaymentHistoryRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_rootscare_payment_history_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return paymentHistoryList!!.size
//        return 9
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemRootscarePaymentHistoryRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
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
            if(local_position==0){
                itemView?.rootView?.ll_header?.visibility=View.VISIBLE
                itemView?.rootView?.view_header?.visibility=View.VISIBLE
            }else{
                itemView?.rootView?.ll_header?.visibility=View.GONE
                itemView?.rootView?.view_header?.visibility=View.GONE
            }

            itemView?.rootView?.txt_payment_id?.setText(paymentHistoryList?.get(pos)?.orderId)
            itemView?.rootView?.txt_payment_date?.setText(formateDateFromstring("yyyy-MM-dd", "dd/MM/yyyy",paymentHistoryList?.get(pos)?.date))
            itemView?.rootView?.txt_payment_amount?.setText(paymentHistoryList?.get(pos)?.amount)
            if(paymentHistoryList?.get(pos)?.paymentType.equals("Offline")){
                itemView?.rootView?.txt_payment_type?.setText("COD")
            }else{
                itemView?.rootView?.txt_payment_type?.setText(paymentHistoryList?.get(pos)?.paymentType)
            }
            itemView?.rootView?.txt_payment_status?.setText(paymentHistoryList?.get(pos)?.paymentStatus)







//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f


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