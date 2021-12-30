# BiometricDemo
指纹识别，面部识别demo，用法超级简单

#  基于安卓原生指纹识别和面部识别的demo

##  功能：
    1：支持自定义弹窗
    2：支持系统弹窗

## 截图:

<div align="center">
<img src = "screenshots/p1.jpg" width=200 >
<img src = "screenshots/p2.jpg" width=200 >
<img src = "screenshots/p3.jpg" width=200 >
<img src = "screenshots/p4.jpg" width=200 >
<img src = "screenshots/p5.jpg" width=200 >
<img src = "screenshots/p6.jpg" width=200 >
<img src = "screenshots/p7.jpg" width=200 >
<img src = "screenshots/p8.jpg" width=200 >

</div>


## 使用方法:
###  需要指纹权限
```xml
  <uses-permission android:name="android.permission.USE_FINGERPRINT" />
```


``` java
    private fun showDialog(enableAndroidP: Boolean) {
        BiometricPromptManager.Builder(this)
            // 启动安卓自带弹窗 default true，设置成false面部识别不生效
            .enableAndroidP(enableAndroidP)
            .setCallback(fingerprintCallback)
            .title("请验证已录入的指纹/面容")
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
```

