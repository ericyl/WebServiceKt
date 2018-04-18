package com.ericyl.webservice.model

import org.ksoap2.serialization.SoapObject

internal data class WebServiceRequest(val methodName: String, val requestObject: SoapObject)
