package com.tss.user_service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tss.user_service.Enum.ReturnStatusEnums;
import com.tss.user_service.config.DateConverterConfig;
import com.tss.user_service.entity.IdCard;
import com.tss.user_service.entity.User;
import com.tss.user_service.mapper.IdCardMapper;
import com.tss.user_service.mapper.UserMapper;
import com.tss.user_service.service.IdCardService;
import com.tss.user_service.util.FastJsonUtils;
import com.tss.user_service.util.HttpClientUtil;
import com.tss.user_service.util.IdcardUtils;
import com.tss.user_service.util.RedisUtil;
import com.tss.user_service.vo.ResultVO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/21 13:49
 * @description：身份证实名认证服务层
 */
@Service
public class IdCardServiceImpl extends ServiceImpl<IdCardMapper, IdCard> implements IdCardService {

    @Autowired
    private IdCard idCard;

    @Autowired
    private DateConverterConfig dateConverter;

    @Autowired
    private IdCardMapper idCardMapper;

    @Autowired
    private ResultVO resultVO;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getAuth(String ak, String sk) throws Exception{
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        URL realUrl = new URL(getAccessTokenUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : map.keySet()) {
            System.err.println(key + "--->" + map.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String result = "";
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        //返回结果示例
        System.err.println(result);
        JSONObject jsonObject = new JSONObject(result);
        String access_token = jsonObject.getString("access_token");
        return access_token;

    }

    @Override
    public ResultVO recCard(String accessToken, String params,String userId) throws Exception{
        // 身份证识别url
        String idcardIdentificate = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
        //线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
        String result = "";
        result = HttpClientUtil.post(idcardIdentificate, accessToken, params);
        idCard.setUserId(userId);
        idCard.setAddress(getCard(result,"住址"));
        if (getCard(result,"出生")!=null && getCard(result,"出生").length()==8)
            idCard.setBirthday(dateConverter.convert(dateFormat(getCard(result,"出生"))));
        idCard.setName(getCard(result,"姓名"));
        idCard.setId(getCard(result,"公民身份号码"));
        idCard.setSex(getCard(result,"性别"));
        idCard.setFamous(getCard(result,"民族"));
        if (getCard(result,"签发日期")!=null && getCard(result,"签发日期").length()==8)
            idCard.setIssued(dateConverter.convert(dateFormat(getCard(result,"签发日期"))));
        if (getCard(result,"失效日期")!=null && getCard(result,"失效日期").length()==8)
            idCard.setValidity(dateConverter.convert(dateFormat(getCard(result,"失效日期"))));
        idCard.setInst(getCard(result,"签发机关"));
        resultVO.setCode(ReturnStatusEnums.RECO_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.RECO_SUCCESS.getMsg());
        resultVO.setData(idCard);
        return resultVO;
    }

    @Override
    public ResultVO authen(String userId) throws Exception {
        IdCard idCardFront = FastJsonUtils.getJsonToBean(redisUtil.get(userId+"front"),IdCard.class);
        IdCard idCardBack = FastJsonUtils.getJsonToBean(redisUtil.get(userId+"back"),IdCard.class);
        boolean flag = isNotNull(idCardFront.getName()) && isNotNull(idCardFront.getSex()) &&
                isNotNull(idCardFront.getFamous()) && isNotNull(idCardFront.getAddress()) &&
                isNotNull(idCardFront.getId()) &&isNotNull(idCardBack.getInst()) &&
                idCardFront.getBirthday()!=null && idCardBack.getIssued()!=null && idCardBack.getValidity()!=null;
        if (!flag){
            resultVO.setCode(ReturnStatusEnums.RECO_NOT_COMPLETE.getCode());
            resultVO.setMsg(ReturnStatusEnums.RECO_NOT_COMPLETE.getMsg());
            resultVO.setData(null);
            return resultVO;
        }
        if (!IdcardUtils.validateCard(idCardFront.getId())){
            resultVO.setCode(ReturnStatusEnums.RECO_NOT_LEGAL.getCode());
            resultVO.setMsg(ReturnStatusEnums.RECO_NOT_LEGAL.getMsg());
            resultVO.setData(null);
            return resultVO;
        }
        User user = userMapper.selectById(userId);
        if (!idCardFront.getName().equals(user.getRealname())){
            resultVO.setCode(ReturnStatusEnums.NAME_NOT_MATCH.getCode());
            resultVO.setMsg(ReturnStatusEnums.NAME_NOT_MATCH.getMsg());
            resultVO.setData(null);
            return resultVO;
        }
        idCard.setName(idCardFront.getName());
        idCard.setSex(idCardFront.getSex());
        idCard.setFamous(idCardFront.getFamous());
        idCard.setBirthday(idCardFront.getBirthday());
        idCard.setAddress(idCardFront.getAddress());
        idCard.setId(idCardFront.getId());
        idCard.setIssued(idCardBack.getIssued());
        idCard.setValidity(idCardBack.getValidity());
        idCard.setInst(idCardBack.getInst());
        idCard.setUserId(userId);
        if (idCardMapper.insert(idCard)==1){
            resultVO.setCode(ReturnStatusEnums.AUTHEN_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.AUTHEN_SUCCESS.getMsg());
            resultVO.setData(idCard);
            return resultVO;
        }else {
            resultVO.setCode(ReturnStatusEnums.SYS_ERROR.getCode());
            resultVO.setMsg(ReturnStatusEnums.SYS_ERROR.getMsg());
            resultVO.setData(null);
            return resultVO;
        }
    }

    private String getCard(String res,String key){
        JsonParser parser = new JsonParser();
        JsonObject card = (JsonObject) parser.parse(res);
        if (card.get("words_result").getAsJsonObject().get(key)!=null)
            return card.get("words_result").getAsJsonObject().get(key).getAsJsonObject().get("words").getAsString();
        return null;
    }

    private String dateFormat(String date){
        return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
    }

    private boolean isNotNull(String str){
        boolean flag = false;
        if (str!=null && str.length()!=0)
            flag = true;
        return flag;
    }
}
