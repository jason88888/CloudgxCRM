package com.yunzhijia.open;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

 
/**
 * appAuth2授权认证
 * 
 * @since 2015-09-06
 * @version 1.1
 * @author Kingdee
 *
 */
public class AppAuth2 {
 
    private static final String ENC = "UTF-8";
    /**
     * appAuth2协议生成算法
     * @return String OpenAuth2 version="1.1", appid="XXX", timestamp=1436929524676, nonce="XXX", sign="XXX"
     * @throws UnsupportedEncodingException 
     */
    public static String appAuth2Treaty(String appid, String appSecret) throws UnsupportedEncodingException {
        String authorization = "OpenAuth2 version=\"%s\", appid=\"%s\", timestamp=%d, nonce=\"%s\", sign=\"%s\"";
        String version = "1.1";
        appid = URLEncoder.encode(appid, ENC);
        long timestamp = System.currentTimeMillis();
        String nonce = URLEncoder.encode(UUID.randomUUID().toString(), ENC);
        String sign = shaHex(version, appid, timestamp + "", nonce, appSecret);
        sign = URLEncoder.encode(sign, ENC);
        authorization = String.format(authorization, version, appid, timestamp, nonce, sign);
        return authorization;
    }
     
    /**
     * shaHex算法
     * 
     * @param data
     * @return
     */
    public static String shaHex(String... data) {
        Arrays.sort(data);
        String join = StringUtils.join(data);
        String sign = DigestUtils.sha1Hex(join);
        return sign;
    }
}