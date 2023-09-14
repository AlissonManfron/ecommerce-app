package br.com.amanfron.ecommerce_app.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

interface ResponseHandler {
    fun <T> handleResponseFlow(call: suspend () -> Response<T>): Flow<T>
}

class ResponseHandlerImpl @Inject constructor() : ResponseHandler {

    private val defaultErrorMessage = "Ops, ocorreu um erro inesperado, tente novamente!"

    override fun <T> handleResponseFlow(call: suspend () -> Response<T>): Flow<T> {
        return flow {
            val result = runSafety {
                val response = call.invoke()
                val body = response.body()
                when {
                    response.isSuccessful && body != null -> Result.success(body)
                    else -> Result.failure(Exception(defaultErrorMessage))
                }
            }.getOrThrow()

            emit(result)
        }
    }

    private suspend fun <T> runSafety(block: suspend () -> Result<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
