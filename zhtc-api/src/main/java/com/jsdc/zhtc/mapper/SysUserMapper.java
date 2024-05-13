package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.SysUserDao;
import com.jsdc.zhtc.model.SysRole;
import com.jsdc.zhtc.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yanbin123
 * @since 2019-08-14
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @SelectProvider(method = "getRoles", type = SysUserDao.class)
    public List<SysRole> getRoles(Integer userId);
}
