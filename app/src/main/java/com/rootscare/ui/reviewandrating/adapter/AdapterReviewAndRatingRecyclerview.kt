package com.rootscare.ui.reviewandrating.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rootscare.R
import com.rootscare.data.model.api.response.patientreviewandratingresponse.ResultItem
import com.rootscare.databinding.ItemReviewAndRatingRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_review_and_rating_recyclerview.view.*
import kotlinx.android.synthetic.main.item_rootscare_doctor_categorilisting_recyclerview.view.*

class AdapterReviewAndRatingRecyclerview  (val reviewandratinglist: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterReviewAndRatingRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemReviewAndRatingRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_review_and_rating_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return reviewandratinglist!!.size
//        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemReviewAndRatingRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.crdview_doctor_category_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(1)
            })
        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            itemView?.rootView?.txt_review_name?.setText(reviewandratinglist?.get(pos)?.reviewTo)
            itemView?.rootView?.txt_review_and_rating?.setText(reviewandratinglist?.get(pos)?.review)

            if(reviewandratinglist?.get(pos)?.rating!=null && !reviewandratinglist?.get(pos)?.rating.equals("")){
                itemView?.rootView?.ratingbar?.rating= reviewandratinglist?.get(pos)?.rating?.toFloat()!!
            }

            Glide.with(context)
                .load(context.getString(R.string.api_base)+"uploads/images/" + (reviewandratinglist?.get(pos)?.image))
                .into(itemView?.rootView?.img_review_profile_photo!!)
        }
    }

}