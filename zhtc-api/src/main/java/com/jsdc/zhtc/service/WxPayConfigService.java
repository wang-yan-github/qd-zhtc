package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.WxPayConfigDao;
import com.jsdc.zhtc.model.WxPayConfig;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类 名: 微信支付配置
 * 描 述: WxPayConfigService
 * 作 者: lw
 * 创 建：2021/12/30 14:56
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@Transactional
public class WxPayConfigService extends BaseService<WxPayConfigDao, WxPayConfig> {

    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo selectAll(PageVo<WxPayConfig> data) {

        WxPayConfig bean = data.getBean();

        QueryWrapper<WxPayConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", GlobalData.ISDEL_NO);
        wrapper.orderByDesc("id");

        // 判断是否分页查询
        if (data.getPageSize() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());
            List<WxPayConfig> lists = this.selectList(wrapper);
            PageInfo<WxPayConfig> listPage = new PageInfo<>(lists);
            return ResultInfo.success(listPage);
        } else {
            List<WxPayConfig> lists = this.selectList(wrapper);
            return ResultInfo.success(lists);
        }
    }

    /**
     * 描 述： TODO(获取默认配置)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link WxPayConfig}
     */
    public WxPayConfig findWxPayConfig() {
        LambdaQueryWrapper<WxPayConfig> alWrapper = new LambdaQueryWrapper<>();
        alWrapper.eq(WxPayConfig::getIs_default, 0);
        WxPayConfig wxPayConfig = this.selectOne(alWrapper);
        return wxPayConfig;
    }

    /**
     * 描 述： TODO(根据id查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo selectById(WxPayConfig bean) {
        WxPayConfig data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error("未查询到数据");
        }
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return josn
     */
    public ResultInfo saveData(WxPayConfig bean) {

        bean.setIs_del(GlobalData.ISDEL_NO);
        if (bean.getIs_default() == null)
            bean.setIs_default(0);

        //判定是否为默认 默认将其他的全设置为非默认
        if (bean.getIs_default() == 0) {
            UpdateWrapper<WxPayConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("is_default", 1);
            int update = this.update(bean, updateWrapper);
            if (update <= 0)
                return ResultInfo.error("操作失败");
        }

        if (this.insert(bean) > 0) {
            return ResultInfo.success("数据添加成功");
        } else {
            return ResultInfo.success("message", "数据添加失败");
        }
    }

    /**
     * 描 述： TODO(根据id更新)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo updateInfo(WxPayConfig bean) {

        //如要删除提前判断是否为默认 为默认则不允许删除
        if (bean.getIs_del() != null && !bean.getIs_del().equals(GlobalData.ISDEL_YES)) {
            WxPayConfig alPayConfig = this.selectById(bean.getId());
            if (alPayConfig.getIs_default() == 0) {
                return ResultInfo.error("该项为默认支付账号！请先迁移默认项！");
            }
        }
        //判定是否为默认 默认将其他的全设置为非默认
        if (bean.getIs_default() != null && bean.getIs_default() == 0) {

            UpdateWrapper<WxPayConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("is_default", 1);
            int count = this.update(bean, updateWrapper);

            if (count <= 0)
                return ResultInfo.error("操作失败");

        }

        if (this.updateById(bean) > 0)
            return ResultInfo.success("操作成功");
        else
            return ResultInfo.error("操作成功");

    }

    /**
     * 描 述： TODO(根据id删除)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo deleById(WxPayConfig bean) {

        if (deleteById(bean.getId()) > 0) {
            return ResultInfo.success("删除成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }
}
