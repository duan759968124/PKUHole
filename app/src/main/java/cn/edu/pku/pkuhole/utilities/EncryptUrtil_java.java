//package cn.edu.pku.pkuhole.utilities;
//
//import org.apache.commons.codec.binary.Base64;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.security.GeneralSecurityException;
//import java.security.KeyFactory;
//import java.security.PublicKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.X509EncodedKeySpec;
//
//import javax.crypto.Cipher;
//
//public class EncryptUrtil_java {
//
//    static final String key_Pub = "-----BEGIN PUBLIC KEY-----\n"
//            + "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA6HcEQA3OMytGT/gvmdq1\n"
//            + "2ITpgcmi8qnXA0NfpDNrBBo17NIM2it5IJ7e7Xl5lNRL75giJPKJhXYcB69KGCRX\n"
//            + "u5YmfUGfKKlM8/YYmnbyyKSIoTcYrnhrjgKzDPJ9MViDYNnE6Za0JBDbpA9OemZs\n"
//            + "I7Hbeyv4HFu9fYs8ZzrUAaeuj6uiiAaiJv+GEOOqa+ZCUtIy4t/OB7dGq92O1LN6\n"
//            + "Dt7hiRJ0lzj2lwDIKAGvvlk+8iAJrGyYfLo885+mTrnfWGW9kptkFaEFTJfrYJ9X\n"
//            + "GyKNpH87Jz5NlqfrZzb/bO60/YPJ526ZIVaNcq9I0GIRRmlC1N1fPuvfulnpvdkv\n"
//            + "DKCN/2igERPi0BXZylB7emVZA9hk+Y8uFqZHdYCFPfuMJl5PICvXt08Dm+/HKwxf\n"
//            + "SH2QIEZSuhAISSScuIquzkj9XlLwQ42IBefReOtXu7hZYD14Du4LxyXcfeIuejEh\n"
//            + "wgPiUQp5/rFJR0bYtn/UPgYzQCHC/NJtCf/qh3IKCN6ggYwzBC+B1Y1eynbWt4cN\n"
//            + "LEVzIV688/hcqZB3xKedbW2Tt03yepacHHoEMCM0yoTOrk+7OqGDX3xF7YIQJ0wH\n"
//            + "+ZvtiwrQDqSBUCExNiC2p49kYL6qcbhMiRR+QQsXPkDzeudxu+sP3n3zhurWIk1a\n"
//            + "0Io8JgPxs+yiBTyv1gjx9J0CAwEAAQ==\n"
//            + "-----END PUBLIC KEY-----";
//
//    public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
//        String publicKeyPEM = key;
//        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
//        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
//        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
//        return pubKey;
//    }
//
//    public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
////        Cipher cipher = Cipher.getInstance("RSA");
////        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
////        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
////        cipher.init(Cipher.ENCRYPT_MODE, publicKey, new
////                OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1,
////                PSource.PSpecified.DEFAULT));
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes(StandardCharsets.UTF_8)));
//    }
//
//}
