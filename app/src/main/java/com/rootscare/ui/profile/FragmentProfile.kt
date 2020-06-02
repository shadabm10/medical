package com.rootscare.ui.profile

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dialog.CommonDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.interfaces.DropDownDialogCallBack
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.patientprofilerequest.PatientProfileRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilelifestylerequest.ProfileLifestyleUpdateRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilemedicalrequest.ProfileMedicalUpdateRequest
import com.rootscare.data.model.api.response.patientprofileresponse.PatientProfileResponse
import com.rootscare.databinding.FragmentProfileBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.login.subfragment.registrationfragment.FragmentRegistration
import com.rootscare.utils.ManagePermissions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*

class FragmentProfile : BaseFragment<FragmentProfileBinding, FragmentProfileViewModel>(),
    FragmentProfileNavigator {
    private var fragmentProfileBinding: FragmentProfileBinding? = null
    private var fragmentProfileViewModel: FragmentProfileViewModel? = null
    var monthstr: String=""
    var dayStr: String=""
    var imagefilename=""

    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    var imageFile: File? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_profile
    override val viewModel: FragmentProfileViewModel
        get() {
            fragmentProfileViewModel =
                ViewModelProviders.of(this).get(FragmentProfileViewModel::class.java!!)
            return fragmentProfileViewModel as FragmentProfileViewModel
        }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        val TAG = FragmentRegistration::class.java.simpleName
        fun newInstance(): FragmentProfile {
            val args = Bundle()
            val fragment = FragmentProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentProfileBinding = viewDataBinding
        setUpTabLayout()
        fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.setText("1")
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this!!.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

        if(isNetworkConnected){
            baseActivity?.showLoading()
            var patientProfileRequest= PatientProfileRequest()
            patientProfileRequest?.userId=fragmentProfileViewModel?.appSharedPref?.userId

//            patientProfileRequest?.userId="11"
            fragmentProfileViewModel?.apipatientprofile(patientProfileRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalDob?.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this!!.activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                if((monthOfYear+1)<10){
                    monthstr= "0" + (monthOfYear+1)
                }else{
                    monthstr=(monthOfYear+1).toString()
                }

                if(dayOfMonth<10){
                    dayStr="0"+dayOfMonth

                }else{
                    dayStr=dayOfMonth.toString()
                }
//                fragmentSeedStockedBinding?.txtLsfSeedstockDateofStocking?.setText("" + year + "-" + monthstr + "-" + dayStr)
                fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalDob?.setText("" + year + "-" + monthstr + "-" + dayStr)

            }, year, month, day)

            dpd.show()
        })

        radioButtonClickEvenOfMedical()

        fragmentProfileBinding?.edtProfileImage?.setOnClickListener({
            showPictureDialog()
        })

        fragmentProfileBinding?.layoutProfileMedical?.btnPatientProfileMedical?.setOnClickListener(
            View.OnClickListener {

                if(isNetworkConnected){
                    baseActivity?.showLoading()
                    var profileMedicalUpdateRequest= ProfileMedicalUpdateRequest()
                    if (fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.allergies="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.allergies="no"
                    }

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.isChecked!!){
                        profileMedicalUpdateRequest?.currentMedication="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.isChecked!!){
                        profileMedicalUpdateRequest?.currentMedication="no"
                    }

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.isChecked!!){
                        profileMedicalUpdateRequest?.pastMedication="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.isChecked!!){
                        profileMedicalUpdateRequest?.pastMedication="no"
                    }

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.injuries="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.injuries="no"
                    }

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.surgeries="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.surgeries="no"
                    }

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.chronicDiseases="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.chronicDiseases="no"
                    }
                    profileMedicalUpdateRequest?.userId=fragmentProfileViewModel?.appSharedPref?.userId
                    fragmentProfileViewModel?.apieditpatientprofilemedical(profileMedicalUpdateRequest)

                }else{
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }

            })

        fragmentProfileBinding?.layoutProfileLifestyle?.btnPatientProfileLifestyle?.setOnClickListener(
            View.OnClickListener {
                if(checkFieldValidation()){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        var profileLifestyleUpdateRequest= ProfileLifestyleUpdateRequest()
                        profileLifestyleUpdateRequest?.smoking=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.text?.toString()
                        profileLifestyleUpdateRequest?.alcohol=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.text?.toString()
                        profileLifestyleUpdateRequest?.bloodGroup=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.text?.toString()
                        profileLifestyleUpdateRequest?.activityLevel=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.text?.toString()
                        profileLifestyleUpdateRequest?.foodPreference=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.text?.toString()
                        profileLifestyleUpdateRequest?.occupation=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.text?.toString()
                        profileLifestyleUpdateRequest?.userId=fragmentProfileViewModel?.appSharedPref?.userId
                        fragmentProfileViewModel?.apieditpatientprofilestyle(profileLifestyleUpdateRequest)
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }

            })

        fragmentProfileBinding?.layoutProfilePersonal?.btnPatientProfilePersonal?.setOnClickListener(
            View.OnClickListener {
                if(checkFieldValidationForPersonalDetails()){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        apieditpatientprofilepersonalApiCall()
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        //Life Style Smooking Habits Dropdown Click

        lifeStyleDropdownClick()

        personalDropdownClick()


    }

    private fun setUpTabLayout() {
        val tabTitles: MutableList<String> =
            ArrayList()
        tabTitles.add("Personal")
        tabTitles.add("Medical")
        tabTitles.add("Life style")
        for (i in tabTitles.indices) {
            fragmentProfileBinding?.tablayout?.addTab(
                fragmentProfileBinding?.tablayout?.newTab()?.setText(
                    tabTitles[i]
                )!!, i
            )
        }
        //        activityOrderBinding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (i in 0 until fragmentProfileBinding?.tablayout?.getTabCount()!!) {
            val view: View =
                LayoutInflater.from(activity).inflate(R.layout.tab_item_layout, null)
            val tab_item_tv = view.findViewById<TextView>(R.id.tab_item_tv)
            tab_item_tv.text = tabTitles[i]
            if (i == 0) {
                tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
                view.background = resources.getDrawable(R.drawable.tab_background_selected)
            } else {
                tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
//                tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                view.background = resources.getDrawable(R.drawable.tab_background_unselected)
            }
            fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView = view
        }
        fragmentProfileBinding?.tablayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until fragmentProfileBinding?.tablayout?.getTabCount()!!) {
//                    val view: View = fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView!!
                    val view: View = Objects.requireNonNull<View>(
                        fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView
                    )

                    val tab_item_tv =
                        Objects.requireNonNull(view)
                            .findViewById<TextView>(R.id.tab_item_tv)
                    if (i == tab.position) {
                        tab_item_tv.setTextColor(resources.getColor(R.color.background_white))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_selected)
                    } else {
                        tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
//                        tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_unselected)
                    }
                }
                if (tab.position == 0) {
                    fragmentProfileBinding?.llProfilePersonal?.visibility=View.VISIBLE
                    fragmentProfileBinding?.llProfileMedical?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileLifestyle?.visibility=View.GONE
//                    addFragment(
//                        PendingFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                } else if (tab.position == 1) {
                    fragmentProfileBinding?.llProfilePersonal?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileMedical?.visibility=View.VISIBLE
                    fragmentProfileBinding?.llProfileLifestyle?.visibility=View.GONE
//                    addFragment(
//                        ConfirmFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                } else if (tab.position == 2) {
                    fragmentProfileBinding?.llProfilePersonal?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileMedical?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileLifestyle?.visibility=View.VISIBLE
//                    addFragment(
//                        DeliveredFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        fragmentProfileBinding?.llProfilePersonal?.visibility=View.VISIBLE
        fragmentProfileBinding?.llProfileMedical?.visibility=View.GONE
        fragmentProfileBinding?.llProfileLifestyle?.visibility=View.GONE

    }

    override fun successPatientProfileResponse(patientProfileResponse: PatientProfileResponse?) {
        baseActivity?.hideLoading()
        if(patientProfileResponse?.code.equals("200")){
            fragmentProfileBinding?.txtProfileName?.setText(patientProfileResponse?.result?.firstName+" "+patientProfileResponse?.result?.lastName)
            fragmentProfileBinding?.txtProfileEmail?.setText(patientProfileResponse?.result?.email)
            fragmentProfileBinding?.txtProfileContactnumner?.setText(patientProfileResponse?.result?.phoneNumber)
            Toast.makeText(activity, patientProfileResponse?.message, Toast.LENGTH_SHORT).show()
            fragmentProfileViewModel?.appSharedPref?.userImage=patientProfileResponse?.result?.image
            defaultPersonalDetailsSetup(patientProfileResponse)
            defaultMedicalDetailsSetup(patientProfileResponse)
            defaultRadioButtonSetup()
            defaultLifeStylelDetailsSetup(patientProfileResponse)

        }else{
            Toast.makeText(activity, patientProfileResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorPatientProfileResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentProfile.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun defaultPersonalDetailsSetup(patientProfileResponse: PatientProfileResponse?){

        if(!patientProfileResponse?.result?.firstName.equals("") || patientProfileResponse?.result?.firstName!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalFname?.setText(patientProfileResponse?.result?.firstName)
        }

        if(!patientProfileResponse?.result?.lastName.equals("") || patientProfileResponse?.result?.lastName!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLname?.setText(patientProfileResponse?.result?.lastName)
        }

        if(!patientProfileResponse?.result?.phoneNumber.equals("") || patientProfileResponse?.result?.phoneNumber!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalPhonenumber?.setText(patientProfileResponse?.result?.phoneNumber)
        }

        if(!patientProfileResponse?.result?.email.equals("") || patientProfileResponse?.result?.email!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalEmail?.setText(patientProfileResponse?.result?.email)
        }

        if(!patientProfileResponse?.result?.location.equals("") || patientProfileResponse?.result?.location!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLocation?.setText(patientProfileResponse?.result?.location)
        }

        if(!patientProfileResponse?.result?.dob.equals("") || patientProfileResponse?.result?.dob!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalDob?.setText(patientProfileResponse?.result?.dob)
        }

        if(!patientProfileResponse?.result?.age.equals("") || patientProfileResponse?.result?.age!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalAge?.setText(patientProfileResponse?.result?.age)
        }

        if(!patientProfileResponse?.result?.address.equals("") || patientProfileResponse?.result?.address!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalAddress?.setText(patientProfileResponse?.result?.address)
        }

        if(!patientProfileResponse?.result?.idNumber.equals("") || patientProfileResponse?.result?.idNumber!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalIdnumber?.setText(patientProfileResponse?.result?.idNumber)
        }

//
        if(!patientProfileResponse?.result?.image.equals("") || patientProfileResponse?.result?.image!=null){
            imagefilename= patientProfileResponse?.result?.image!!
            Glide.with(this!!.activity!!)
                .load(activity?.getString(R.string.api_base)+"uploads/images/" + (patientProfileResponse?.result?.image))
                .into(fragmentProfileBinding?.imgRootscareProfileImage!!)
        }else{
            imagefilename=""
            Glide.with(this!!.activity!!)
                .load(  this.getResources().getDrawable(R.drawable.profile_no_image))
                .into(fragmentProfileBinding?.imgRootscareProfileImage!!)
        }


    }

    private fun defaultMedicalDetailsSetup(patientProfileResponse: PatientProfileResponse?){
        if(patientProfileResponse?.result?.allergies.equals("") || patientProfileResponse?.result?.allergies!=null){
            if(patientProfileResponse?.result?.allergies.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.allergies.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.isChecked=true
            }
        }

        if(patientProfileResponse?.result?.currentMedication.equals("") || patientProfileResponse?.result?.currentMedication!=null){
            if(patientProfileResponse?.result?.currentMedication.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.isChecked=true
            }else if(patientProfileResponse?.result?.currentMedication.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.isChecked=true
            }
        }

        if(patientProfileResponse?.result?.pastMedication.equals("") || patientProfileResponse?.result?.pastMedication!=null){
            if(patientProfileResponse?.result?.pastMedication.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.isChecked=true
            }else if(patientProfileResponse?.result?.pastMedication.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.isChecked=true
            }
        }

        if(patientProfileResponse?.result?.injuries.equals("") || patientProfileResponse?.result?.injuries!=null){
            if(patientProfileResponse?.result?.injuries.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.injuries.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.isChecked=true
            }
        }

        if(patientProfileResponse?.result?.surgeries.equals("") || patientProfileResponse?.result?.surgeries!=null){
            if(patientProfileResponse?.result?.surgeries.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.surgeries.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.isChecked=true
            }
        }

        if(patientProfileResponse?.result?.chronicDiseases.equals("") || patientProfileResponse?.result?.chronicDiseases!=null){
            if(patientProfileResponse?.result?.chronicDiseases.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.chronicDiseases.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.isChecked=true
            }
        }


    }

    private fun defaultRadioButtonSetup(){
        if (fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.GONE
        }
    }

    private  fun radioButtonClickEvenOfMedical(){
        fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.GONE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.GONE
        })
    }

    private fun defaultLifeStylelDetailsSetup(patientProfileResponse: PatientProfileResponse?){
        if(!patientProfileResponse?.result?.smoking.equals("") || patientProfileResponse?.result?.smoking!=null){
           fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.setText(patientProfileResponse?.result?.smoking)
        }
        if(!patientProfileResponse?.result?.alcohol.equals("") || patientProfileResponse?.result?.alcohol!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.setText(patientProfileResponse?.result?.alcohol)
        }
        if(!patientProfileResponse?.result?.bloodGroup.equals("") || patientProfileResponse?.result?.bloodGroup!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.setText(patientProfileResponse?.result?.bloodGroup)
        }
        if(!patientProfileResponse?.result?.activityLevel.equals("") || patientProfileResponse?.result?.activityLevel!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.setText(patientProfileResponse?.result?.activityLevel)
        }
        if(!patientProfileResponse?.result?.foodPreference.equals("") || patientProfileResponse?.result?.foodPreference!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.setText(patientProfileResponse?.result?.foodPreference)
        }
        if(!patientProfileResponse?.result?.occupation.equals("") || patientProfileResponse?.result?.occupation!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.setText(patientProfileResponse?.result?.occupation)
        }

    }


    //IMAGE SELECTION AND GET IMAGE PATH

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    bitmapToFile(bitmap)
                    Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()

                    fragmentProfileBinding?.imgRootscareProfileImage?.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            fragmentProfileBinding?.imgRootscareProfileImage?.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            bitmapToFile(thumbnail)
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + FragmentProfile.IMAGE_DIRECTORY)

        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            //     File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");


            f.createNewFile()
            // imageFile=f

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())


            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    Toast.makeText(activity, "Permissions granted.", Toast.LENGTH_SHORT).show()
                    //  toast("Permissions granted.")
                } else {
                    Toast.makeText(activity, "Permissions denied.", Toast.LENGTH_SHORT).show()
                    // toast("Permissions denied.")
                }
                return
            }
        }
    }
    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(activity)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        imageFile = file



        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //updateProfileImageApiCall(imageFile!!)
        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }



    private fun checkFieldValidation(): Boolean {
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.text?.toString())) {
            Toast.makeText(activity, "Please enter your smoking habits", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.text?.toString())) {
            Toast.makeText(activity, "Please enter your alcohol habits", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.text?.toString())) {
            Toast.makeText(activity, "Please enter your blood group", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.text?.toString())) {
            Toast.makeText(activity, "Please enter your activity level", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.text?.toString())) {
            Toast.makeText(activity, "Please enter your foodPre ference", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.text?.toString())) {
            Toast.makeText(activity, "Please enter your occupation", Toast.LENGTH_SHORT).show()
            return false
        }
//


        return true
    }

    private fun checkFieldValidationForPersonalDetails(): Boolean {
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalFname?.text?.toString())) {
            Toast.makeText(activity, "Please enter your first name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLname?.text?.toString())) {
            Toast.makeText(activity, "Please enter your last name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalIdnumber?.text?.toString())) {
            Toast.makeText(activity, "Please enter your id number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.text?.toString())) {
            Toast.makeText(activity, "Please select your accout status", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    private fun apieditpatientprofilepersonalApiCall(){
        baseActivity?.showLoading()
        val userId = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileViewModel!!.appSharedPref!!.userId)
        val first_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalFname?.text?.toString())
        val last_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLname?.text?.toString())
        val id_number = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalIdnumber?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"),"1")
        if (imageFile != null) {
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            var multipartBody = MultipartBody.Part.createFormData("image", imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,multipartBody)

        } else{
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), "")
            var multipartBody = MultipartBody.Part.createFormData("image", imagefilename, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,multipartBody)
            //Toast.makeText(activity, "Image can not be blank", Toast.LENGTH_SHORT).show()
        }
    }

    private fun lifeStyleDropdownClick(){
        var smokingHabitsDropdownlist= ArrayList<String?>()
        smokingHabitsDropdownlist.add("Yes")
        smokingHabitsDropdownlist.add("No")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Smoking Habits",smokingHabitsDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.setText(text)
                }

            })
        })

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Alcohol Habits",smokingHabitsDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.setText(text)
                }

            })
        })

        var bloodGroupDropdownlist= ArrayList<String?>()
        bloodGroupDropdownlist.add("A+")
        bloodGroupDropdownlist.add("O+")
        bloodGroupDropdownlist.add("B+")
        bloodGroupDropdownlist.add("AB+")
        bloodGroupDropdownlist.add("A-")
        bloodGroupDropdownlist.add("O-")
        bloodGroupDropdownlist.add("B-")
        bloodGroupDropdownlist.add("AB-")
        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Blood Group",bloodGroupDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.setText(text)
                }

            })
        })
        var activityLevelDropdownlist= ArrayList<String?>()
        activityLevelDropdownlist.add("Extremely inactive")
        activityLevelDropdownlist.add("Sedentary")
        activityLevelDropdownlist.add("Moderately active")
        activityLevelDropdownlist.add("Vigorously active")
        activityLevelDropdownlist.add("Extremely active")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Activity Level",activityLevelDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.setText(text)
                }

            })
        })

        var foodPreferanceDropdownlist= ArrayList<String?>()
        foodPreferanceDropdownlist.add("Standard")
        foodPreferanceDropdownlist.add("Pescetarian")
        foodPreferanceDropdownlist.add("Vegetarian")
        foodPreferanceDropdownlist.add("Lacto-vegetarian")
        foodPreferanceDropdownlist.add("Vegan")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Food Preference",foodPreferanceDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.setText(text)
                }

            })
        })

        var occupationDropdownlist= ArrayList<String?>()
        occupationDropdownlist.add("Technician")
        occupationDropdownlist.add("Teacher")
        occupationDropdownlist.add("Machinist")
        occupationDropdownlist.add("Radiographer")
        occupationDropdownlist.add("Technologist")
        occupationDropdownlist.add("Electrician")
        occupationDropdownlist.add("Engineering technician")
        occupationDropdownlist.add("Actuary")
        occupationDropdownlist.add("Tradesman")
        occupationDropdownlist.add("Medical laboratory scientist")
        occupationDropdownlist.add("Quantity surveyor")
        occupationDropdownlist.add("Prosthetist")
        occupationDropdownlist.add("Paramedic")
        occupationDropdownlist.add("Bricklayer")
        occupationDropdownlist.add("Special Education Teacher")
        occupationDropdownlist.add("Lawyer")
        occupationDropdownlist.add("Physician")
        occupationDropdownlist.add("Other")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Occupation",occupationDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.setText(text)
                }

            })
        })
    }
    private fun personalDropdownClick(){
        var nationalityDropdownlist= ArrayList<String?>()
        nationalityDropdownlist.add("British")
        nationalityDropdownlist.add("Scottish")
        nationalityDropdownlist.add("Irish")
        nationalityDropdownlist.add("Welsh")
        nationalityDropdownlist.add("Danish")
        nationalityDropdownlist.add("Finnish")
        nationalityDropdownlist.add("Norwegian")
        nationalityDropdownlist.add("Swedish")
        nationalityDropdownlist.add("Swiss")
        nationalityDropdownlist.add("Estonian")
        nationalityDropdownlist.add("Latvian")
        nationalityDropdownlist.add("Lithuanian")
        nationalityDropdownlist.add("Austrian")
        nationalityDropdownlist.add("Belgian")
        nationalityDropdownlist.add("French")
        nationalityDropdownlist.add("German")
        nationalityDropdownlist.add("Italian")
        nationalityDropdownlist.add("Dutch")
        nationalityDropdownlist.add("American")
        nationalityDropdownlist.add("Indian")
        nationalityDropdownlist.add("Canadian")
        nationalityDropdownlist.add("Mexican")
        nationalityDropdownlist.add("Ukrainian")
        nationalityDropdownlist.add("Russian")
        nationalityDropdownlist.add("Belarusian")
        nationalityDropdownlist.add("Polish")
        nationalityDropdownlist.add("Czech")
        nationalityDropdownlist.add("Slovak / Slovakian")
        nationalityDropdownlist.add("Hungarian")
        nationalityDropdownlist.add("Romanian")
        nationalityDropdownlist.add("Bulgarian")
        nationalityDropdownlist.add("Greek")
        nationalityDropdownlist.add("Spanish")

        fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalNationality?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Nationality",nationalityDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalNationality?.setText(text)
                }

            })
        })

        var materialStatusDropdownlist= ArrayList<String?>()
        materialStatusDropdownlist.add("Married")
        materialStatusDropdownlist.add("Widowed")
        materialStatusDropdownlist.add("Separated")
        materialStatusDropdownlist.add("Divorced")
        materialStatusDropdownlist.add("Single")

        fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalMaterialStatus?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Material Status",materialStatusDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalMaterialStatus?.setText(text)
                }

            })
        })


    }




}