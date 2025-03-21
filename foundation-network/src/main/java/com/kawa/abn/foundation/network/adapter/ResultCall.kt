package com.kawa.abn.foundation.network.adapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ResultCall<T>(val delegate: Call<T>) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                response.code(),
                                Result.success(response.body()!!)
                            )
                        )
                    } else {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                Result.failure(HttpException(response))
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    t.printStackTrace()
                    val throwable = when (t) {
                        is IOException -> Exception("No internet connection", t)
                        is HttpException -> {
                            HttpException(t.response())
                        }

                        else -> Exception("Unknown error occurred", t)
                    }
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(Result.failure(throwable))
                    )
                }
            }
        )
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(delegate.execute().body()!!))
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun clone(): Call<Result<T>> {
        return ResultCall(delegate.clone())
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }
}
