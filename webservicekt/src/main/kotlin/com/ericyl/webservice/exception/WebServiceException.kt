package com.ericyl.webservice.exception

import com.ericyl.webservice.CODE_AN_UNKNOWN_ERROR


/**
 * @author ericyl on 2017/11/29.
 */

class WebServiceException(val errorCode: Int, cause: Throwable) : RuntimeException(cause) {
    constructor(cause: Throwable) : this(CODE_AN_UNKNOWN_ERROR, cause)

    override val message: String
        get() = cause?.message ?: ""
}
