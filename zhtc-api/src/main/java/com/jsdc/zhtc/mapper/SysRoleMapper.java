package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.SysRoleDao;
import com.jsdc.zhtc.model.SysMenu;
import com.jsdc.zhtc.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    @SelectProvider(method = "getMenusByRoleId", type = SysRoleDao.class)
    public List<SysMenu> getMenusByRoleId(Integer roleId);
}
