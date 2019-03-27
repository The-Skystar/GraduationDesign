package com.tss.user_service.service;

import com.baomidou.mybatisplus.service.IService;
import com.tss.user_service.entity.District;
import com.tss.user_service.vo.ResultVO;

import java.util.List;

public interface DistrictService extends IService<District> {

    /**
     * 查询所有省份
     * @return
     * @throws Exception
     */
    public ResultVO<List<District>> getProvince() throws Exception;

    /**
     * 根据父节点查询所有子节点
     * @param parent
     * @return
     * @throws Exception
     */
    public ResultVO<List<District>> getCity(String parent) throws Exception;

}
