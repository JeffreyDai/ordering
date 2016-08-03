package com.wzqj.ordering.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Title: ThreeDesUtil.java</p>
 * <p>Description: 示例类</p>
 * <p>Company: wzqj</p>
 *
 * @author wh
 *         date  Dec 6, 2011  1:50:28 PM
 */
public class ThreeDesUtil {

    /**
     * 构造方法
     */
    public ThreeDesUtil() {
    }

    /**
     * <p>Title: encryptMode</p>
     * <p>Description: </p>
     *
     * @param keybyte keybyte
     * @param src     src
     * @return return
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            Cipher c1;
            javax.crypto.SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
            c1 = Cipher.getInstance("DESede");
            c1.init(1, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        } catch (Exception exx) {
            exx.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Title: decryptMode</p>
     * <p>Description: </p>
     *
     * @param keybyte keybyte
     * @param src     src
     * @return return
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            Cipher c1;
            javax.crypto.SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
            c1 = Cipher.getInstance("DESede");
            c1.init(2, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        } catch (Exception exx) {
            exx.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Title: byte2hex</p>
     * <p>Description: </p>
     *
     * @param arrB arrB
     * @return return
     */
    public static String byte2hex(byte[] arrB) {
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp;
            for (intTmp = arrB[i]; intTmp < 0; intTmp += 256) {
                if (intTmp < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toString(intTmp, 16));
            }
        }

        return sb.toString().toUpperCase();
    }

    /**
     * <p>Title: threeDESencode</p>
     * <p>Description: </p>
     *
     * @param value 加密串
     * @param key   key
     * @return 加密结果
     */
    public static String threeDESencode(String value, String key) {
        try {
            byte[] encoded;
            Base64 encoder;
            encoded = encryptMode(key.getBytes(), value.getBytes());
            encoder = new Base64();
            return new String(encoder.encode(encoded));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <p>Title: threeDESdecrypt</p>
     * <p>Description: </p>
     *
     * @param value 解密串
     * @param key   key
     * @return 解密串
     */
    public static String threeDESdecrypt(String value, String key) {
        try {
            byte[] decoded;
            Base64 decoer = new Base64();
            byte[] btyvalue = decoer.decode(value.getBytes());
            decoded = decryptMode(key.getBytes(), btyvalue);
            return new String(decoded);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
