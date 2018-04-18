package com.ericyl.webservice

import org.ksoap2.serialization.SoapObject

internal data class WebServiceRequest(val methodName: String, val requestObject: SoapObject)
