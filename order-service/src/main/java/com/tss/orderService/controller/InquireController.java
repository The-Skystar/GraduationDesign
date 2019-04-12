package com.tss.orderService.controller;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.service.InquireService;
import com.tss.orderService.util.EncryptUtil;
import com.tss.orderService.util.HttpClientUtil;
import com.tss.orderService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/9 9:28
 * @description：快递查询控制层
 */
@RestController
public class InquireController {

    @Autowired
    private InquireService inquireService;

    @Autowired
    private ResultVO resultVO;

    @GetMapping("/query")
    public ResultVO expressInquire(String com,String nu) throws Exception {
//        resultVO.setCode(1);
//        resultVO.setData(JSONObject.parseObject(queryData(com, nu)));
        return inquireService.expressInquire(com,nu);
    }

    @GetMapping("/com")
    public ResultVO companyReco(String nu) throws Exception{
        return inquireService.companyReco(nu);
    }

    @GetMapping("/estimate")
    public ResultVO priceEstimate() throws Exception{
        return inquireService.priceEstimate("","","");
    }

    public static final String QUERYURL = "http://www.kuaidi100.com/query?";

    public static String setUrl(String logisticsCode, String logisticsNo) {
        String temp = String.valueOf(Math.random());
        StringBuilder sb = new StringBuilder(QUERYURL);
        sb.append("tpye=").append(logisticsCode).append("&");
        sb.append("postid=").append(logisticsNo).append("&");
        sb.append("temp=").append(temp);
        return sb.toString();
    }

    public static String queryData(String logisticsCode, String logisticsNo) {
        String line = "";
        String temp = String.valueOf(Math.random());
        String url = "http://www.kuaidi100.com/query?type=" + logisticsCode + "&postid=" + logisticsNo + "&temp=" + temp + "";
        try {
            URL realURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realURL.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    return line;
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return line;

    }
}
