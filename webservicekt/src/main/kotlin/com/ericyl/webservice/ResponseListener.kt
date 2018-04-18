package com.ericyl.webservice

import com.ericyl.webservice.exception.WebServiceException

internal interface ResponseListener {

    @Throws(WebServiceException::class)
    fun onSuccess(response: WebServiceResponse)

    fun onError(exception: WebServiceException)


}
