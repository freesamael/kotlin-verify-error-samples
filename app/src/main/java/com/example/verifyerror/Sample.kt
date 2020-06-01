package com.example.verifyerror

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.*

val sampleSupervisor = SupervisorJob()
val sampleScope: CoroutineScope
    get() {
        return CoroutineScope(sampleSupervisor + Dispatchers.Default)
    }

fun Throwable.rethrowOnCancellation(block: ((Throwable) -> Unit)? = null) {
    if (this is CancellationException) {
        block?.invoke(this)
        throw this
    }
}

class Sample {

    fun errorCapturingThrowable(appOrActivityCtx: Context, data: Uri) {
        data.getQueryParameter("test")?.also {
            sampleScope.launch {
                try {
                    iowork()
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "execute on main dispatcher")
                    }
                } catch (t: Throwable) {
                    t.rethrowOnCancellation()
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "handling exception $t on main dispatcher")
                    }
                }
            }
        }
    }

    private suspend fun iowork() = withContext(Dispatchers.IO) {
        Log.d(TAG, "execute on IO dispatcher")
    }

    companion object {
        const val TAG = "Sample"
    }
}