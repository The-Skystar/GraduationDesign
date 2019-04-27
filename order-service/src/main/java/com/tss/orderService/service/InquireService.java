package com.tss.orderService.service;

import com.tss.orderService.vo.ResultVO;

public interface InquireService {

    /**
     * 根据订单号查询物流信息
     * @param com
     * @param nu
     * @return
     * @throws Exception
     */
    public ResultVO expressInquire(String com,String nu,String phone) throws Exception;

    /**
     * 时效价格查询
     * @param from
     * @param to
     * @param time
     * @return
     * @throws Exception
     */
    public ResultVO priceEstimate(String from,String to,String weight,String time) throws Exception;

    /**
     * 智能识别快递公司
     * @param nu
     * @return
     * @throws Exception
     */
    public ResultVO companyReco(String nu) throws Exception;
}
