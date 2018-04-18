package com.ericyl.webservice

import com.ericyl.webservice.core.HttpTransportSender
import com.ericyl.webservice.core.ResponseListener
import com.ericyl.webservice.exception.WebServiceException
import com.ericyl.webservice.model.RequestObject
import com.ericyl.webservice.model.WebServiceResponse

import io.reactivex.Observable
import io.reactivex.disposables.Disposable


/**
 * @author ericyl on 2017/11/29.
 */

class WebServiceUtils private constructor(url: String, nameSpace: String, timeout: Int) {

    private val sender: HttpTransportSender = HttpTransportSender.newInstance(url, nameSpace, timeout)

    fun <T> send(methodName: String, requestObjects: List<RequestObject>, action: (webServiceResponse: WebServiceResponse) -> T): Observable<T> {
        return sender.send(methodName, requestObjects, action)
    }

    fun send(methodName: String, requestObjects: List<RequestObject>, successAction: (any: Any) -> Unit, errorAction: (exception: WebServiceException) -> Unit): Disposable {
        return sender.send(methodName, requestObjects, object : ResponseListener {
            override fun onSuccess(response: WebServiceResponse) {
                successAction(response.result)
            }

            override fun onError(exception: WebServiceException) {
                errorAction(exception)
            }
        })
    }

    fun cancel(disposable: Disposable) {
        sender.cancel(disposable)
    }

    fun isCancel(disposable:  Disposable?): Boolean {
        if (disposable == null)
            throw IllegalArgumentException("subscription is null")
        return disposable.isDisposed
    }

    companion object {
        fun newInstance(url: String, nameSpace: String, timeout: Int): WebServiceUtils {
            return WebServiceUtils(url, nameSpace, timeout)
        }
    }

}
