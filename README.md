# BiometricHelper
指纹识别，面部识别，自定义弹窗，用法超级简单

#  基于安卓原生指纹识别和面部识别的工具类，可按需求自定义弹窗

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

###  主要调用代码

``` kotlin
    private fun showDialog(enableAndroidP: Boolean) {
        BiometricPromptManager.Builder(this)
            // 启动安卓自带弹窗 default true，设置成false面部识别不生效
            .enableAndroidP(enableAndroidP)
            .setCallback(fingerprintCallback)
            .title("请验证已录入的指纹/面容")
            .cancelText("取消")
            // 以下设置 enableAndroidP false 安卓8以上手机才有效
            .setImgRes(R.drawable.ic_fingerprint)
            .failTitle("未能识别指纹")
            .failContent("再试一次")
            // 自定义弹窗(采用工厂模式，继承IBiometricDialog)
            //.setCustomDialog(BiometricDialogCustomImpl())
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
    

    /**
     * onPause生命周期关闭自定义弹窗
     */
    override fun onPause() {
        super.onPause()
        if (null != manager) {
            manager!!.onActivityPause()
        }
    }

    /**
     * onDestroy生命周期关闭自定义弹窗
     */
    override fun onDestroy() {
        super.onDestroy()
        if (null != manager) {
            manager!!.onActivityDestroy()
        }
    }
```

