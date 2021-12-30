package com.xiaoguang.widget.biometric

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

/**
 * 加密类，用于判定指纹合法性
 */
@RequiresApi(Build.VERSION_CODES.M)
class CipherHelper {
    private val _keystore: KeyStore

    /**
     * 获得Cipher
     *
     * @return
     */
    fun createCipher(): Cipher? {
        var cipher: Cipher? = null
        try {
            cipher = createCipher(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cipher
    }

    /**
     * 创建一个Cipher，用于 FingerprintManager.CryptoObject 的初始化
     * https://developer.android.google.cn/reference/javax/crypto/Cipher.html
     *
     * @param retry
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun createCipher(retry: Boolean): Cipher {
        val key = GetKey()
        val cipher =
            Cipher.getInstance(TRANSFORMATION) // Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        try {
            cipher.init(Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE, key)
        } catch (e: KeyPermanentlyInvalidatedException) {
            _keystore.deleteEntry(KEY_NAME)
            if (retry) {
                createCipher(false)
            }
            throw Exception("Could not create the cipher for fingerprint authentication.", e)
        }
        return cipher
    }

    @Throws(Exception::class)
    private fun GetKey(): Key {
        val secretKey: Key
        if (!_keystore.isKeyEntry(KEY_NAME)) {
            CreateKey()
        }
        secretKey = _keystore.getKey(KEY_NAME, null)
        return secretKey
    }

    @Throws(Exception::class)
    private fun CreateKey() {
        val keyGen = KeyGenerator.getInstance(KEY_ALGORITHM, KEYSTORE_NAME)
        val keyGenSpec = KeyGenParameterSpec.Builder(
            KEY_NAME,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(BLOCK_MODE)
            .setEncryptionPaddings(ENCRYPTION_PADDING)
            .setUserAuthenticationRequired(true)
            .build()
        keyGen.init(keyGenSpec)
        keyGen.generateKey()
    }

    companion object {
        // This can be key name you want. Should be unique for the app.
        private const val KEY_NAME = "com.hailong.fingerprint.CipherHelper"

        // We always use this keystore on Android.
        private const val KEYSTORE_NAME = "AndroidKeyStore"

        // Should be no need to change these values.
        private const val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION =
            KEY_ALGORITHM + "/" + BLOCK_MODE + "/" + ENCRYPTION_PADDING
    }

    init {
        _keystore = KeyStore.getInstance(KEYSTORE_NAME)
        _keystore.load(null)
    }
}