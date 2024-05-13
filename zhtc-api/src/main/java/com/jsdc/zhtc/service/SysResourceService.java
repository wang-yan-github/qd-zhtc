package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.SysResourceDao;
import com.jsdc.zhtc.mapper.SysResourceMapper;
import com.jsdc.zhtc.model.FileManage;
import com.jsdc.zhtc.model.SysResource;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ClassName: SysResourceService
 * Description:
 * date: 2021/12/30 10:55
 *
 * @author wh
 */
@Service
@Transactional
public class SysResourceService extends BaseService<SysResourceDao, SysResource> {
    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    private SysUserService userService;
    @Autowired
    private FileManageService fileManageService;
    @Value("${jsdc.loadPicPath2}")
    public String loadPicPath2;

    /**
     * create by wh at 2022/12/30 14:30
     * description: 新增系统资源
     *
     * @param sysResource
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo add(SysResource sysResource) {
        SysUser sysUser = userService.getUser();
        sysResource.setCreate_time(new Date());
        sysResource.setCreate_user(sysUser.getId());
        sysResource.setUpdate_time(new Date());
        sysResource.setUpdate_user(sysUser.getId());
        sysResource.setIs_del(GlobalData.ISDEL_NO);
        if (insert(sysResource) > 0) {
            return ResultInfo.success("新增系统资源成功！");
        } else {
            return ResultInfo.error("新增系统资源失败！");
        }
    }

    /**
     * create by wh at 2022/12/30 14:30
     * description: 编辑系统资源
     *
     * @param sysResource
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(SysResource sysResource) {
        SysUser sysUser = userService.getUser();
        if (Base.notEmpty(sysResource.getId())) {
            //修改
            sysResource.setUpdate_user(sysUser.getId());
            sysResource.setUpdate_time(new Date());
            if (updateById(sysResource) > 0) {
                String msg = "";
                //删除操作
                if (Base.notEmpty(sysResource.getIs_del()) && sysResource.getIs_del().equals("1")) {
                    SysResource bean = selectById(sysResource.getId());
                    if (Base.notEmpty(bean)) {
                        //类别 0：文档 1：操作指南 2:常见问题 3轮播图 4 公告资讯
                        if (bean.getCategory().equals("0")) {
                            msg = "文档管理";
                        } else if (bean.getCategory().equals("1")) {
                            msg = "操作指南";
                        } else if (bean.getCategory().equals("2")) {
                            msg = "常见问题";
                        } else if (bean.getCategory().equals("3")) {
                            msg = "轮播图管理";
                        } else if (bean.getCategory().equals("4")) {
                            msg = "公告资讯";
                        }
                    }
                }
                if (Base.notEmpty(msg)) {
                    return ResultInfo.success("编辑系统资源成功！", msg + "删除");
                } else {
                    return ResultInfo.success("编辑系统资源成功！");
                }
            } else {
                return ResultInfo.error("编辑系统资源失败！");
            }
        } else {
            sysResource.setCreate_time(new Date());
            sysResource.setCreate_user(sysUser.getId());
            sysResource.setUpdate_time(new Date());
            sysResource.setUpdate_user(sysUser.getId());
            sysResource.setIs_del(GlobalData.ISDEL_NO);
            sysResource.setIs_push(GlobalData.ISDEL_NO);
            if (insert(sysResource) > 0) {
                return ResultInfo.success("新增系统资源成功！");
            } else {
                return ResultInfo.error("新增系统资源失败！");
            }
        }
    }

    //分页查询
    public List<SysResource> selectPageList(SysResource bean, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        LambdaQueryWrapper<SysResource> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(bean.getTitle())) {
            wrapper.like(SysResource::getTitle, bean.getTitle());
        }
        if (StringUtils.isNotEmpty(bean.getCategory())) {
            wrapper.eq(SysResource::getCategory, bean.getCategory());
        }
        if (StringUtils.isNotEmpty(bean.getIs_push())) {
            wrapper.eq(SysResource::getIs_push, bean.getIs_push());
        }
        wrapper.eq(SysResource::getIs_del, GlobalData.ISDEL_NO);
        wrapper.orderByDesc(SysResource::getId);
        List<SysResource> list = this.selectList(wrapper);
        for (SysResource sysResource : list) {
            if (Base.notEmpty(sysResource.getPicture_id())) {
                String[] ids = sysResource.getPicture_id().split(",");
                if (Base.notEmpty(ids[0])) {
                    FileManage fileManage = fileManageService.selectById(Integer.parseInt(ids[0]));
                    if (Base.notEmpty(fileManage)) {
                        sysResource.setPic_url(loadPicPath2 + fileManage.getFile_url());
                    }
                }

            }
        }
        return list;
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public ResultInfo delAll(String ids) {
        List<String> stringList = Arrays.asList(ids.split(","));
        UpdateWrapper<SysResource> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysResource::getIs_del, GlobalData.ISDEL_YES)
                .in(SysResource::getId, stringList);
        if (update(null, wrapper) > 0) {
            String msg = "";
            //删除操作
            SysResource bean = selectById(Integer.parseInt(stringList.get(0)));
            if (Base.notEmpty(bean)) {
                //类别 0：文档 1：操作指南 2:常见问题 3轮播图 4 公告资讯
                if (bean.getCategory().equals("0")) {
                    msg = "文档管理";
                } else if (bean.getCategory().equals("1")) {
                    msg = "操作指南";
                } else if (bean.getCategory().equals("2")) {
                    msg = "常见问题";
                } else if (bean.getCategory().equals("3")) {
                    msg = "轮播图管理";
                } else if (bean.getCategory().equals("4")) {
                    msg = "公告资讯";
                }
            }
            return ResultInfo.success("删除成功！", msg + "批量删除");
        } else {
            return ResultInfo.error("删除失败！");
        }
    }

    /**
     * 获取一条数据
     */
    public SysResource selectBean(SysResource bean) {
        QueryWrapper<SysResource> wrapper = new QueryWrapper<>();
        wrapper.eq("category", bean.getCategory());
        if (Base.notEmpty(bean.getDoc_type())) {
            wrapper.eq("doc_type", bean.getDoc_type());
        }
        if (Base.notEmpty(bean.getIs_push())) {
            wrapper.eq("is_push", bean.getIs_push());
        }
        wrapper.eq("is_del", GlobalData.ISDEL_NO);
        wrapper.orderByDesc("id");
        List<SysResource> list = this.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return bean;
        }
    }

}
