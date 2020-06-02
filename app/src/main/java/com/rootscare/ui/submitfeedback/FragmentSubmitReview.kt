package com.rootscare.ui.submitfeedback

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.insertdoctorreviewratingrequest.InsertDoctorReviewRatingRequest
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorreviewsubmitresponse.DoctorReviewRatingSubmiteResponse
import com.rootscare.databinding.FragmentSubmitFeedbackBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.doctorbookingdetails.FragmentDoctorBookingDetails
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.patientbookpaynow.FragmentPatientbookPayNow

class FragmentSubmitReview : BaseFragment<FragmentSubmitFeedbackBinding, FragmentSubmitReviewViewModel>(),
    FragmentSubmitReviewNavigator  {
    private var fragmentSubmitFeedbackBinding: FragmentSubmitFeedbackBinding? = null
    private var fragmentSubmitReviewViewModel: FragmentSubmitReviewViewModel? = null
    private var doctorId:String=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_submit_feedback
    override val viewModel: FragmentSubmitReviewViewModel
        get() {
            fragmentSubmitReviewViewModel =
                ViewModelProviders.of(this).get(FragmentSubmitReviewViewModel::class.java!!)
            return fragmentSubmitReviewViewModel as FragmentSubmitReviewViewModel
        }
    companion object {
        fun newInstance(doctorid: String): FragmentSubmitReview {
            val args = Bundle()
            args.putString("doctorid",doctorid)
            val fragment = FragmentSubmitReview()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentSubmitReviewViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSubmitFeedbackBinding = viewDataBinding
        if (arguments!=null && arguments?.getString("doctorid")!=null){
            doctorId = arguments?.getString("doctorid")!!
            Log.d("Doctor Id", ": " + doctorId )
        }
        fragmentSubmitFeedbackBinding?.btnSubmitReview?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialog(this.activity!!, object :
                DialogClickCallback {
                override fun onDismiss() {
                }

                override fun onConfirm() {

                    submitReviewAndRatingApiCall()

                }

            }, "Submit Review", "Are you sure to submit review ?")
        })
    }

    override fun successDoctorReviewRatingSubmiteResponse(doctorReviewRatingSubmiteResponse: DoctorReviewRatingSubmiteResponse?) {
        baseActivity?.hideLoading()
        if(doctorReviewRatingSubmiteResponse?.code.equals("200")){
            Toast.makeText(activity, doctorReviewRatingSubmiteResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentDoctorListingDetails.newInstance(doctorId))
        }else{
            Toast.makeText(activity, doctorReviewRatingSubmiteResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorDoctorReviewRatingSubmiteResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitReviewAndRatingApiCall(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var insertDoctorReviewRatingRequest= InsertDoctorReviewRatingRequest()

            insertDoctorReviewRatingRequest.userId = fragmentSubmitReviewViewModel!!.appSharedPref!!.userId
            insertDoctorReviewRatingRequest.doctorId=doctorId
            insertDoctorReviewRatingRequest?.rating=fragmentSubmitFeedbackBinding?.ratingBarteacherFeedback?.rating?.toString()
            insertDoctorReviewRatingRequest?.review=fragmentSubmitFeedbackBinding?.edtReattingComment?.text?.toString()?.trim()

            fragmentSubmitReviewViewModel?.apiinsertdoctorreview(insertDoctorReviewRatingRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }
}