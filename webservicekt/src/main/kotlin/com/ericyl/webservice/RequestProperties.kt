package com.ericyl.webservice

import org.ksoap2.SoapEnvelope

/**
 * @author ericyl on 2017/11/28.
 */

internal class RequestProperties private constructor(val url: String, val nameSpace: String, val timeout: Int, var soapVersion: Int = SoapEnvelope.VER10) {
    companion object {
        fun newInstance(url: String, nameSpace: String, timeout: Int): RequestProperties {
            return RequestProperties(url, nameSpace, timeout)
        }
    }
}


