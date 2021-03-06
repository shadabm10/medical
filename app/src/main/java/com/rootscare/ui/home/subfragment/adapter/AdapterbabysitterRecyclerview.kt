package com.rootscare.ui.home.subfragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rootscare.R
import com.rootscare.data.model.api.response.patienthome.BabysitterItem
import com.rootscare.data.model.api.response.patienthome.NurseItem
import com.rootscare.databinding.ItemHomeBabysitterRecyclerviewlistBinding
import com.rootscare.databinding.ItemHomeNursesRecyclerviewBinding
import kotlinx.android.synthetic.main.item_home_babysitter_recyclerviewlist.view.*
import kotlinx.android.synthetic.main.item_home_nurses_recyclerview.view.*

class AdapterbabysitterRecyclerview (val babysitterList: ArrayList<BabysitterItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterbabysitterRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

//    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
//    internal lateinit var recyclerViewItemClickWithView: RecyclerViewItemClickWithView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHomeBabysitterRecyclerviewlistBinding>(
            LayoutInflater.from(context),
            R.layout.item_home_babysitter_recyclerviewlist, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return babysitterList!!.size
//        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHomeBabysitterRecyclerviewlistBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.btn_send_a_feed_back?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClick?.onClick(trainerList?.get(local_position)?.id!!, trainerList?.get(local_position)?.name!!)
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

            itemView?.rootView?.txt_home_babysitter_name?.setText(babysitterList?.get(pos)?.firstName+" "+babysitterList?.get(pos)?.lastName)
            itemView?.rootView?.txt_home_babysitter_qualification?.setText(babysitterList?.get(pos)?.qualification)
            Glide.with(context)
                .load(context.getString(R.string.api_base)+"uploads/images/" + (babysitterList?.get(pos)?.image))
                .into(itemView?.rootView?.img_babysitter_profile!!)




        }
    }

}