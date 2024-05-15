package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.CarOwnerDao;
import com.jsdc.zhtc.model.CarOwner;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CarOwnerService extends BaseService<CarOwnerDao, CarOwner> {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询
     */
    public ResultInfo selectAll(PageVo<CarOwner> data) {

        CarOwner bean = data.getBean();

        QueryWrapper<CarOwner> wrapper = new QueryWrapper<>();
        wrapper.eq("type", bean.getType());
        wrapper.like("name", bean.getName());
        wrapper.like("phone", bean.getPhone());
        wrapper.eq("is_del", GlobalData.ISDEL_NO);
        wrapper.orderByDesc("create_time");

        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());
            List<CarOwner> lists = selectList(wrapper);
            PageInfo<CarOwner> listPage = new PageInfo<>(lists);
            return ResultInfo.success(listPage);
        } else {
            List<CarOwner> lists = this.selectList(wrapper);
            return ResultInfo.success(lists);
        }
    }

    /**
     * 根据id查询
     */
    public ResultInfo selectById(CarOwner bean) {
        CarOwner data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error(null, "未查询到数据");
        }
    }

    /**
     * 新增数据
     */
    public ResultInfo saveData(CarOwner bean) {
        SysUser sysUser = sysUserService.getUser();
        bean.setIs_del(GlobalData.ISDEL_NO);
        bean.setCreate_time(new Date());
        bean.setUpdate_user(sysUser.getId());
        bean.setCreate_user_name(sysUser.getUser_name());

        if (this.insert(bean) > 0) {
            return ResultInfo.success(null, "数据添加成功");
        } else {
            return ResultInfo.success("message", "数据添加失败");
        }
    }

    /**
     * 根据id更新
     */
    public ResultInfo updateInfo(CarOwner bean) {
        SysUser sysUser = sysUserService.getUser();
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(sysUser.getId());
        bean.setUpdate_user_name(sysUser.getUser_name());

        if (this.updateById(bean) > 0) {
            return ResultInfo.success(null, "操作成功");
        } else {
            return ResultInfo.error(null, "操作成功");
        }

    }

    /**
     * 根据id删除
     */
    public ResultInfo deleById(CarOwner bean) {

        if (deleteById(bean.getId()) > 0) {
            return ResultInfo.success(null, "删除成功");
        } else {
            return ResultInfo.error(null, "操作失败");
        }
    }


}
