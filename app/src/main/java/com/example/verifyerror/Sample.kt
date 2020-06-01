package com.example.verifyerror

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

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

    /**
     * On Dalvik VM (i.e. pre-L devices), this generates
     * W/dalvikvm( 3892): VFY: register1 v1 type 17, wanted 5
     * W/dalvikvm( 3892): VFY:  rejecting call to Lcom/example/verifyerror/Foo;.<init> (ZIILkotlin/jvm/internal/DefaultConstructorMarker;)V
     * W/dalvikvm( 3892): VFY:  rejecting opcode 0x70 at 0x0040
     * W/dalvikvm( 3892): VFY:  rejected Lcom/example/verifyerror/Sample$errorResumeWithDefaultParameters$1;.invokeSuspend (Ljava/lang/Object;)Ljava/lang/Object;
     * W/dalvikvm( 3892): Verifier rejected class Lcom/example/verifyerror/Sample$errorResumeWithDefaultParameters$1;
     */
    fun errorResumeWithDefaultParametersPreL() {
        GlobalScope.launch {
            val foo = Foo(baz = randomInt())
            Log.d(TAG, "foo=$foo")
        }
    }

    private suspend fun randomInt(): Int = withContext(Dispatchers.IO) {
        return@withContext Random.nextInt()
    }
}

class Foo(val bar: Boolean = false, val baz: Int = 0)