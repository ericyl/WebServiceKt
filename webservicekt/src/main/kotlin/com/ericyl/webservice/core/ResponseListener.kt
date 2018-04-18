package com.ericyl.webservice.core

import com.ericyl.webservice.exception.WebServiceException
import com.ericyl.webservice.model.WebServiceResponse

internal interface ResponseListener {

    @Throws(WebServiceException::class)
    fun onSuccess(response: WebServiceResponse)

    fun onError(exception: WebServiceException)


}
