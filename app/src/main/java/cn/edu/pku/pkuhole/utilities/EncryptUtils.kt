package cn.edu.pku.pkuhole.utilities

import org.apache.commons.codec.binary.Base64
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.KeyFactory
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


object EncryptUtils {


    val PUBLIC_KEY_PASTH = "projectpasswd.key.pub"

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
            "0Io8JgPxs+yiBTyv1gjx9J0CAwEAAQ==\n"

    const val key_Pub = ("-----BEGIN PUBLIC KEY-----\n"
            + "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA6HcEQA3OMytGT/gvmdq1\n"
            + "2ITpgcmi8qnXA0NfpDNrBBo17NIM2it5IJ7e7Xl5lNRL75giJPKJhXYcB69KGCRX\n"
            + "u5YmfUGfKKlM8/YYmnbyyKSIoTcYrnhrjgKzDPJ9MViDYNnE6Za0JBDbpA9OemZs\n"
            + "I7Hbeyv4HFu9fYs8ZzrUAaeuj6uiiAaiJv+GEOOqa+ZCUtIy4t/OB7dGq92O1LN6\n"
            + "Dt7hiRJ0lzj2lwDIKAGvvlk+8iAJrGyYfLo885+mTrnfWGW9kptkFaEFTJfrYJ9X\n"
            + "GyKNpH87Jz5NlqfrZzb/bO60/YPJ526ZIVaNcq9I0GIRRmlC1N1fPuvfulnpvdkv\n"
            + "DKCN/2igERPi0BXZylB7emVZA9hk+Y8uFqZHdYCFPfuMJl5PICvXt08Dm+/HKwxf\n"
            + "SH2QIEZSuhAISSScuIquzkj9XlLwQ42IBefReOtXu7hZYD14Du4LxyXcfeIuejEh\n"
            + "wgPiUQp5/rFJR0bYtn/UPgYzQCHC/NJtCf/qh3IKCN6ggYwzBC+B1Y1eynbWt4cN\n"
            + "LEVzIV688/hcqZB3xKedbW2Tt03yepacHHoEMCM0yoTOrk+7OqGDX3xF7YIQJ0wH\n"
            + "+ZvtiwrQDqSBUCExNiC2p49kYL6qcbhMiRR+QQsXPkDzeudxu+sP3n3zhurWIk1a\n"
            + "0Io8JgPxs+yiBTyv1gjx9J0CAwEAAQ==\n"
            + "-----END PUBLIC KEY-----")


    @Throws(IOException::class, GeneralSecurityException::class)
    fun getPublicKeyFromString(key: String): RSAPublicKey? {
        var publicKeyPEM = key
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "")
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "")
        val encoded: ByteArray = Base64.decodeBase64(publicKeyPEM)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(X509EncodedKeySpec(encoded)) as RSAPublicKey
    }

//    @Throws(IOException::class, GeneralSecurityException::class)
//    fun getPublicKey(filename: String?): RSAPublicKey? {
//        val publicKeyPEM: String = AuthenticatorKeys.getKey(filename)
//        return getPublicKeyFromString(publicKeyPEM)
//    }


//    fun getPublicKeyFromString(): PublicKey {
//        val publicBytes: ByteArray = Base64.decode(publicK, Base64.DEFAULT)
//        val keySpec: X509EncodedKeySpec = X509EncodedKeySpec(publicBytes)
//        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
//        return keyFactory.generatePublic(keySpec)
//    }

//    fun getPublicKeyFromFile(context: Context): String{
//        val inputSteam: InputStream = context.assets.open(PUBLIC_KEY_PASTH)
//        val br:BufferedReader = BufferedReader(InputStreamReader(inputSteam))
//        var lines : MutableList<String> = ArrayList()
//        var line : String? = null
//        while ((br.readLine().also { line = it })!=null){
//            line?.let { lines.add(it) }
//        }
//        // removes the first and last lines of the file (comments)
//        if (lines.size > 1 && lines[0].startsWith("-----") && lines[lines.size -1].startsWith("-----")) {
//            lines.removeAt(0);
//            lines.removeAt(lines.size - 1);
//        }
//        // concats the remaining lines to a single String
//        val sb = StringBuilder()
//        for (aLine in lines) sb.append(aLine)
//        val keyString = sb.toString()
//        Timber.e("keyString:$keyString")
//        return keyString
//    }


    @Throws(IOException::class, GeneralSecurityException::class)
    fun encrypt(rawText: String, publicKey: PublicKey?): String? {
        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return Base64.encodeBase64String(cipher.doFinal(rawText.toByteArray(charset("UTF-8"))))
    }

}