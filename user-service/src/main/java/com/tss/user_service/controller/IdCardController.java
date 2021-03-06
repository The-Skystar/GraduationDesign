package com.tss.user_service.controller;

import com.baidu.aip.util.Base64Util;
import com.tss.user_service.entity.IdCard;
import com.tss.user_service.service.IdCardService;
import com.tss.user_service.util.FastJsonUtils;
import com.tss.user_service.util.RedisUtil;
import com.tss.user_service.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/21 14:09
 * @description：实名认证控制层
 */
@RestController
public class IdCardController {

    @Autowired
    private IdCardService idCardService;

    @Autowired
    private IdCard idCard;

    @Autowired
    private ResultVO resultVO;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 识别身份证正面信息，并将识别结果存入redis
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/authenFront",headers = "Content-Type=multipart/form-data",method = RequestMethod.POST)
    public ResultVO authenFront(@RequestParam("file")MultipartFile file,@RequestParam("userId")String userId) throws Exception{
        String ak = "vvoaRorsjeDdYZCbDanHsSYp";
        String sk = "adURe3KwH9rzZBELTqvHIWjWaD1jUxzF";
        String path = "E://正.jpg";
//        byte[] imgData = FileUtil.readFileByBytes(path);
        byte[] imgData = file.getBytes();
        String imgStr = Base64Util.encode(imgData);
        // 识别身份证正面id_card_side=front;识别身份证背面id_card_side=back;
        String params = "id_card_side=front&" + URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(imgStr, "UTF-8");
        resultVO = idCardService.recCard(idCardService.getAuth(ak,sk),params,userId);
        idCard = (IdCard) resultVO.getData();
        System.err.println(idCard.toString());
        redisUtil.set(userId+"front", FastJsonUtils.getBeanToJson(idCard));
        return resultVO;
    }

    /**
     * 识别身份证反面信息，并将识别结果存入redis
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/authenBack")
    public ResultVO authenBack(@RequestParam("file")MultipartFile file,@RequestParam("userId")String userId) throws Exception{
        String ak = "vvoaRorsjeDdYZCbDanHsSYp";
        String sk = "adURe3KwH9rzZBELTqvHIWjWaD1jUxzF";
        String path = "E://反.jpg";
//        byte[] imgData = FileUtil.readFileByBytes(path);
        byte[] imgData = file.getBytes();
        String imgStr = Base64Util.encode(imgData);
        // 识别身份证正面id_card_side=front;识别身份证背面id_card_side=back;
        String params = "id_card_side=back&" + URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(imgStr, "UTF-8");
        resultVO = idCardService.recCard(idCardService.getAuth(ak,sk),params,userId);
        idCard = (IdCard) resultVO.getData();
        System.err.println(idCard.toString());
        redisUtil.set(userId+"back",FastJsonUtils.getBeanToJson(idCard));
        return resultVO;
    }

    /**
     * 与用户绑定进行实名认证,从redis中取出身份证正反面识别信息，并对信息进行校验
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/cer")
    public ResultVO authen(String userId) throws Exception{
        return idCardService.authen(userId);
    }



}
