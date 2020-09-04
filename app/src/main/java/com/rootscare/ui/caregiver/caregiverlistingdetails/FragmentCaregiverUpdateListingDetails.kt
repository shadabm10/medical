package com.rootscare.ui.caregiver.caregiverlistingdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.nurse.nursedetailsrequest.NurseDetailsRequest
import com.rootscare.data.model.api.request.nurse.nurseslots.NurseSlotRequest
import com.rootscare.data.model.api.response.nurses.nursedetails.*
import com.rootscare.data.model.api.response.nurses.nurseviewtiming.NueseViewTimingsResponse
import com.rootscare.data.model.api.response.nurses.nurseviewtiming.ResultItem
import com.rootscare.databinding.FragmentNursesListingDetailsBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.caregiver.caregiverbookingappointment.FragmentCaregiverUpdateBookingAppointment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.caregiverlistingdetails.adapter.AdapterCaregiverEducationListRecyclerView
import com.rootscare.ui.nurses.caregiverlistingdetails.adapter.AdapterCaregiverReviewrecyclerview
import com.rootscare.ui.nurses.caregiverlistingdetails.adapter.AdapterCaregiverSpecilityListRecyclerview
import com.rootscare.ui.nurses.caregiverlistingdetails.adapter.AdapterCaregiverViewTimingRecyclerview
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.*
import com.rootscare.ui.nurses.review.FragmentNurseReviewSubmit
import com.rootscare.ui.profile.FragmentProfile
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentCaregiverUpdateListingDetails : BaseFragment<FragmentNursesListingDetailsBinding, FragmentCaregiverUpdateListingDetailsViewModel>(),
    FragmentCaregiverUpdateListingDetailsNavigator {
    private var fragmentNursesListingDetailsBinding: FragmentNursesListingDetailsBinding? = null
    private var fragmentNursesListingDetailsViewModel: FragmentCaregiverUpdateListingDetailsViewModel? = null
    var nurseFirstName=""
    var nurseLastName=""
    var nurseId=""
    var currentDate=""
    private var hidden = true
    var  initialReviewRatingList: ArrayList<ReviewRatingItem?>?=null
    var  finalReviewRatingList: ArrayList<ReviewRatingItem?>?=null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_listing_details
    override val viewModel: FragmentCaregiverUpdateListingDetailsViewModel
        get() {
            fragmentNursesListingDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentCaregiverUpdateListingDetailsViewModel::class.java!!)
            return fragmentNursesListingDetailsViewModel as FragmentCaregiverUpdateListingDetailsViewModel
        }
    companion object {
        fun newInstance(nurseid: String): FragmentCaregiverUpdateListingDetails {
            val args = Bundle()
            args.putString("nurseid",nurseid)
            val fragment = FragmentCaregiverUpdateListingDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesListingDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesListingDetailsBinding = viewDataBinding
        if (arguments!=null && arguments?.getString("nurseid")!=null){
            nurseId = arguments?.getString("nurseid")!!
            Log.d("Nurse ID", ": " + nurseId )
        }

        var c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        var df = SimpleDateFormat("yyyy-MM-dd");
        var formattedDate = df.format(c);
        currentDate=formattedDate
//Api hit for nurse details
        apiHitForNurseViewTiming()
        apiHitForNurseDetails()
        fragmentNursesListingDetailsBinding?.btnRootscareBookingNurses?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverUpdateBookingAppointment.newInstance(nurseId))
        })

        fragmentNursesListingDetailsBinding?.btnBookingAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverUpdateBookingAppointment.newInstance(nurseId))
        })
        fragmentNursesListingDetailsBinding?.txtNurseSubmitReview?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNurseReviewSubmit.newInstance(nurseId))
        })

        fragmentNursesListingDetailsBinding?.txtNursedetaisheaderReviewWrite?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNurseReviewSubmit.newInstance(nurseId))
        })
        fragmentNursesListingDetailsBinding?.txtNursedetailsViewTimings?.setOnClickListener(View.OnClickListener {
            if(hidden){
                fragmentNursesListingDetailsBinding?.llNurseViewTiming?.visibility=View.VISIBLE
                val animSlideDown = AnimationUtils.loadAnimation(
                    activity?.applicationContext,
                    R.anim.slide_down
                )
                fragmentNursesListingDetailsBinding?.llNurseViewTiming?.startAnimation(animSlideDown)
                hidden=false
                fragmentNursesListingDetailsBinding?.txtNursedetailsViewTimings?.setText("Close Slots")
            }else{
                val animSlideUp: Animation =
                    AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.slide_up)
                fragmentNursesListingDetailsBinding?.llNurseViewTiming?.startAnimation(animSlideUp)
                fragmentNursesListingDetailsBinding?.llNurseViewTiming?.visibility=View.GONE
                hidden=true
                fragmentNursesListingDetailsBinding?.txtNursedetailsViewTimings?.setText("View Slots")
            }

        })

        fragmentNursesListingDetailsBinding?.imgClose?.setOnClickListener(View.OnClickListener {
            val animSlideUp: Animation =
                AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.slide_up)
            fragmentNursesListingDetailsBinding?.llNurseViewTiming?.startAnimation(animSlideUp)
            fragmentNursesListingDetailsBinding?.llNurseViewTiming?.visibility=View.GONE
        })

        fragmentNursesListingDetailsBinding?.txtNursedetailsReviewMore?.setOnClickListener(View.OnClickListener {
            if(fragmentNursesListingDetailsBinding?.txtNursedetailsReviewMore?.text!!.equals("More..")){
                if(finalReviewRatingList!=null && finalReviewRatingList!!.size>0){
                    setReviewRatingListing(finalReviewRatingList)
                    fragmentNursesListingDetailsBinding?.txtNursedetailsReviewMore?.setText("Less..")
                }
            }else if (fragmentNursesListingDetailsBinding?.txtNursedetailsReviewMore?.text!!.equals("Less..")){
                if (initialReviewRatingList!=null && initialReviewRatingList!!.size>0){
                    setReviewRatingListing(initialReviewRatingList)
                    fragmentNursesListingDetailsBinding?.txtNursedetailsReviewMore?.setText("More..")
                }
            }
        })


    }

    // Set up recycler view for service listing if available
    private fun setUpViewNursesFeeslistingRecyclerview(hourlyRatesList: ArrayList<HourlyRatesItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing!= null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterNursesFeesListingRecyclerView(hourlyRatesList,context!!)
        recyclerView.adapter = contactListAdapter

    }

    override fun successNurseDetailsResponse(nurseDetailsResponse: NurseDetailsResponse?) {
        baseActivity?.hideLoading()
        if(nurseDetailsResponse?.code.equals("200")){
            if (nurseDetailsResponse?.result?.firstName!=null && !nurseDetailsResponse?.result?.firstName.equals("")){
                nurseFirstName=nurseDetailsResponse?.result?.firstName
            }else{
                nurseFirstName=""
            }

            if (nurseDetailsResponse?.result?.lastName!=null && !nurseDetailsResponse?.result?.lastName.equals("")){
                nurseLastName=nurseDetailsResponse?.result?.lastName
            }else{
                nurseLastName=""
            }

            fragmentNursesListingDetailsBinding?.txtNursedetaisName?.setText(nurseFirstName+" "+nurseLastName)

            if(nurseDetailsResponse?.result?.qualification!=null && !nurseDetailsResponse?.result?.qualification.equals("")){
                fragmentNursesListingDetailsBinding?.txtNursedetaisQualification?.setText(nurseDetailsResponse?.result?.qualification)
            }else{
                fragmentNursesListingDetailsBinding?.txtNursedetaisQualification?.setText("")
            }

            if(nurseDetailsResponse?.result?.reviewAbility!=null && !nurseDetailsResponse?.result?.reviewAbility.equals("")){
                if(nurseDetailsResponse?.result?.reviewAbility.equals("yes")){
                    fragmentNursesListingDetailsBinding?.txtNursedetaisheaderReviewWrite?.visibility=View.VISIBLE
                    fragmentNursesListingDetailsBinding?.txtNurseSubmitReview?.visibility=View.VISIBLE

                }else if(nurseDetailsResponse?.result?.reviewAbility.equals("no")){
                    fragmentNursesListingDetailsBinding?.txtNursedetaisheaderReviewWrite?.visibility=View.GONE
                    fragmentNursesListingDetailsBinding?.txtNurseSubmitReview?.visibility=View.GONE
                }
            }

            if (nurseDetailsResponse?.result?.avgRating!=null && !nurseDetailsResponse?.result?.avgRating.equals("")){
                fragmentNursesListingDetailsBinding?.ratingBardoctordetailseview?.rating=nurseDetailsResponse?.result?.avgRating.toFloat()
            }else{

            }
            if (nurseDetailsResponse?.result?.reviewRating!=null && nurseDetailsResponse?.result?.reviewRating.size>0){
                fragmentNursesListingDetailsBinding?.txtNursedetailsNoofreview?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.txtNursedetailsNoofreview?.setText(nurseDetailsResponse?.result?.reviewRating.size.toString()+" "+"reviews")
            }else{
                fragmentNursesListingDetailsBinding?.txtNursedetailsNoofreview?.visibility=View.GONE
            }
            if (nurseDetailsResponse?.result?.description!=null && !nurseDetailsResponse?.result?.description.equals("")){
                fragmentNursesListingDetailsBinding?.txtNursedetaisAddressDescription?.setText(nurseDetailsResponse?.result?.description)
            }else{
                fragmentNursesListingDetailsBinding?.txtNursedetaisAddressDescription?.setText("")
            }

            if(nurseDetailsResponse?.result?.hourlyRates!=null && nurseDetailsResponse?.result?.hourlyRates.size>0){
             fragmentNursesListingDetailsBinding?.recyclerViewRootscareNursesfeesListing?.visibility=View.VISIBLE
             fragmentNursesListingDetailsBinding?.tvNoDateNursefeeslist?.visibility=View.GONE
                setUpViewNursesFeeslistingRecyclerview(nurseDetailsResponse?.result?.hourlyRates)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewRootscareNursesfeesListing?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNoDateNursefeeslist?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNoDateNursefeeslist?.setText("No fees found.")
            }

            if(nurseDetailsResponse?.result?.qualificationData!=null && nurseDetailsResponse?.result?.qualificationData.size>0){
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsEducation?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsEducationNoDate?.visibility=View.GONE
                setQualificationDataListing(nurseDetailsResponse?.result?.qualificationData)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsEducation?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNursedetailsEducationNoDate?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsEducationNoDate?.setText("No qualification data found.")
            }

            if(nurseDetailsResponse?.result?.department!=null && nurseDetailsResponse?.result?.department.size>0){
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsSpecility?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsSpecilityNoDate?.visibility=View.GONE
                setSpecilityDataListing(nurseDetailsResponse?.result?.department)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsSpecility?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNursedetailsSpecilityNoDate?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsSpecilityNoDate?.setText("No specility data found.")
            }

            initialReviewRatingList= ArrayList<ReviewRatingItem?>()
            finalReviewRatingList= ArrayList<ReviewRatingItem?>()

            if(nurseDetailsResponse?.result?.reviewRating!=null && nurseDetailsResponse?.result?.reviewRating.size>0){
                fragmentNursesListingDetailsBinding?.recyclerViewNurselistingReview?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNurselistingReviewNoDate?.visibility=View.GONE
                finalReviewRatingList=nurseDetailsResponse?.result?.reviewRating
                if(nurseDetailsResponse?.result?.reviewRating?.size>1){
                    fragmentNursesListingDetailsBinding?.txtNursedetailsReviewMore?.visibility=View.VISIBLE
                    var reviewRatingItem=ReviewRatingItem()
                    reviewRatingItem.rating=nurseDetailsResponse?.result?.reviewRating?.get(0)?.rating
                    reviewRatingItem.review=nurseDetailsResponse?.result?.reviewRating?.get(0)?.review
                    reviewRatingItem.reviewBy=nurseDetailsResponse?.result?.reviewRating?.get(0)?.reviewBy
                    initialReviewRatingList?.add(reviewRatingItem)
                    setReviewRatingListing(initialReviewRatingList)
                }else{
                    fragmentNursesListingDetailsBinding?.txtNursedetailsReviewMore?.visibility=View.GONE
                    finalReviewRatingList= ArrayList<ReviewRatingItem?>()
                    for (i in 0 until nurseDetailsResponse?.result?.reviewRating?.size) {
                        var reviewRatingItem=ReviewRatingItem()
                        reviewRatingItem.rating=nurseDetailsResponse?.result?.reviewRating?.get(0)?.rating
                        reviewRatingItem.review=nurseDetailsResponse?.result?.reviewRating?.get(0)?.review
                        reviewRatingItem.reviewBy=nurseDetailsResponse?.result?.reviewRating?.get(0)?.reviewBy
                        finalReviewRatingList?.add(reviewRatingItem)
                        setReviewRatingListing(finalReviewRatingList)
                    }
                }
              //  setReviewRatingListing(nurseDetailsResponse?.result?.reviewRating)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewNurselistingReview?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNurselistingReviewNoDate?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNurselistingReviewNoDate?.setText("No review found.")
            }

            if(nurseDetailsResponse?.result?.image!=null && !nurseDetailsResponse?.result?.image.equals("")){

                    Glide.with(this!!.activity!!)
                        .load(this!!.activity!!.getString(R.string.api_base)+"uploads/images/" + (nurseDetailsResponse?.result?.image))
                        .into(fragmentNursesListingDetailsBinding?.imgNursedetailsProfilephoto!!)
            }else{
                Glide.with(this!!.activity!!)
                    .load(R.drawable.no_image)
                    .into(fragmentNursesListingDetailsBinding?.imgNursedetailsProfilephoto!!)
            }


        }else{
            Toast.makeText(activity, nurseDetailsResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun successNueseViewTimingsResponse(nueseViewTimingsResponse: NueseViewTimingsResponse?) {
        baseActivity?.hideLoading()
        if(nueseViewTimingsResponse?.code.equals("200")){

            if(nueseViewTimingsResponse?.result!=null && nueseViewTimingsResponse?.result.size>0){
                fragmentNursesListingDetailsBinding?.recyclerViewNursetiming?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNoDateNursetiming?.visibility=View.GONE
                setNurseViewTimingListing(nueseViewTimingsResponse?.result)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewNursetiming?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNoDateNursetiming?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNoDateNursetiming?.setText("No timings found.")
            }

        }else{
            fragmentNursesListingDetailsBinding?.recyclerViewNursetiming?.visibility=View.GONE
            fragmentNursesListingDetailsBinding?.tvNoDateNursetiming?.visibility=View.VISIBLE
            fragmentNursesListingDetailsBinding?.tvNoDateNursetiming?.setText("No timings found.")
        }
    }

    override fun errorNurseDetailsResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentProfile.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    // Set up recycler view for service listing if available
    private fun setQualificationDataListing(qualificationDataList: ArrayList<QualificationDataItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsEducation != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsEducation
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterCaregiverEducationListRecyclerView(qualificationDataList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setSpecilityDataListing(departmentList: ArrayList<DepartmentItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsSpecility != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsSpecility
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterCaregiverSpecilityListRecyclerview(departmentList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setReviewRatingListing(reviewRatingList: ArrayList<ReviewRatingItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewNurselistingReview != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewNurselistingReview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterCaregiverReviewrecyclerview(reviewRatingList,context!!)
        recyclerView.adapter = contactListAdapter
    }
//Setup recyclerview for nurse view timing recyclerview
    private fun setNurseViewTimingListing(nurseTimingList: ArrayList<ResultItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewNursetiming != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewNursetiming
        val gridLayoutManager = GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterCaregiverViewTimingRecyclerview(nurseTimingList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    fun apiHitForNurseDetails(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var nurseDetailsRequest= NurseDetailsRequest()
            nurseDetailsRequest.id=nurseId.toInt()
            nurseDetailsRequest.userId=fragmentNursesListingDetailsViewModel?.appSharedPref?.userId?.toInt()

            fragmentNursesListingDetailsViewModel?.apicaregiverdetails(nurseDetailsRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    fun apiHitForNurseViewTiming(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            val nurseSlotRequest= NurseSlotRequest()
            nurseSlotRequest?.userId=fragmentNursesListingDetailsViewModel?.appSharedPref?.userId
            nurseSlotRequest?.serviceProviderId=nurseId
            nurseSlotRequest?.serviceType="caregiver"
            nurseSlotRequest?.fromDate=currentDate
            nurseSlotRequest?.toDate=currentDate
            fragmentNursesListingDetailsViewModel?.taskbasedslots(nurseSlotRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }
}