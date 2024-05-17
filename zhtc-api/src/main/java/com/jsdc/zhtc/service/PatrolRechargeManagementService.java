package com.jsdc.zhtc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.PatrolRechargeManagementDao;
import com.jsdc.zhtc.mapper.PatrolRechargeManagementMapper;
import com.jsdc.zhtc.model.InspectManage;
import com.jsdc.zhtc.model.PatrolRechargeManagement;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.PatrolRechargeManagementVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 巡检充值管理
 */
@Service
@Transactional
public class PatrolRechargeManagementService extends BaseService<PatrolRechargeManagementDao, PatrolRechargeManagement> {

    @Autowired
    PatrolRechargeManagementMapper rechargeManagementMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    InspectManageService inspectManageService;

    /**
     * 查询查看
     *
     * @param pageIndex
     * @param pageSize
     * @param vo
     * @return
     */
    public ResultInfo listPatrolRechargeManagement(Integer pageIndex, Integer pageSize, PatrolRechargeManagementVo vo) {
        PageHelper.startPage(pageIndex, pageSize);
        vo.setParking_type("1");
        List<Map<String, String>> list = rechargeManagementMapper.listPatrolRechargeManagement(vo);
        list.forEach(x -> {
            if (null != x.get("recharge_amount")) {
                x.put("recharge_amount", new BigDecimal(String.valueOf(x.get("recharge_amount"))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        });
        PageInfo pageInfo = new PageInfo(list);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 统计
     *
     * @param vo
     * @return 充值人数 充值笔数 充值笔数
     */
    public ResultInfo countPatrolRechargeManagement(PatrolRechargeManagementVo vo) {
        vo.setParking_type("1");
        Map<String, String> map = rechargeManagementMapper.countPatrolRechargeManagement(vo);
        if (null != map.get("recharge_amount")) {
            map.put("recharge_amount", new BigDecimal(String.valueOf(map.get("recharge_amount"))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        return ResultInfo.success(map);
    }

    /**
     * 统计 按时间
     *
     * @param vo
     * @return
     */
    public ResultInfo countPatrolRechargeManagementTime(Integer pageIndex, Integer pageSize, PatrolRechargeManagementVo vo) {
        PageHelper.startPage(pageIndex, pageSize);
        vo.setParking_type("1");
        List<Map<String, String>> list = rechargeManagementMapper.countPatrolRechargeManagementTime(vo);
        list.forEach(x -> {
            if (null != x.get("recharge_amount")) {
                x.put("recharge_amount", new BigDecimal(String.valueOf(x.get("recharge_amount"))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        });
        PageInfo pageInfo = new PageInfo(list);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 统计 按收费员
     *
     * @param vo
     * @return
     */
    public ResultInfo countPatrolRechargeManagementInject(Integer pageIndex, Integer pageSize, PatrolRechargeManagementVo vo) {
        PageHelper.startPage(pageIndex, pageSize);
        vo.setParking_type("1");
        List<Map<String, String>> list = rechargeManagementMapper.countPatrolRechargeManagementInject(vo);
        list.forEach(x -> {
            if (null != x.get("recharge_amount")) {
                x.put("recharge_amount", new BigDecimal(String.valueOf(x.get("recharge_amount"))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        });
        PageInfo pageInfo = new PageInfo(list);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 巡检充值
     *
     * @param vo
     * @return
     */
    public ResultInfo insertPatrolRechargeManagement(PatrolRechargeManagementVo vo) {
        PatrolRechargeManagement patrolRechargeManagement = new PatrolRechargeManagement();
        patrolRechargeManagement.setInject_id(Integer.valueOf(vo.getInspectId()));
        patrolRechargeManagement.setBefore_account_balance(0);
        patrolRechargeManagement.setRecharge_amount(Integer.valueOf(vo.getRechargeAmount()));
        patrolRechargeManagement.setAfter_account_balance(Integer.valueOf(vo.getRechargeAmount()));
        patrolRechargeManagement.setRecharge_mode("1");
        patrolRechargeManagement.setRecharge_time(new Date());
        patrolRechargeManagement.setReceive_paper_invoice("0");
        patrolRechargeManagement.setIs_del("0");
        String logMsg = inspectManageService.selectById(vo.getInspectId()).getName() + "的发票充值金额为" + vo.getRechargeAmount() + "";
        return ResultInfo.success(patrolRechargeManagement.insert(), logMsg);
    }

    /**
     * 发票领取管理
     *
     * @param PatrolRechargeManagementId
     * @return
     */
    public ResultInfo saveInvoiceReceiving(Integer PatrolRechargeManagementId) {
        PatrolRechargeManagement patrolRechargeManagement = selectById(PatrolRechargeManagementId);
        patrolRechargeManagement.setReceive_paper_invoice("1");
        patrolRechargeManagement.updateById();

        //往巡检员管理 充值发票余额
        InspectManage inspectManage = inspectManageService.selectById(patrolRechargeManagement.getInject_id());
        BigDecimal invoice_balance_val = new BigDecimal("0");
        BigDecimal invoice_balance = new BigDecimal(inspectManage.getInvoice_balance() == null ? "0" : inspectManage.getInvoice_balance());
        BigDecimal recharge_amount = new BigDecimal(patrolRechargeManagement.getRecharge_amount());
        invoice_balance_val = invoice_balance.add(recharge_amount);
        inspectManage.setInvoice_balance(String.valueOf(invoice_balance_val));
        String logMsg = inspectManage.getName() + "的领取发票登记金额为" + recharge_amount + "";
        return ResultInfo.success(inspectManage.updateById(), logMsg);
    }

    /**
     * 编辑
     *
     * @param patrolRechargeManagement
     * @return
     */
    public ResultInfo savePatrolRechargeManagement(PatrolRechargeManagement patrolRechargeManagement) {
        patrolRechargeManagement.updateById();
        return ResultInfo.success(true);
    }

    /**
     * 软删除
     *
     * @param PatrolRechargeManagementId
     * @return
     */
    public ResultInfo removePatrolRechargeManagement(Integer PatrolRechargeManagementId) {
        PatrolRechargeManagement patrolRechargeManagement = new PatrolRechargeManagement();
        patrolRechargeManagement.setId(PatrolRechargeManagementId);
        patrolRechargeManagement.deleteById();
        return ResultInfo.success(true);
    }

    /**
     * 充值验证
     *
     * @param password
     * @param code
     * @return
     */
    public ResultInfo getInspectPwdCode(String password, String code) {
        SysUser loginUser = sysUserService.getUser();
        if (password != null && password.equals(loginUser.getRecharge_pwd())) {
            return ResultInfo.success(true);
        }
        return ResultInfo.error("充值验证失败");
    }


}

