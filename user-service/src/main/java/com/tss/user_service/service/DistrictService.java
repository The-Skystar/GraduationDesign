package com.tss.user_service.service;

import com.baomidou.mybatisplus.service.IService;
import com.tss.user_service.entity.Address;
import com.tss.user_service.entity.District;
import com.tss.user_service.vo.ResultVO;

import java.util.List;

public interface DistrictService extends IService<District> {

    public ResultVO<List<District>> getProvince() throws Exception;

    public ResultVO<List<District>> getCity(String province) throws Exception;

}
