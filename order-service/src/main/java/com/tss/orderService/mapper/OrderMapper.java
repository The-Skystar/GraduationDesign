package com.tss.orderService.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tss.orderService.entity.Orders;
import com.tss.orderService.vo.PayVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Time;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

    List<Orders> searchByTime(String time,String status) throws Exception;

    List<Orders> searchAllByTime(String time) throws Exception;

    List<Orders> searchAllColumn(String time,String status) throws Exception;

    Double costTotal(String time) throws Exception;

    List<PayVO> costTotalBet(String startTime, String endTime) throws Exception;

    double collection(String time) throws Exception;

    List<PayVO> collectionBet(String startTime,String endTime) throws Exception;
}
