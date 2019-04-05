package com.tss.orderService.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tss.orderService.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
