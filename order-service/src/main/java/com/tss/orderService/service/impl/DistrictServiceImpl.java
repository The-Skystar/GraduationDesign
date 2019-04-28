package com.tss.orderService.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.orderService.Enum.DistrictEnums;
import com.tss.orderService.Enum.ErrorEnums;
import com.tss.orderService.entity.District;
import com.tss.orderService.exception.MyException;
import com.tss.orderService.mapper.DistrictMapper;
import com.tss.orderService.service.DistrictService;
import com.tss.orderService.vo.ResultVO;
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
    private ResultVO resultVO;

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

    @Override
    public ResultVO getPostcode(String districtCode) throws Exception {
        EntityWrapper<District> wrapper = new EntityWrapper<>();
        wrapper.eq("city_id",districtCode);
        List<District> list = districtMapper.selectList(wrapper);
        resultVO.setCode(600);
        resultVO.setMsg("获取邮编");
        if (!list.isEmpty())
            resultVO.setData(list.get(0).getZipcode());
        return resultVO;
    }


}
