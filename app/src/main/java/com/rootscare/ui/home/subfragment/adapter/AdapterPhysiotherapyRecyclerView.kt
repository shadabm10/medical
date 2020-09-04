package com.rootscare.ui.home.subfragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rootscare.R
import com.rootscare.data.model.api.response.patienthome.PhysiotherapyItem
import com.rootscare.databinding.ItemHomeNursesRecyclerviewBinding
import com.rootscare.databinding.ItemHomePhysiotherapyRecyclerviewBinding
import kotlinx.android.synthetic.main.item_home_physiotherapy_recyclerview.view.*

class AdapterPhysiotherapyRecyclerView (val physiotherapyList: ArrayList<PhysiotherapyItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterPhysiotherapyRecyclerView.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

//    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
//    internal lateinit var recyclerViewItemClickWithView: RecyclerViewItemClickWithView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHomePhysiotherapyRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_home_physiotherapy_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return physiotherapyList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHomePhysiotherapyRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.btn_send_a_feed_back?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClick?.onClick(trainerList?.get(local_position)?.id!!, trainerList?.get(local_position)?.name!!)
//            })

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            itemView?.rootView?.txt_home_physiotherapy_name?.setText(physiotherapyList?.get(pos)?.firstName+" "+physiotherapyList?.get(pos)?.lastName)
            itemView?.rootView?.txt_home_physiotherapy_qualification?.setText(physiotherapyList?.get(pos)?.qualification)
            Glide.with(context)
                .load(context.getString(R.string.api_base)+"uploads/images/" + (physiotherapyList?.get(pos)?.image))
                .into(itemView?.rootView?.img_home_pathologyprofile!!)
        }
    }

}