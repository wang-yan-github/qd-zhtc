package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.PatrolRechargeManagementDao;
import com.jsdc.zhtc.model.PatrolRechargeManagement;
import com.jsdc.zhtc.vo.PatrolRechargeManagementVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 巡检充值管理
 */
@Mapper
public interface PatrolRechargeManagementMapper extends BaseMapper<PatrolRechargeManagement> {
    @SelectProvider(type = PatrolRechargeManagementDao.class, method = "listPatrolRechargeManagement")
    List<Map<String, String>> listPatrolRechargeManagement(PatrolRechargeManagementVo vo);

    @SelectProvider(type = PatrolRechargeManagementDao.class, method = "countPatrolRechargeManagement")
    Map<String, String> countPatrolRechargeManagement(PatrolRechargeManagementVo vo);

    @SelectProvider(type = PatrolRechargeManagementDao.class, method = "countPatrolRechargeManagementTime")
    List<Map<String, String>> countPatrolRechargeManagementTime(PatrolRechargeManagementVo vo);

    @SelectProvider(type = PatrolRechargeManagementDao.class, method = "countPatrolRechargeManagementInject")
    List<Map<String, String>> countPatrolRechargeManagementInject(PatrolRechargeManagementVo vo);
}
