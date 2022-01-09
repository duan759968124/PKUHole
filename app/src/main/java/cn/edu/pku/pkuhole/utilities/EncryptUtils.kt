package cn.edu.pku.pkuhole.utilities

import android.util.Base64
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.Cipher


object EncryptUtils {

    val publicK : String = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA6HcEQA3OMytGT/gvmdq1\n" +
            "2ITpgcmi8qnXA0NfpDNrBBo17NIM2it5IJ7e7Xl5lNRL75giJPKJhXYcB69KGCRX\n" +
            "u5YmfUGfKKlM8/YYmnbyyKSIoTcYrnhrjgKzDPJ9MViDYNnE6Za0JBDbpA9OemZs\n" +
            "I7Hbeyv4HFu9fYs8ZzrUAaeuj6uiiAaiJv+GEOOqa+ZCUtIy4t/OB7dGq92O1LN6\n" +
            "Dt7hiRJ0lzj2lwDIKAGvvlk+8iAJrGyYfLo885+mTrnfWGW9kptkFaEFTJfrYJ9X\n" +
            "GyKNpH87Jz5NlqfrZzb/bO60/YPJ526ZIVaNcq9I0GIRRmlC1N1fPuvfulnpvdkv\n" +
            "DKCN/2igERPi0BXZylB7emVZA9hk+Y8uFqZHdYCFPfuMJl5PICvXt08Dm+/HKwxf\n" +
            "SH2QIEZSuhAISSScuIquzkj9XlLwQ42IBefReOtXu7hZYD14Du4LxyXcfeIuejEh\n" +
            "wgPiUQp5/rFJR0bYtn/UPgYzQCHC/NJtCf/qh3IKCN6ggYwzBC+B1Y1eynbWt4cN\n" +
            "LEVzIV688/hcqZB3xKedbW2Tt03yepacHHoEMCM0yoTOrk+7OqGDX3xF7YIQJ0wH\n" +
            "+ZvtiwrQDqSBUCExNiC2p49kYL6qcbhMiRR+QQsXPkDzeudxu+sP3n3zhurWIk1a\n" +
            "0Io8JgPxs+yiBTyv1gjx9J0CAwEAAQ=="

    fun getPublicKeyFromString(): PublicKey {
        val publicBytes: ByteArray = Base64.decode(publicK, Base64.DEFAULT)
        val keySpec: X509EncodedKeySpec = X509EncodedKeySpec(publicBytes)
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }


    @Throws(IOException::class, GeneralSecurityException::class)
    fun encrypt(rawText: String, publicKey: PublicKey?): String? {
        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return Base64.encodeToString(cipher.doFinal(rawText.toByteArray(charset("UTF-8"))), Base64.DEFAULT)
    }

}