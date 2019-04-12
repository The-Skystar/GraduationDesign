package com.tss.orderService.util;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/19 14:25
 * @description：网络请求工具类
 */
public class HttpClientUtil {
    private static final String GET = "get";
    /**
     * 默认GET请求模式，不带参数
     *
     * @param url 请求地址
     * @return
     */
    public static String method(String url) {
        return method(url, null, null);
    }

    /**
     * 默认GET请求模式，带参数
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String method(String url, Map<String, String> params) {
        return method(url, params, null);
    }

    /**
     * 根据请求模式选择GET或者POST，不带参数
     *
     * @param url    请求地址
     * @param method 请求模式
     * @return
     */
    public static String method(String url, String method) {
        return method(url, null, method);
    }

    /**
     * 根据请求模式选择GET或者POST，带参数
     *
     * @param url    请求地址
     * @param params 请求参数
     * @param method 请求模式
     * @return
     */
    public static String method(String url, Map<String, String> params, String method) {
        if (method != null && !"".equals(method)) {
            if (method.equalsIgnoreCase(GET)) {
                return get(url, params);
            } else {
                return post(url, params);
            }
        } else {
            return get(url, params);
        }
    }

    /**
     * POST请求
     *
     * @param url    请求的地址
     * @param params 参数map
     * @return 请求结果
     */
    public static String post(String url, Map<String, String> params) {
        //构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        //创建POST方法的实例
        PostMethod postMethod = new PostMethod(url);
        //参数不为空添加参数
        if (params != null) {
            NameValuePair[] data = new NameValuePair[params.size()];
            System.out.println(data.length);
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + "：" + entry.getValue());
                data[i] = new NameValuePair(entry.getKey(), entry.getValue());
                i++;
            }
            postMethod.setRequestBody(data);
        }
        //使用系统提供的默认的恢复策略,设置请求重试处理，用的是默认的重试处理：请求三次
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String result = execute(postMethod, httpClient);
        return result;
    }

    /**
     * GET请求
     *
     * @param url    请求地址
     * @param params 参数map
     * @return String 返回的结果
     */
    private static String get(String url, Map<String, String> params) {
        //构造HttpClient实例
        HttpClient client = new HttpClient();
        //参数不为空拼接参数
        if (params != null) {
            String paramStr = "";
            for (String key : params.keySet()) {
                paramStr += "&" + key + "=" + params.get(key);
            }
            paramStr = paramStr.substring(1);
            url += "?" + paramStr;
        }
        //创建GET方法的实例
        GetMethod method = new GetMethod(url);
        String result = execute(method, client);
        return result;
    }

    /**
     * 执行请求
     * @param method 连接
     * @param client 客户端
     * @return 请求结果
     */
    private static String execute(HttpMethod method, HttpClient client){
        String result = "";
        try {
            //设置请求头信息
            method.addRequestHeader("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            //执行HTTP GET方法请求
            client.executeMethod(method);
            //返回处理结果
            result = method.getResponseBodyAsString();
        } catch (HttpException e) {
            throw new RuntimeException("请检查输入的URL![" + e.getMessage() + "]");
        } catch (IOException e) {
            throw new RuntimeException("发生网络异常![" + e.getMessage() + "]");
        } finally {
            closeConnection(method, client);
        }
        return result;
    }

    /**
     * 释放所有资源
     *
     * @param method 链接
     * @param client 客户端
     */
    private static void closeConnection(HttpMethod method, HttpClient client) {
        //释放链接
        if (method != null) {
            method.releaseConnection();
            method = null;
        }
        //关闭HttpClient实例
        if (client != null) {
            ((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
            client = null;
        }
    }

    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpClientUtil.post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpClientUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpClientUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }
}
