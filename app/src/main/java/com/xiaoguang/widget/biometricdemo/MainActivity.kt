package com.xiaoguang.widget.biometricdemo

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.xiaoguang.widget.biometric.BiometricPromptManager
import com.xiaoguang.widget.biometric.CommDialog.*
import com.xiaoguang.widget.biometric.FingerprintCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            showDialog()

        }
    }

    private fun showDialog() {
        BiometricPromptManager.Builder(this)
            // 启动安卓自带弹窗 default true，设置成false面部识别不生效
            .enableAndroidP(true)
            .setCallback(fingerprintCallback)
            .title("请验证已录入的指纹")
            .cancelText("取消")
            // enableAndroidP true 安卓8以上手机无效
            .setImgRes(R.drawable.ic_fingerprint)
            .failTitle("未能识别指纹")
            .failContent("再试一次")
            .build()
    }

    private val fingerprintCallback: FingerprintCallback = object : FingerprintCallback {

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onSucceeded23(result: FingerprintManagerCompat.AuthenticationResult?) {
            Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        override fun onSucceeded28(result: androidx.biometric.BiometricPrompt.AuthenticationResult?) {
            Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()
        }

        override fun onFailed() {
            Toast.makeText(this@MainActivity, "onFailed", Toast.LENGTH_SHORT).show()
        }

        override fun onError(errString: CharSequence?) {
            Toast.makeText(this@MainActivity, "onError " + errString, Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            Toast.makeText(this@MainActivity, "onCancel", Toast.LENGTH_SHORT).show()
        }

    }
}