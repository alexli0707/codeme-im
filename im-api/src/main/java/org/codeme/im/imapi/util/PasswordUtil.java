package org.codeme.im.imapi.util;

//import org.apache.commons.codec.binary.Base64;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * PasswordUtil
 *
 * @author walker lee
 * @date 2019-10-10
 */
public class PasswordUtil {


    public static String encryptPasswordWithBycrypt(String originPassword) {
        return new BCryptPasswordEncoder().encode(originPassword);
    }

    public static boolean matchPasswordWithBycrypt(String originPassword, String encryptedPassword) {
        return new BCryptPasswordEncoder().matches(originPassword, encryptedPassword);
    }


    public static byte[] encryptPassword(String originPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(originPassword.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return hash;
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    //base64字符串转byte[]
    public static byte[] base64String2Byte(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }

    //byte[]转base64
    public static String byte2Base64String(byte[] b) {
        return Base64.encodeBase64String(b);
    }


    public static void main(String[] args) {
//        byte[] saltByte = PasswordUtil.generateSalt();
////        String salt = new String(saltByte);
//        String salt = PasswordUtil.byte2Base64String(saltByte);
//        System.out.println(salt.toString());
//        byte[] encryptedPasswordByte = new byte[0];
//        try {
//            encryptedPasswordByte = PasswordUtil.encryptPassword("123456",saltByte);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        String encryptedPassword = Base64.encodeBase64String(encryptedPasswordByte);
//        System.out.println(encryptedPassword);
//        try {
//            System.out.println(Base64.encodeBase64String(PasswordUtil.encryptPassword("123456",PasswordUtil.base64String2Byte(salt))));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
        String rs = new BCryptPasswordEncoder().encode("111111");
        System.out.println(rs);
    }
}
