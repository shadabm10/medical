package com.rootscare.data.datasource.api





import com.rootscare.data.model.api.request.appointmentdetailsrequest.AppointmentDetailsRequest
import com.rootscare.data.model.api.request.appointmentrequest.AppointmentRequest
import com.rootscare.data.model.api.request.cancelappointmentrequest.CancelAppointmentRequest
import com.rootscare.data.model.api.request.cartitemdeleterequest.CartItemDeleteRequest
import com.rootscare.data.model.api.request.checkoutdoctorbookingrequest.CheckoutDoctorBookingRequest
import com.rootscare.data.model.api.request.deletepatientfamilymemberrequest.DeletePatientFamilyMemberRequest
import com.rootscare.data.model.api.request.doctorrequest.bookingcartrequests.BookingCartRequest
import com.rootscare.data.model.api.request.doctorrequest.doctordetailsrequest.DoctorDetailsRequest
import com.rootscare.data.model.api.request.doctorrequest.doctorlistbydepartmentrequest.DoctorListByDepartmentIdRequest
import com.rootscare.data.model.api.request.doctorrequest.doctorprivatesotrequest.DoctorPrivateSlotRequest
import com.rootscare.data.model.api.request.doctorrequest.doctorsearchrequest.SeeAllDoctorSearch
import com.rootscare.data.model.api.request.doctorrequest.getpatientfamilymemberrequest.GetPatientFamilyMemberRequest
import com.rootscare.data.model.api.request.forgotpassword.forgotpasswordchangerequest.ForgotPasswordChangeRequest
import com.rootscare.data.model.api.request.forgotpassword.forgotpasswordemailrequest.ForgotPasswordSendEmailRequest
import com.rootscare.data.model.api.request.homesearch.HomeSearchRequest
import com.rootscare.data.model.api.request.insertdoctorreviewratingrequest.InsertDoctorReviewRatingRequest
import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.data.model.api.request.medicalrecorddeleterequest.MedicalFileDeleteRequest
import com.rootscare.data.model.api.request.medicalrecordsrequest.GetMedicalRecordListRequest
import com.rootscare.data.model.api.request.nurse.departmentnurselist.DepartmentNurseListRequest
import com.rootscare.data.model.api.request.nurse.hourlyslot.NurseHourlySlotRequest
import com.rootscare.data.model.api.request.nurse.nursedetailsrequest.NurseDetailsRequest
import com.rootscare.data.model.api.request.nurse.nurseslots.NurseSlotRequest
import com.rootscare.data.model.api.request.nurse.review.InsertNurseReviewRequest
import com.rootscare.data.model.api.request.nurse.searchbyname.NurseSearchByNameRequest
import com.rootscare.data.model.api.request.patientpaymenthistoryreuest.PatientPaymentHistoryRequest
import com.rootscare.data.model.api.request.patientprofilerequest.PatientProfileRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilelifestylerequest.ProfileLifestyleUpdateRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilemedicalrequest.ProfileMedicalUpdateRequest
import com.rootscare.data.model.api.request.patientreviewandratingrequest.PatientReviewAndRatingRequest
import com.rootscare.data.model.api.request.registrationrequest.registrationotpcheckrequest.RegistrationCheckOTPRequest
import com.rootscare.data.model.api.request.registrationrequest.registrationotpsendrequest.RegistrationSendOTPRequest
import com.rootscare.data.model.api.request.reschedule.DoctorAppointmentRescheduleRequest
import com.rootscare.data.model.api.response.appointcancelresponse.AppointmentCancelResponse
import com.rootscare.data.model.api.response.appointmentdetails.DoctorAppointmentResponse
import com.rootscare.data.model.api.response.appointmenthistoryresponse.AppointmentHistoryResponse
import com.rootscare.data.model.api.response.caregiverallresponse.allcaregiverlistingresponse.AllCaregiverListingResponse
import com.rootscare.data.model.api.response.caregiverallresponse.caregiverlist.GetCaregiverListResponse
import com.rootscare.data.model.api.response.deactivateaccountresponse.DeactivateAccountResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.alldoctorlistingresponse.AllDoctorListingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.checkoutdoctorbookingresponse.CheckoutDoctorBookingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.addpatientresponse.AddPatientResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.BookingCartResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.cartitemdeleteresponse.DoctorCartItemDeleteResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingresponse.DoctorPrivateBooingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse.DoctorPrivateSlotResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.getpatientfamilymemberlistresponse.GetPatientFamilyListResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctordepartmentlistingresponse.DoctorDepartmentListingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctordetailsresponse.DoctorDetailsResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorreviewsubmitresponse.DoctorReviewRatingSubmiteResponse
import com.rootscare.data.model.api.response.editpatientfamilymemberresponse.EditFamilyMemberResponse
import com.rootscare.data.model.api.response.forgotpasswordresponse.forgotpasswordchangepassword.ForgotPasswordChangePasswordResponse
import com.rootscare.data.model.api.response.forgotpasswordresponse.forgotpasswordsendmailresponse.ForgotPasswordSendMailResponse
import com.rootscare.data.model.api.response.hospitalallapiresponse.allhospitalistingresponse.AllHospitalListingResponse
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.data.model.api.response.medicalfiledeleteresponse.MedicalFileDeleteResponse
import com.rootscare.data.model.api.response.medicalrecordresponse.MedicalRecordListResponse
import com.rootscare.data.model.api.response.nationalityresponse.NationalityResponse
import com.rootscare.data.model.api.response.nurses.nurseappointmentdetails.NurseAppointmentDetailsResponse
import com.rootscare.data.model.api.response.nurses.nursebookappointment.NurseBookAppointmentResponse
import com.rootscare.data.model.api.response.nurses.nursedetails.NurseDetailsResponse
import com.rootscare.data.model.api.response.nurses.nursehourlyslot.GetNurseHourlySlotResponse
import com.rootscare.data.model.api.response.nurses.nurselist.GetNurseListResponse
import com.rootscare.data.model.api.response.nurses.nurseviewtiming.NueseViewTimingsResponse
import com.rootscare.data.model.api.response.patienthome.PatientHomeApiResponse
import com.rootscare.data.model.api.response.patientprescription.PatientPrescriptionResponse
import com.rootscare.data.model.api.response.patientprofileresponse.PatientProfileResponse
import com.rootscare.data.model.api.response.patientreviewandratingresponse.PatientReviewAndRatingResponse
import com.rootscare.data.model.api.response.paymenthistoryresponse.PatientPaymentHistoryResponse
import com.rootscare.data.model.api.response.registrationresponse.registrationcheckotpresponse.RegistrationCheckOTPResponse
import com.rootscare.data.model.api.response.registrationresponse.registrationsendotpresponse.RegistrationSendOTPResponse
import com.rootscare.data.model.api.response.reschedule.doctorreschedule.DoctorRescheduleResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.ArrayList

interface ApiService {

    //    @Headers({
    //            "x-api-key: 123456"
    //    })

//    @GET("api-product-list")
//    fun getOurProductList(): Single<OurProductListResponse>

    //13-04-2020 API IMPLEMENTATION

    @POST("api-patient-registration-otp-send")
    fun apipatientregistrationotpsend(@Body registrationRequestBody: RegistrationSendOTPRequest): Single<RegistrationSendOTPResponse>

    @POST("api-patient-registration-otp-check")
    fun apipatientregistrationotpcheck(@Body registrationRequestBody: RegistrationCheckOTPRequest): Single<RegistrationCheckOTPResponse>

    @POST("api-patient-login")
    fun apipatientlogin(@Body loginRequestBody: LoginRequest): Single<LoginResponse>

    @POST("api-forgot-password-email")
    fun apiforgotpasswordemail(@Body forgotPasswordSendEmailRequestBody: ForgotPasswordSendEmailRequest): Single<ForgotPasswordSendMailResponse>

    @POST("api-forgot-change-password")
    fun apiforgotchangepassword(@Body forgotPasswordChangeRequestBody: ForgotPasswordChangeRequest): Single<ForgotPasswordChangePasswordResponse>

    @POST("api-patient-profile")
    fun apipatientprofile(@Body patientProfileRequestBody: PatientProfileRequest): Single<PatientProfileResponse>

    @POST("api-edit-patient-profile-medical")
    fun apieditpatientprofilemedical(@Body ProfileMedicalUpdateRequestBody: ProfileMedicalUpdateRequest): Single<PatientProfileResponse>

    @POST("api-edit-patient-profile-style")
    fun apieditpatientprofilestyle(@Body ProfileLifestyleUpdateRequestBody: ProfileLifestyleUpdateRequest): Single<PatientProfileResponse>


    @Multipart
    @POST("api-edit-patient-profile-personal")
    fun apieditpatientprofilepersonal(@Part("user_id") user_id: RequestBody,
                                @Part("first_name") first_name: RequestBody,
                                @Part("last_name") last_name: RequestBody,
                                @Part("id_number") id_number: RequestBody,
                                      @Part("age") age: RequestBody,
                                      @Part("address") address: RequestBody,
                                      @Part("gender") gender: RequestBody,
                                      @Part("nationality") nationality: RequestBody,
                                      @Part("height") height: RequestBody,
                                      @Part("weight") weight: RequestBody,
                                      @Part("marital_status") marital_status: RequestBody): Single<PatientProfileResponse>

//    @Part("status") status: RequestBody,    @Part image: MultipartBody.Part,

    @POST("api-patient-home")
    fun apipatienthome(): Single<PatientHomeApiResponse>

    @POST("api-patient-home-search")
    fun apipatienthomesearch(@Body homeSearchRequestRequestBody: HomeSearchRequest): Single<PatientHomeApiResponse>

    @POST("api-department-list")
    fun apidepartmentlist(): Single<DoctorDepartmentListingResponse>

    @POST("api-doctor-list")
    fun apidoctorlist(): Single<AllDoctorListingResponse>



    @POST("api-hospital-list")
    fun apihospitallist(): Single<AllHospitalListingResponse>


    @GET("api-caregiver-list")
    fun apicaregivelist(): Single<AllCaregiverListingResponse>


    @GET("api-caregiver-list")
    fun apicaregivelist1(): Single<GetCaregiverListResponse>

    @POST("api-department-doctor-list")
    fun apidepartmentdoctorlist(@Body doctorListByDepartmentIdRequestBody: DoctorListByDepartmentIdRequest): Single<AllDoctorListingResponse>



    @POST("api-department-hospital-list")
    fun apidepartmenthospitallist(@Body doctorListByDepartmentIdRequestBody: DoctorListByDepartmentIdRequest): Single<AllHospitalListingResponse>

    @POST("api-doctor-details")
    fun apidoctordetails(@Body doctorDetailsRequestBody: DoctorDetailsRequest): Single<DoctorDetailsResponse>

    @POST("api-patient-family-member")
    fun apipatientfamilymember(@Body getPatientFamilyMemberRequestBody: GetPatientFamilyMemberRequest): Single<GetPatientFamilyListResponse>

    @POST("api-doctor-private-slot")
    fun apidoctorprivateslot(@Body doctorPrivateSlotRequestBody: DoctorPrivateSlotRequest): Single<DoctorPrivateSlotResponse>

    @Multipart
    @POST("api-insert-patient-family")
    fun apiinsertpatientfamily(@Part("patient_id") patient_id: RequestBody,
                                      @Part("first_name") first_name: RequestBody,
                                      @Part("last_name") last_name: RequestBody,
                                      @Part image: MultipartBody.Part,
                                      @Part("gender") gender: RequestBody,
                                      @Part("age") age: RequestBody): Single<AddPatientResponse>

//    @Part("email") email: RequestBody,
//    @Part("phone_number") phone_number: RequestBody,
    @Multipart
    @POST("api-book-cart-private-doctor")
    fun apibookcartprivatedoctor(@Part("patient_id") patient_id: RequestBody,
                               @Part("family_member_id") family_member_id: RequestBody,
                               @Part("doctor_id") doctor_id: RequestBody,
                                 @Part("private_clinic_id") private_clinic_id: RequestBody,
                                 @Part("appointment_date") appointment_date: RequestBody,
                                 @Part("from_time") from_time: RequestBody,
                                 @Part("to_time") to_time: RequestBody,
                                 @Part("price") price: RequestBody,
                               @Part symptom_recording: MultipartBody.Part,
                               @Part("symptom_text") symptom_text: RequestBody,
                                 @Part upload_prescription: MultipartBody.Part,
                               @Part("appointment_type") appointment_type: RequestBody): Single<DoctorPrivateBooingResponse>

    @POST("api-patient-cart")
    fun apipatientcart(@Body bookingCartRequestBody: BookingCartRequest): Single<BookingCartResponse>

    @POST("api-delete-patient-cart")
    fun apideletepatientcart(@Body cartItemDeleteRequestBody: CartItemDeleteRequest): Single<DoctorCartItemDeleteResponse>

    @POST("api-insert-doctor-review")
    fun apiinsertdoctorreview(@Body insertDoctorReviewRatingRequestBody: InsertDoctorReviewRatingRequest): Single<DoctorReviewRatingSubmiteResponse>

    @POST("api-patient-book-appointment-offline-payment")
    fun apipatientbookappointmentofflinepayment(@Body checkoutDoctorBookingRequestBody: CheckoutDoctorBookingRequest): Single<CheckoutDoctorBookingResponse>

    @POST("api-patient-review")
    fun apipatientreview(@Body patientReviewAndRatingRequestBody: PatientReviewAndRatingRequest): Single<PatientReviewAndRatingResponse>

    @POST("api-patient-payment-history")
    fun apipatientpaymenthistory(@Body patientPaymentHistoryRequestBody: PatientPaymentHistoryRequest): Single<PatientPaymentHistoryResponse>

    @POST("api-patient-appointment-history")
    fun apipatientappointmenthistory(@Body appointmentRequestBody: AppointmentRequest): Single<AppointmentHistoryResponse>

    @POST("api-search-doctor")
    fun apisearchdoctor(@Body seeAllDoctorSearchRequestBody: SeeAllDoctorSearch): Single<AllDoctorListingResponse>


    @POST("api-search-hospital")
    fun apisearchhospital(@Body seeAllDoctorSearchRequestBody: SeeAllDoctorSearch): Single<AllHospitalListingResponse>

    @POST("api-appointment-details")
    fun apiappointmentdetails(@Body appointmentDetailsRequestBody: AppointmentDetailsRequest): Single<DoctorAppointmentResponse>


    @POST("api-appointment-details")
    fun apinurseappointmentdetails(@Body appointmentDetailsRequestBody: AppointmentDetailsRequest): Single<NurseAppointmentDetailsResponse>

    @POST("api-patient-upcoming-appointment")
    fun apipatientupcomingappointment(@Body appointmentRequestBody: AppointmentRequest): Single<AppointmentHistoryResponse>

    @POST("api-patient-today-appointment")
    fun apipatienttodayappointment(@Body appointmentRequestBody: AppointmentRequest): Single<AppointmentHistoryResponse>

    @POST("api-patient-all-appointment")
    fun apipatientallappointment(@Body appointmentRequestBody: AppointmentRequest): Single<AppointmentHistoryResponse>


    @POST("api-patient-upcoming-cancel-appointment")
    fun apipatientupcomingcancelappointment(@Body appointmentRequestBody: AppointmentRequest): Single<AppointmentHistoryResponse>

    @POST("api-patient-prescription")
    fun apipatientprescription(@Body appointmentRequestBody: AppointmentRequest): Single<PatientPrescriptionResponse>

    @POST("api-deactivate-user")
    fun apideactivateuser(@Body appointmentRequestBody: AppointmentRequest): Single<DeactivateAccountResponse>

    @POST("api-delete-patient-family-member")
    fun apideletepatientfamilymember(@Body deletePatientFamilyMemberRequestBody: DeletePatientFamilyMemberRequest): Single<GetPatientFamilyListResponse>

    @Multipart
    @POST("api-edit-patient-family")
    fun apieditpatientfamily(@Part("id") id: RequestBody,
                               @Part("first_name") first_name: RequestBody,
                               @Part("last_name") last_name: RequestBody,
                               @Part image: MultipartBody.Part,
                               @Part("gender") gender: RequestBody,
                               @Part("age") age: RequestBody): Single<EditFamilyMemberResponse>

    @POST("api-nationality")
    fun apinationality(): Single<NationalityResponse>

    @POST("api-cancel-appointment")
    fun apicancelappointment(@Body cancelAppointmentRequestBody: CancelAppointmentRequest): Single<AppointmentCancelResponse>

    @POST("api-reschedule-appointment")
    fun apirescheduleappointment(@Body doctorAppointmentRescheduleRequestBody: DoctorAppointmentRescheduleRequest): Single<DoctorRescheduleResponse>

    @POST("api-patient-medical-record")
    fun apipatientmedicalrecord(@Body getMedicalRecordListRequestBody: GetMedicalRecordListRequest): Single<MedicalRecordListResponse>

    @POST("api-delete-patient-medical-record")
    fun apideletepatientmedicalrecord(@Body medicalFileDeleteRequestBody: MedicalFileDeleteRequest): Single<MedicalFileDeleteResponse>


    @Multipart
    @POST("api-insert-medical-record")
    fun apiinsertmedicalrecord(@Part("user_id") user_id: RequestBody,
                               @Part("date") date: RequestBody,
                               @Part("title") title: RequestBody,
                               @Part file: ArrayList<MultipartBody.Part>): Single<MedicalRecordListResponse>


    @GET("api-nurse-list")
    fun apinurselist(): Single<GetNurseListResponse>

    @POST("api-search-nurse")
    fun apisearchnurse(@Body nurseSearchByNameRequestBody: NurseSearchByNameRequest): Single<GetNurseListResponse>


    @POST("api-search-caregiver")
    fun apisearchcaregiver(@Body nurseSearchByNameRequestBody: NurseSearchByNameRequest): Single<GetCaregiverListResponse>


    @POST("api-search-caregiver")
    fun apisearchcaregiverGrid(@Body nurseSearchByNameRequestBody: NurseSearchByNameRequest): Single<AllCaregiverListingResponse>

    @POST("api-department-nurse-list")
    fun apidepartmentnurselist(@Body departmentNurseListRequestBody: DepartmentNurseListRequest): Single<GetNurseListResponse>

    @POST("api-nurse-details")
    fun apinursedetails(@Body nurseDetailsRequestBody: NurseDetailsRequest): Single<NurseDetailsResponse>


    @POST("api-caregiver-details")
    fun apicaregiverdetails(@Body nurseDetailsRequestBody: NurseDetailsRequest): Single<NurseDetailsResponse>

    @POST("task-based-slots")
    fun taskbasedslots(@Body nurseSlotRequestBody: NurseSlotRequest): Single<NueseViewTimingsResponse>

    @POST("api-insert-nurse-review")
    fun apiinsertnursereview(@Body InsertNurseReviewRequestBody: InsertNurseReviewRequest): Single<DoctorReviewRatingSubmiteResponse>

    @POST("api-get-hourly-rates")
    fun apigethourlyrates(@Body nurseHourlySlotRequestBody: NurseHourlySlotRequest): Single<GetNurseHourlySlotResponse>

    @Multipart
    @POST("api-book-cart-nurse")
    fun apibookcartnurse(@Part("patient_id") patient_id: RequestBody,
                                 @Part("family_member_id") family_member_id: RequestBody,
                                 @Part("nurse_id") nurse_id: RequestBody,
                                 @Part("from_date") from_date: RequestBody,
                                 @Part("to_date") to_date: RequestBody,
                                 @Part("from_time") from_time: RequestBody,
                                 @Part("to_time") to_time: RequestBody,
                                 @Part("price") price: RequestBody,
                                 @Part symptom_recording: MultipartBody.Part,
                                 @Part("symptom_text") symptom_text: RequestBody,
                                 @Part upload_prescription: MultipartBody.Part,
                                 @Part("appointment_type") appointment_type: RequestBody): Single<NurseBookAppointmentResponse>
 @Multipart
    @POST("api-book-cart-caregiver")
    fun apibookcartcaregiver(@Part("patient_id") patient_id: RequestBody,
                                 @Part("family_member_id") family_member_id: RequestBody,
                                 @Part("caregiver_id") nurse_id: RequestBody,
                                 @Part("from_date") from_date: RequestBody,
                                 @Part("to_date") to_date: RequestBody,
                                 @Part("from_time") from_time: RequestBody,
                                 @Part("to_time") to_time: RequestBody,
                                 @Part("price") price: RequestBody,
                                 @Part symptom_recording: MultipartBody.Part,
                                 @Part("symptom_text") symptom_text: RequestBody,
                                 @Part upload_prescription: MultipartBody.Part,
                                 @Part("appointment_type") appointment_type: RequestBody): Single<NurseBookAppointmentResponse>


    @Multipart
    @POST("api-edit-patient-profile-image")
    fun apieditpatientprofileimage(@Part("user_id") user_id: RequestBody,
                                   @Part image: MultipartBody.Part): Single<PatientProfileResponse>



//    @Part("email") email: RequestBody,
//    @Part("phone_number") phone_number: RequestBody,
}
