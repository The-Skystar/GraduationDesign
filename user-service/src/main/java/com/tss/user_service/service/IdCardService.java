package com.tss.user_service.service;

import com.baomidou.mybatisplus.service.IService;
import com.tss.user_service.entity.IdCard;
import com.tss.user_service.vo.ResultVO;

public interface IdCardService extends IService<IdCard> {

    /**
     * 获取百度云access_token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取
     * @param ak 百度云获取的API Key
     * @param sk 百度云获取的Secret Key
     * @return
     */
    public String getAuth(String ak,String sk) throws Exception;
    /**
     * 身份证识别
     * @param accessToken
     * @param params
     * @return
     */
    public ResultVO recCard(String accessToken, String params,String userId) throws Exception;

    /**
     * 与用户绑定进行实名认证,从redis中取出身份证正反面识别信息，并对信息进行校验
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultVO authen(String userId) throws Exception;
}
