package com.chris.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * Created by YaoQi on 2017/2/19.
 */
public class WendaUtils {
    private static final Logger logger = LoggerFactory.getLogger(WendaUtils.class);
    public static String md5(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(key.getBytes(Charset.forName("UTF-8")));
            byte[] resultByte = md.digest();
            String result = Hex.encodeHexString(resultByte);
            return result;
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}
