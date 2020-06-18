package com.rootscare.ui.bookingappointment.subfragment.editpatient

import android.Manifest
import android.app.AlertDialog
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
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.addpatientresponse.AddPatientResponse
import com.rootscare.data.model.api.response.editpatientfamilymemberresponse.EditFamilyMemberResponse
import com.rootscare.databinding.FragmentEditPatientFamilyMemberBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.FragmentBookingAppointment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.utils.ManagePermissions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*

class FragmentEditPatientFamilyMember : BaseFragment<FragmentEditPatientFamilyMemberBinding, FragmentEditPatientFamilyMemberViewModel>(),
    FragmentEditPatientFamilyMemberNavigator {
    private var fragmentEditPatientFamilyMemberBinding: FragmentEditPatientFamilyMemberBinding? = null
    private var fragmentEditPatientFamilyMemberViewModel: FragmentEditPatientFamilyMemberViewModel? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    private var selectedGender=""
    var imageFile: File? = null
    private var doctorId:String=""
    private var patientId:String=""
    private var patientimage:String=""
    private var patientFirstName:String=""
    private var patientLastname:String=""
    private var patientEmail:String=""
    private var patientPhonenumber:String=""
    private var patientAge:String=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_edit_patient_family_member
    override val viewModel: FragmentEditPatientFamilyMemberViewModel
        get() {
            fragmentEditPatientFamilyMemberViewModel =
                ViewModelProviders.of(this).get(FragmentEditPatientFamilyMemberViewModel::class.java!!)
            return fragmentEditPatientFamilyMemberViewModel as FragmentEditPatientFamilyMemberViewModel
        }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(doctorid: String,id:String,imagename: String,firstname: String,lastname: String,email:String,phoneno:String,age:String,gender:String): FragmentEditPatientFamilyMember {
            val args = Bundle()
            args.putString("doctorid",doctorid)
            args.putString("id",id)
            args.putString("imagename",imagename)
            args.putString("firstname",firstname)
            args.putString("lastname",lastname)
            args.putString("email",email)
            args.putString("phoneno",phoneno)
            args.putString("age",age)
            args.putString("gender",gender)


            val fragment = FragmentEditPatientFamilyMember()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentEditPatientFamilyMemberViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentEditPatientFamilyMemberBinding = viewDataBinding
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this!!.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()


        if (arguments!=null && arguments?.getString("doctorid")!=null){
            doctorId = arguments?.getString("doctorid")!!
            Log.d("Doctor Id", ": " + doctorId )
        }

        if (arguments!=null && arguments?.getString("id")!=null){
            patientId = arguments?.getString("id")!!
            Log.d("patientId", ": " + patientId )
        }

        if (arguments!=null && arguments?.getString("imagename")!=null){
            patientimage = arguments?.getString("imagename")!!
            Log.d("patientimage", ": " + patientimage )
        }
        if (arguments!=null && arguments?.getString("firstname")!=null){
            patientFirstName = arguments?.getString("firstname")!!
            Log.d("patientFirstName", ": " + patientFirstName )
        }
        if (arguments!=null && arguments?.getString("lastname")!=null){
            patientLastname = arguments?.getString("lastname")!!
            Log.d("patientLastname", ": " + patientLastname )
        }
        if (arguments!=null && arguments?.getString("email")!=null){
            patientEmail = arguments?.getString("email")!!
            Log.d("patientEmail", ": " + patientEmail )
        }
        if (arguments!=null && arguments?.getString("phoneno")!=null){
            patientPhonenumber = arguments?.getString("phoneno")!!
            Log.d("patientPhonenumber", ": " + patientPhonenumber )
        }
        if (arguments!=null && arguments?.getString("age")!=null){
            patientAge = arguments?.getString("age")!!
            Log.d("Doctor Id", ": " + patientAge )
        }
        if (arguments!=null && arguments?.getString("gender")!=null){
            selectedGender = arguments?.getString("gender")!!
            Log.d("Doctor Id", ": " + selectedGender )
        }

        fragmentEditPatientFamilyMemberBinding?.edtPatientProfileImage?.setOnClickListener(View.OnClickListener {
            showPictureDialog()
        })
        fragmentEditPatientFamilyMemberBinding?.radioPatientGenderFemale?.setOnClickListener(View.OnClickListener {
            selectedGender="Female"
        })

        fragmentEditPatientFamilyMemberBinding?.radioPatientGenderMale?.setOnClickListener(View.OnClickListener {
            selectedGender="Male"
        })

        fragmentEditPatientFamilyMemberBinding?.btnRootscareAddpatientForDoctorBooking?.setOnClickListener(
            View.OnClickListener {
                if(checkFieldValidationForPersonalDetails()){
                    apieditpatientfamilyApiCall()
                }

            })


        if (patientFirstName!=null && !patientFirstName.equals("")){
            fragmentEditPatientFamilyMemberBinding?.edtAddpatientFname?.setText(patientFirstName)
        }else{
            patientFirstName=""
        }

        if (patientLastname!=null && !patientLastname.equals("")){
            fragmentEditPatientFamilyMemberBinding?.edtAddpatientLname?.setText(patientFirstName)
        }else{
            patientLastname=""
        }
        if (patientEmail!=null && !patientEmail.equals("")){
            fragmentEditPatientFamilyMemberBinding?.edtAddpatientEmail?.setText(patientEmail)
        }else{
            patientEmail=""
        }

        if (patientPhonenumber!=null && !patientPhonenumber.equals("")){
            fragmentEditPatientFamilyMemberBinding?.edtAddpatientPhonenumber?.setText(patientPhonenumber)
        }else{
            patientPhonenumber=""
        }

        if (patientAge!=null && !patientAge.equals("")){
            fragmentEditPatientFamilyMemberBinding?.edtAddpatientAge?.setText(patientAge)
        }else{
            patientAge=""
        }

        if (selectedGender!=null && !selectedGender.equals("")){
            if (selectedGender.equals("Female")){
                fragmentEditPatientFamilyMemberBinding?.radioPatientGenderFemale?.isChecked=true
            }else if(selectedGender.equals("Male")){
                fragmentEditPatientFamilyMemberBinding?.radioPatientGenderMale?.isChecked=true
            }

        }else{
            selectedGender=""
        }
        if(patientimage!=null && !patientimage.equals("")){
            Log.d("PROFILE IMAGE---->", "http://166.62.54.122/rootscare/"+"uploads/images/" + (patientimage))
            Glide.with(this!!.activity!!)
                .load("http://166.62.54.122/rootscare/"+"uploads/images/" + (patientimage))
                .into(fragmentEditPatientFamilyMemberBinding?.imgRootscarePatientProfileImage!!)
        }else{
            patientimage=""
            Glide.with(this!!.activity!!)
                .load(  this.getResources().getDrawable(R.drawable.profile_no_image))
                .into(fragmentEditPatientFamilyMemberBinding?.imgRootscarePatientProfileImage!!)
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

                    fragmentEditPatientFamilyMemberBinding?.imgRootscarePatientProfileImage?.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            fragmentEditPatientFamilyMemberBinding?.imgRootscarePatientProfileImage?.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            bitmapToFile(thumbnail)
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + FragmentEditPatientFamilyMember.IMAGE_DIRECTORY)

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

    private fun checkFieldValidationForPersonalDetails(): Boolean {
        if (TextUtils.isEmpty(fragmentEditPatientFamilyMemberBinding?.edtAddpatientFname?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient first name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentEditPatientFamilyMemberBinding?.edtAddpatientLname?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient last name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentEditPatientFamilyMemberBinding?.edtAddpatientEmail?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentEditPatientFamilyMemberBinding?.edtAddpatientPhonenumber?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient phone number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentEditPatientFamilyMemberBinding?.edtAddpatientAge?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient age", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun apieditpatientfamilyApiCall(){
        baseActivity?.showLoading()
        val patient_id = RequestBody.create(MediaType.parse("multipart/form-data"), patientId)
        val first_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentEditPatientFamilyMemberBinding?.edtAddpatientFname?.text?.toString())
        val last_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentEditPatientFamilyMemberBinding?.edtAddpatientLname?.text?.toString())
        val email = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentEditPatientFamilyMemberBinding?.edtAddpatientEmail?.text?.toString())
        val phone_number = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentEditPatientFamilyMemberBinding?.edtAddpatientPhonenumber?.text?.toString())
        val gender = RequestBody.create(MediaType.parse("multipart/form-data"), selectedGender)
        val age = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentEditPatientFamilyMemberBinding?.edtAddpatientAge?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"),"1")
        if (imageFile != null) {
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            var multipartBody = MultipartBody.Part.createFormData("image", imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentEditPatientFamilyMemberViewModel?.apieditpatientfamily(patient_id,first_name,last_name,multipartBody,email,phone_number,gender,age)

        } else{
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), patientimage)
            var multipartBody = MultipartBody.Part.createFormData("image", "", image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentEditPatientFamilyMemberViewModel?.apieditpatientfamily(patient_id,first_name,last_name,multipartBody,email,phone_number,gender,age)
            //Toast.makeText(activity, "Image can not be blank", Toast.LENGTH_SHORT).show()
        }
    }

    override fun successEditFamilyMemberResponse(editFamilyMemberResponse: EditFamilyMemberResponse?) {
        baseActivity?.hideLoading()
        if(editFamilyMemberResponse?.code.equals("200")){
            Toast.makeText(activity, editFamilyMemberResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentBookingAppointment.newInstance(doctorId))

        }else{
            Toast.makeText(activity, editFamilyMemberResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorEditFamilyMemberResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


}