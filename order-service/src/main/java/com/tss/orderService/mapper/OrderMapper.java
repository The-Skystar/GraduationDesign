package com.tss.orderService.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tss.orderService.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
