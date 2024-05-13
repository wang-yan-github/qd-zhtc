package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.SysMaterialDao;
import com.jsdc.zhtc.mapper.SysMaterialMapper;
import com.jsdc.zhtc.model.SysMaterial;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 素材管理
 *
 * @Author thr
 * @create 2022-12-01 10:52:56
 */
@Service
@Transactional
public class SysMaterialService extends BaseService<SysMaterialDao, SysMaterial> {

    @Autowired
    private SysMaterialMapper sysMaterialMapper;
    @Autowired
    private SysUserService sysUserService;

    public PageInfo<SysMaterial> toList(Integer pageIndex, Integer pageSize, SysMaterial beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<SysMaterial> sysMaterialVos = sysMaterialMapper.toList(beanParam);

        PageInfo<SysMaterial> page = new PageInfo<>(sysMaterialVos);

        return page;
    }

    public List<SysMaterial> getList(SysMaterial beanParam) {

        List<SysMaterial> sysMaterialVos = sysMaterialMapper.toList(beanParam);

        return sysMaterialVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<SysMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        SysMaterial sysMaterial = selectOne(queryWrapper);
        return ResultInfo.success(sysMaterial);
    }

    /**
     * 新增、修改
     */
    public ResultInfo edit(SysMaterial sysMaterial) {
        SysUser sysUser = sysUserService.getUser();
        if (Base.notEmpty(sysMaterial.getId())) {
            //修改
            sysMaterial.setUpdate_user(sysUser.getId());
            sysMaterial.setUpdate_time(new Date());
            if (updateById(sysMaterial) > 0) {
                return ResultInfo.success("修改成功！");
            } else {
                return ResultInfo.error("修改失败！");
            }
        } else {
            sysMaterial.setCreate_time(new Date());
            sysMaterial.setCreate_user(sysUser.getId());
            sysMaterial.setUpdate_time(new Date());
            sysMaterial.setUpdate_user(sysUser.getId());
            sysMaterial.setIs_del(GlobalData.ISDEL_NO);
            if (insert(sysMaterial) > 0) {
                return ResultInfo.success("新增成功！");
            } else {
                return ResultInfo.error("新增失败！");
            }
        }
    }

    /**
     * 批量删除
     */
    public ResultInfo delAll(String ids) {
        List<String> stringList = Arrays.asList(ids.split(","));
        UpdateWrapper<SysMaterial> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysMaterial::getIs_del, GlobalData.ISDEL_YES)
                .in(SysMaterial::getId, stringList);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除成功！");
        } else {
            return ResultInfo.error("删除失败！");
        }
    }
}
