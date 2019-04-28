package com.tss.orderService.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.service.InquireService;
import com.tss.orderService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/9 14:11
 * @description：快递查询服务层
 */
@Service
public class InquireServiceImpl implements InquireService {

    @Autowired
    private ResultVO resultVO;

    @Override
    public ResultVO expressInquire(String com, String nu,String phone) throws Exception {
        String line = "";
        String temp = String.valueOf(Math.random());
        Map<String,String> parameters = new HashMap<>();
        parameters.put("type",com);
        parameters.put("postid",nu);
        parameters.put("temp",temp);
        parameters.put("phone",phone);
        parameters.put("platform","MWWW");
        parameters.put("id","1");
        String url = "http://m.kuaidi100.com/query" ;
//                "?type=" + com + "&postid=" + nu + "&temp=" + temp;
//        if (phone!=null&&phone.length()!=0)
//            url = url + "&phone=" + phone;
        try {
            URL realURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realURL.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
            conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//            conn.connect();
            String requestContent = "";
            Set<String> keys = parameters.keySet();
            for(String key : keys){
                requestContent = requestContent + key + "=" + parameters.get(key) + "&";
            }
            requestContent = requestContent.substring(0, requestContent.lastIndexOf("&"));
            conn.setDoOutput(true);
            DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
            //使用write(requestContent.getBytes())是为了防止中文出现乱码
            ds.write(requestContent.getBytes());
            ds.flush();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
//                    return line;
                    resultVO.setCode(999);
                    resultVO.setMsg("查询成功");
                    resultVO.setData(JSONObject.parseObject(line));
                    return resultVO;
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    @Override
    public ResultVO priceEstimate(String from, String to,String weight, String time) throws Exception {
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("startPlace",from);
        parameters.put("endPlace",to);
        parameters.put("weight",weight);
        parameters.put("startTime",time);
//        parameters.put("longitude","112.21737");
//        parameters.put("latitude","30.33354");
        parameters.put("token","guYsCE6pw-5n4LO9V0tc5L3hQ4gAnhcpKkRE4AOMUGw");
        parameters.put("platform","MWWW");
        String line = "";
        String url = "http://m.kuaidi100.com/apicenter/kdquerytools.do?method=priceandtimenew";
        URL realURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) realURL.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("accept", "application/json");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//        conn.connect();
        String requestContent = "";
        Set<String> keys = parameters.keySet();
        for(String key : keys){
            requestContent = requestContent + key + "=" + parameters.get(key) + "&";
        }
        requestContent = requestContent.substring(0, requestContent.lastIndexOf("&"));
        DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
        //使用write(requestContent.getBytes())是为了防止中文出现乱码
        ds.write(requestContent.getBytes());
        ds.flush();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
//                    return line;
                resultVO.setCode(999);
                resultVO.setMsg("查询成功");
                resultVO.setData(JSONObject.parseObject(line));
                return resultVO;
            }
            reader.close();
            conn.disconnect();
        }
        return null;
    }

    @Override
    public ResultVO companyReco(String nu) throws Exception {
        String line = "";
        String url = "https://www.kuaidi100.com/autonumber/autoComNum?resultv2=1&text="+nu;
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
//                    return line;
                resultVO.setCode(999);
                resultVO.setMsg("查询成功");
                resultVO.setData(JSONObject.parseObject(line));
                return resultVO;
            }
            reader.close();
            conn.disconnect();
        }
        return null;
    }
}
