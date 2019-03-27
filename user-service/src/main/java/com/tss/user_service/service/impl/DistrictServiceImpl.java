package com.tss.user_service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.user_service.Enum.DistrictEnums;
import com.tss.user_service.Enum.ErrorEnums;
import com.tss.user_service.entity.District;
import com.tss.user_service.exception.MyException;
import com.tss.user_service.mapper.DistrictMapper;
import com.tss.user_service.service.DistrictService;
import com.tss.user_service.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/25 10:49
 * @description：行政区划服务层
 */
@Service
public class DistrictServiceImpl extends ServiceImpl<DistrictMapper, District> implements DistrictService {

    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private ResultVO<List<District>> resultVO;

    @Override
    public ResultVO<List<District>> getProvince() throws Exception {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("level_type", DistrictEnums.PROCINCE.getCode());
        List<District> districtList = districtMapper.selectList(wrapper);
        if (districtList.size()==0)
            throw new MyException(ErrorEnums.SYS_ERROR);
        resultVO.setCode(1);
        resultVO.setMsg("获取省份成功");
        resultVO.setData(districtList);
        return resultVO;
    }

    @Override
    public ResultVO<List<District>> getCity(String parent) throws Exception {
        List<District> districtList = districtMapper.selectList(new EntityWrapper<District>().eq("parentid",parent));
        if (districtList.size()==0)
            throw new MyException(ErrorEnums.SYS_ERROR);
        resultVO.setCode(1);
        resultVO.setMsg("获取成功");
        resultVO.setData(districtList);
        return resultVO;
    }

}
