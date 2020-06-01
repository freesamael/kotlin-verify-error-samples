package com.example.verifyerror

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Sample {
    private const val TAG = "Sample"

    /**
     * With kotlin 1.3.72 and coroutines 1.3.7, this generates
     *
     * java.lang.VerifyError: Verifier rejected class com.example.verifyerror.Sample$errorCapturingThrowable$1:
     * java.lang.Object com.example.verifyerror.Sample$errorCapturingThrowable$1.invokeSuspend(java.lang.Object) failed to verify:
     * java.lang.Object com.example.verifyerror.Sample$errorCapturingThrowable$1.invokeSuspend(java.lang.Object):
     * [0x59] register v3 has type Reference: java.lang.Throwable but expected Precise Reference: kotlin.jvm.internal.Ref$ObjectRef
     */
    fun errorCapturingThrowable() {
        GlobalScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    Log.d(TAG, "execute on IO dispatcher")
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "capture and handle $t on main dispatcher")
                }
            }
        }
    }
}