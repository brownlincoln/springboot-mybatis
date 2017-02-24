package com.chris.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.RETURN;
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

    public static final int ANONYMOUS_USERID = 15;
    public static final int SYSTEM_USERID = 14;

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

    //Get json string
    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }
    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }
}
