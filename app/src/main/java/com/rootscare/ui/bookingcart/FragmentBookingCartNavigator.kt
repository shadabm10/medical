package com.rootscare.ui.bookingcart

import com.rootscare.data.model.api.response.doctorallapiresponse.checkoutdoctorbookingresponse.CheckoutDoctorBookingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.BookingCartResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.cartitemdeleteresponse.DoctorCartItemDeleteResponse

interface FragmentBookingCartNavigator {
    fun successBookingCartResponse(bookingCartResponse: BookingCartResponse?)
    fun successDoctorCartItemDeleteResponse(doctorCartItemDeleteResponse: DoctorCartItemDeleteResponse?)
    fun successCheckoutDoctorBookingResponse(checkoutDoctorBookingResponse: CheckoutDoctorBookingResponse?)
    fun errorBookingCartResponse(throwable: Throwable?)
}