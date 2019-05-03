package com.tss.orderService.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tss.orderService.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

    List<Orders> searchByTime(String time,String status) throws Exception;

    List<Orders> searchAllColumn(String time,String status) throws Exception;
}
