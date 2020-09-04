package com.rootscare.ui.bookingcart

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.cartitemdeleterequest.CartItemDeleteRequest
import com.rootscare.data.model.api.request.checkoutdoctorbookingrequest.CheckoutDoctorBookingRequest
import com.rootscare.data.model.api.request.doctorrequest.bookingcartrequests.BookingCartRequest
import com.rootscare.data.model.api.request.doctorrequest.doctorprivatesotrequest.DoctorPrivateSlotRequest
import com.rootscare.ui.base.BaseViewModel

class FragmentBookingCartViewModel : BaseViewModel<FragmentBookingCartNavigator>() {

    fun apipatientcart(bookingCartRequest: BookingCartRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apipatientcart(bookingCartRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successBookingCartResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorBookingCartResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apideletepatientcart(cartItemDeleteRequest: CartItemDeleteRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apideletepatientcart(cartItemDeleteRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorCartItemDeleteResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorBookingCartResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apipatientbookappointmentofflinepayment(checkoutDoctorBookingRequest: CheckoutDoctorBookingRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apipatientbookappointmentofflinepayment(checkoutDoctorBookingRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successCheckoutDoctorBookingResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorBookingCartResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}