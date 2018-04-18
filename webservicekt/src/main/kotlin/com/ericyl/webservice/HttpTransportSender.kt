package com.ericyl.webservice

import com.ericyl.webservice.exception.WebServiceException

import org.ksoap2.SoapFault
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapPrimitive
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

import java.net.ConnectException
import java.net.SocketTimeoutException

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * @author ericyl on 2017/12/28.
 */

internal class HttpTransportSender private constructor(private val properties: RequestProperties) {

    fun send(methodName: String, requestObjects: List<RequestObject>, listener: ResponseListener): Disposable {
        return send(methodName, requestObjects)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listener.onSuccess(it)
                }, {
                    listener.onError(it as WebServiceException)
                })

    }

    fun <T> send(methodName: String, requestObjects: List<RequestObject>, action: (webServiceResponse: WebServiceResponse) -> T): Observable<T> {
        return send(methodName, requestObjects).map { action(it) }
    }

    private fun send(methodName: String, requestObjects: List<RequestObject>): Observable<WebServiceResponse> {
        return Observable.just(createRequest(methodName, requestObjects))
                .subscribeOn(Schedulers.io())
                .map<WebServiceResponse> {
                    try {
                        val soapAction = "${properties.nameSpace}${it.methodName}"
                        val transportSE = HttpTransportSE(properties.url, properties.timeout)
                        val envelope = SoapSerializationEnvelope(
                                properties.soapVersion)
                        envelope.setOutputSoapObject(it.requestObject)


                        //                          synchronized (_transportSE) {
                        transportSE.call(soapAction, envelope)
                        //                          }

//                        val obj = envelope.response as SoapPrimitive
                        WebServiceResponse(CODE_SUCCESS, envelope.response.toString())
                    } catch (e: SoapFault) {
                        throw WebServiceException(e)
                    } catch (e: SocketTimeoutException) {
                        throw WebServiceException(CODE_SERVICE_UNAVAILABLE, e)
                    } catch (e: ConnectException) {
                        throw WebServiceException(CODE_NETWORK_UNREACHABLE, e)
                    } catch (e: Exception) {
                        throw WebServiceException(e)
                    }
                }
    }


    private fun createRequest(methodName: String, objects: List<RequestObject>): WebServiceRequest {
        val requestObject = SoapObject(properties.nameSpace, methodName)
        for ((key, value) in objects) {
            requestObject.addProperty(key, value)
        }
        return WebServiceRequest(methodName, requestObject)
    }

    fun cancel(disposable: Disposable) {
        if (!disposable.isDisposed)
            disposable.dispose()
    }

    companion object {
        fun newInstance(url: String, nameSpace: String, timeout: Int): HttpTransportSender {
            return newInstance(RequestProperties.newInstance(url, nameSpace, timeout))
        }

        fun newInstance(requestProperties: RequestProperties): HttpTransportSender {
            return HttpTransportSender(requestProperties)
        }
    }

}
