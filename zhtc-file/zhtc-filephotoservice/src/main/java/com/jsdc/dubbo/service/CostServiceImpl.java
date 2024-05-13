package com.jsdc.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class CostServiceImpl implements CostService {

    @Override
    public Integer add(int cost) {
        return cost + 1000;
    }

}
