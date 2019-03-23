package com.tss.user_service.util;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/19 14:27
 * @description：发送短信工具类
 */
@Component
public class SendMsgUtil {
    private static final String GET = "get";
    /**
     * 发送短信消息
     *
     * @param url        请求地址
     * @param accountSid 开发者主账号ID
     * @param authToken  开发者主账号 TOKEN
     * @param content    短信内容，和模板保持一致
     * @param method     请求模式，get或者post
     * @param phones     短信接收端手机号码，可多个号码
     * @return String    请求结果
     */
    public String sendMsg(String url, String accountSid, String authToken, String templateid,String param, String... phones) {
        //可以多个手机号，用，号拼接起来
        String phone = "";
        for (String data : phones) {
            phone += data + ",";
        }
        phone.substring(0, phone.lastIndexOf(","));
        //秒嘀API需要的接口参数
        Map<String, String> params = new HashMap<>(16);
        params.put("accountSid", accountSid);
//        params.put("smsContent", content);
        params.put("templateid",templateid);
        params.put("param",param);
        params.put("to", phone);
        params.put("sig", queryArguments(accountSid, authToken));
        params.put("timestamp", getTimestamp());
        //调用Http工具类
        return HttpClientUtil.method(url, params, "POST");
    }

    /**
     * 随机生成6位随机验证码
     *
     * @return 验证码
     */
    public String getRandNum() {
        String randNum = new Random().nextInt(1000000) + "";
        //如果生成的不是6位数随机数则返回该方法继续生成
        int num = 6;
        if (randNum.length() != num) {
            return getRandNum();
        }
        return randNum;
    }

    /**
     * @return 获取时间戳
     */
    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 签名，MD5(sid + token+ timestamp)，共32位（小写）
     * 注意：MD5中的内容不包含”+”号。
     *
     * @param accountSid
     * @param authToken
     * @return
     */
    public String queryArguments(String accountSid, String authToken) {
        //时间戳
        String timestamp = getTimestamp();
        //签名认证
        String sig = getMD5(accountSid, authToken, timestamp);
        return sig;
    }

    /**
     * MD5加密
     *
     * @param args 动态参数
     * @return
     */
    public String getMD5(String... args) {
        StringBuffer result = new StringBuffer();
        if (args == null || args.length == 0) {
            return "";
        } else {
            StringBuffer str = new StringBuffer();
            for (String string : args) {
                str.append(string);
            }
            System.out.println("加密前：\t" + str.toString());
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] bytes = digest.digest(str.toString().getBytes());
                for (byte b : bytes) {
                    //转化十六进制
                    String hex = Integer.toHexString(b & 0xff);
                    if (hex.length() == 1) {
                        result.append("0" + hex);
                    } else {
                        result.append(hex);
                    }
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        System.out.println("加密后：\t" + result.toString());
        return result.toString();
    }
}
