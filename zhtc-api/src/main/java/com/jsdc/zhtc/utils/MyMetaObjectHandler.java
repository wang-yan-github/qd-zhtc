package com.jsdc.zhtc.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 类 名: 自动填充时间
 * 描 述: MyMetaObjectHandler
 * 作 者: lw
 * 创 建：2022/1/4 9:55
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("start insert field....");
        this.setFieldValByName("create_time", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("start update field....");
        this.setFieldValByName("update_time", new Date(), metaObject);
    }

}
