package com.rootscare.ui.seealldoctorbygrid

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.interfaces.DropdownRowItemItemClickOnConfirm
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.adapter.CustomDropDownAdapter
import com.rootscare.data.model.api.request.doctorrequest.doctorlistbydepartmentrequest.DoctorListByDepartmentIdRequest
import com.rootscare.data.model.api.request.doctorrequest.doctorsearchrequest.SeeAllDoctorSearch
import com.rootscare.data.model.api.response.doctorallapiresponse.alldoctorlistingresponse.AllDoctorListingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.alldoctorlistingresponse.ResultItem
import com.rootscare.data.model.api.response.doctorallapiresponse.doctordepartmentlistingresponse.DoctorDepartmentListingResponse
import com.rootscare.databinding.FragmentSeeAllDoctorByGridBinding
import com.rootscare.model.RowItem
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.doctorcategorieslisting.FragmentDoctorCategoriesListing
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.seealldoctorbygrid.adapter.AdapterSeeAllDoctorByGridRecyclerView


class FragmentSeeAllDoctorByGrid : BaseFragment<FragmentSeeAllDoctorByGridBinding, FragmentSeeAllDoctorByGridViewModel>(),
    FragmentSeeAllDoctorByGridNavigator {
    private var fragmentSeeAllDoctorByGridBinding: FragmentSeeAllDoctorByGridBinding? = null
    private var fragmentSeeAllDoctorByGridViewModel: FragmentSeeAllDoctorByGridViewModel? = null
    private var hidden = true
    private var selectedSpecialityCodeForFilter: String? = null
    private var selectedSpecialityNameForFilter: String? = null
     var departmentDropdownList:ArrayList<RowItem?>? = null
    var departmentId=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_see_all_doctor_by_grid
    override val viewModel: FragmentSeeAllDoctorByGridViewModel
        get() {
            fragmentSeeAllDoctorByGridViewModel =
                ViewModelProviders.of(this).get(FragmentSeeAllDoctorByGridViewModel::class.java!!)
            return fragmentSeeAllDoctorByGridViewModel as FragmentSeeAllDoctorByGridViewModel
        }

    companion object {
        fun newInstance(): FragmentSeeAllDoctorByGrid {
            val args = Bundle()
            val fragment = FragmentSeeAllDoctorByGrid()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentSeeAllDoctorByGridViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSeeAllDoctorByGridBinding = viewDataBinding
        fragmentSeeAllDoctorByGridBinding?.btnRootscareMoreDoctor?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentDoctorCategoriesListing.newInstance())
        })
        fragmentSeeAllDoctorByGridBinding?.llMain?.setOnClickListener(View.OnClickListener {
//            val inputMethodManager =
//                activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(
//                activity!!.currentFocus!!.windowToken,
//                0
//            )
            baseActivity?.hideKeyboard()
        })
//        val inputMethodManager: InputMethodManager = activity!!.getSystemService(
//            Activity.INPUT_METHOD_SERVICE
//        ) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(
//            activity!!.currentFocus!!.windowToken, 0
//        )


        //All department api call
        if(isNetworkConnected){
            baseActivity?.showLoading()
            fragmentSeeAllDoctorByGridViewModel?.apidepartmentlist()

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        //All Doctor List Api Call
        if(isNetworkConnected){
            baseActivity?.showLoading()
            fragmentSeeAllDoctorByGridViewModel?.apidoctorlist()

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
        fragmentSeeAllDoctorByGridBinding?.txtSelectDoctorDepartment?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForRowItemDropDownList(this!!.activity!!,"Select Department",departmentDropdownList,object:
                DropdownRowItemItemClickOnConfirm {
                override fun onConfirm(title_name: String,title_id:String) {
                    fragmentSeeAllDoctorByGridBinding?.txtSelectDoctorDepartment?.setText(title_name)
                    departmentId=title_id
                    apicalldepartmentdoctorlist(departmentId)
                }
            })
        })

        //Doctor Search Api Call
        fragmentSeeAllDoctorByGridBinding?.txtSeeallDoctorSearch?.setOnClickListener(View.OnClickListener {
            showFilterMenuWithCircularRevealAnimation()

        })

        fragmentSeeAllDoctorByGridBinding?.tvFilterSubmit?.setOnClickListener {
            if(selectedSpecialityCodeForFilter!=null){
                departmentId=selectedSpecialityCodeForFilter.toString()
                apicalldepartmentdoctorlist(departmentId)
            }else{
                fragmentSeeAllDoctorByGridViewModel?.apidoctorlist()
//                Toast.makeText(activity, "Please select any specility.", Toast.LENGTH_SHORT).show()
            }
            showFilterMenuWithCircularRevealAnimation()
        }
        fragmentSeeAllDoctorByGridBinding?.tvFilterClear?.setOnClickListener(View.OnClickListener {
            selectedSpecialityCodeForFilter = null
            selectedSpecialityNameForFilter = null
            fragmentSeeAllDoctorByGridBinding?.spinnerSpeciality?.setSelection(0)
            showFilterMenuWithCircularRevealAnimation()
            if(isNetworkConnected){
                baseActivity?.showLoading()
                fragmentSeeAllDoctorByGridViewModel?.apidoctorlist()

            }else{
                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
            }
        })

//        setupSpecialitySpinner()

        fragmentSeeAllDoctorByGridBinding?.edtSearchDoctor?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) { // you can call or do what you want with your EditText here
// yourEditText...
                if(s.toString().length==0){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        fragmentSeeAllDoctorByGridViewModel?.apidoctorlist()

                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
             //   if(s.toString().length>3){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        var seeAllDoctorSearch= SeeAllDoctorSearch()
                        seeAllDoctorSearch?.searchContent=fragmentSeeAllDoctorByGridBinding?.edtSearchDoctor?.text?.toString()
                        fragmentSeeAllDoctorByGridViewModel?.apisearchdoctor(seeAllDoctorSearch)
                    }
                //}
            }
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllDoctorByGridlistingRecyclerview(allDoctorList: ArrayList<ResultItem?>?) {
        assert(fragmentSeeAllDoctorByGridBinding!!.recyclerViewRootscareSeealldoctorsByGrid != null)
        val recyclerView = fragmentSeeAllDoctorByGridBinding!!.recyclerViewRootscareSeealldoctorsByGrid
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterSeeAllDoctorByGridRecyclerView(allDoctorList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {

            override fun onFirstItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorProfile.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
               (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                   FragmentDoctorListingDetails.newInstance(id.toString()))
            }

        }


    }

    override fun successDoctorDepartmentListingResponse(doctorDepartmentListingResponse: DoctorDepartmentListingResponse?) {
        baseActivity?.hideLoading()
        if(doctorDepartmentListingResponse?.code.equals("200")){

            if(doctorDepartmentListingResponse?.result!=null && doctorDepartmentListingResponse?.result.size>0){




                departmentDropdownList= ArrayList<RowItem?>()
                departmentDropdownList?.add(RowItem("Select Specility","0"))
                for (i in 0 until doctorDepartmentListingResponse?.result?.size) {
                    val item = RowItem(doctorDepartmentListingResponse?.result?.get(i)?.title!!,
                        doctorDepartmentListingResponse?.result?.get(i)?.id.toString()
                    )
                    departmentDropdownList?.add(item)
                }

            }
            setupSpecialitySpinner()
        }else{
            Toast.makeText(activity, doctorDepartmentListingResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }



    /* Filter Start */

    override fun successAllDoctorListingResponse(allDoctorListingResponse: AllDoctorListingResponse?) {
        baseActivity?.hideLoading()
        if(allDoctorListingResponse?.code.equals("200")){
            if(allDoctorListingResponse?.result!=null && allDoctorListingResponse?.result.size>0){
                fragmentSeeAllDoctorByGridBinding?.recyclerViewRootscareSeealldoctorsByGrid?.visibility=View.VISIBLE
                fragmentSeeAllDoctorByGridBinding?.tvNoDate?.visibility=View.GONE
                setUpViewSeeAllDoctorByGridlistingRecyclerview(allDoctorListingResponse?.result)
            }  else{
                fragmentSeeAllDoctorByGridBinding?.recyclerViewRootscareSeealldoctorsByGrid?.visibility=View.GONE
                fragmentSeeAllDoctorByGridBinding?.tvNoDate?.visibility=View.VISIBLE
                fragmentSeeAllDoctorByGridBinding?.tvNoDate?.setText("No doctor list found")
            }
        }else{
            fragmentSeeAllDoctorByGridBinding?.recyclerViewRootscareSeealldoctorsByGrid?.visibility=View.GONE
            fragmentSeeAllDoctorByGridBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentSeeAllDoctorByGridBinding?.tvNoDate?.setText(allDoctorListingResponse?.message)
//            Toast.makeText(activity, allDoctorListingResponse?.message, Toast.LENGTH_SHORT).show()
        }


    }

    override fun errorDoctorDepartmentListingResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun apicalldepartmentdoctorlist(departmentid:String){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var doctorListByDepartmentIdRequest=DoctorListByDepartmentIdRequest()
            doctorListByDepartmentIdRequest?.departmentId=departmentid
            fragmentSeeAllDoctorByGridViewModel?.apidepartmentdoctorlist(doctorListByDepartmentIdRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }


    // Circular Reveal Animation for filter layout


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showFilterMenuWithCircularRevealAnimation() {
        with(fragmentSeeAllDoctorByGridBinding!!) {
            val cx: Int = filterMenuContainerCardView.left + filterMenuContainerCardView.right -  com.rootscare.utils.ViewUtils.dpToPx(30f)
            val cy = 0
            val radius: Int = java.lang.Math.max(filterMenuContainerCardView.width, filterMenuContainerCardView.height)
            if (hidden) {
                val anim = android.view.ViewAnimationUtils.createCircularReveal(filterMenuContainerCardView, cx, cy, 0f, radius.toFloat())
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        filterMenuContainerCardView.visibility = android.view.View.VISIBLE
//                        commonYellowToolbar.common_toolbar_parent_layout.foreground = ColorDrawable(ContextCompat.getColor(this@ProviderListingActivity, R.color.transparentBlack))
                       // commonYellowToolbar.transparent_black_view_for_toolbar.visibility = android.view.View.VISIBLE
                        transparentBlackViewForSearch2.visibility = android.view.View.GONE
                        transparentBlackViewForSearch.visibility = android.view.View.GONE
                        transparentBlackViewForContent.visibility = android.view.View.GONE
                        hidden = false
                    }
                })
                anim.start()
            } else {
                val anim = android.view.ViewAnimationUtils.createCircularReveal(filterMenuContainerCardView, cx, cy, radius.toFloat(), 0f)
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        filterMenuContainerCardView.visibility = android.view.View.GONE
//                        commonYellowToolbar.common_toolbar_parent_layout.foreground = null
//                        commonYellowToolbar.transparent_black_view_for_toolbar.visibility = android.view.View.GONE
                        transparentBlackViewForSearch2.visibility = android.view.View.GONE
                        transparentBlackViewForSearch.visibility = android.view.View.GONE
                        transparentBlackViewForContent.visibility = android.view.View.GONE
                        hidden = true
                    }
                })
                anim.start()
            }
        }
    }
    private fun setupSpecialitySpinner() {
//        var specialityResponseModelForSihatku = Gson().fromJson(clinicsViewModel?.appSharedPref?.sihatkuSectionAllSepeciality, SihatkuSpecialitiesResponseModel::class.java)
        if (departmentDropdownList != null) {
//            var dataList: LinkedList<SihatkuSpecialitiesSubModel> = specialityResponseModelForSihatku.dataList!!
//            dataList.addFirst(SihatkuSpecialitiesSubModel(SpecialityName = resources.getString(R.string.unselect)))
            var spinnerAdapter: CustomDropDownAdapter = CustomDropDownAdapter(context!!, departmentDropdownList!!)
            fragmentSeeAllDoctorByGridBinding?.spinnerSpeciality?.adapter = spinnerAdapter

//            val customSpinnerAdapter = CustomSpinnerAdapter(this@ClinicsActivity, dataList)
//            fragmentSeeAllDoctorByGridBinding?.spinnerSpeciality?.adapter = customSpinnerAdapter
            fragmentSeeAllDoctorByGridBinding?.spinnerSpeciality?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    if (position == 0) {
                        selectedSpecialityCodeForFilter = null
                        selectedSpecialityNameForFilter = null
                    } else {
                        selectedSpecialityCodeForFilter = departmentDropdownList?.get(position)?.item_id
                        selectedSpecialityNameForFilter = departmentDropdownList?.get(position)?.title_item
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            fragmentSeeAllDoctorByGridBinding?.spinnerSpeciality?.setSelection(0)
        }
    }
}