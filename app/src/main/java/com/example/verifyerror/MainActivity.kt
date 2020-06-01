package com.example.verifyerror

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtn1Click(view: View) {
        Sample.errorCapturingThrowable()
    }

    fun onBtn2Click(view: View) {
        Sample.errorResumeWithDefaultParametersPreL()
    }
}