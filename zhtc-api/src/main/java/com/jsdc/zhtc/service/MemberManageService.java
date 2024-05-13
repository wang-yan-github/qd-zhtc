package com.jsdc.zhtc.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.MemberManageDao;
import com.jsdc.zhtc.mapper.MemberManageMapper;
import com.jsdc.zhtc.model.MemberManage;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.vo.MemberManageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName: MemberManageService <br/>
 * Description: <br/>
 * date: 2022/1/4 11:56<br/>
 *
 * @author bn                                                                                                                        />
 */
@Service
@Transactional
public class MemberManageService extends BaseService<MemberManageDao, MemberManage> {

    @Autowired
    private MemberManageMapper memberManageMapper;

    @Autowired
    private OperateCarnoService operateCarnoService;

    /**
     * 描 述： TODO(获取当前用户信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link MemberManage}
     */
    public MemberManage getInspecter() {
        return (MemberManage) StpUtil.getSession().get(StpUtil.getLoginIdByToken(StpUtil.getTokenValue()).toString());
    }

    /**
     * 全会员数据
     *
     * @param memberManage
     * @return
     */
    public List<MemberManage> toList(MemberManage memberManage) {

        QueryWrapper<MemberManage> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(memberManage.getPhone())) {
            queryWrapper.eq("phone", memberManage.getPhone());
        }

        queryWrapper.eq("is_del", 0);

        return selectList(queryWrapper);
    }

    /**
     * 列表会员数据
     *
     * @param pageIndex
     * @param pageSize
     * @param memberManageVo
     * @return
     */
    public PageInfo<MemberManageVo> toList(Integer pageIndex, Integer pageSize, MemberManageVo memberManageVo) {
        PageHelper.startPage(pageIndex, pageSize);

        List<MemberManageVo> memberManageVos = memberManageMapper.toList(memberManageVo);

        memberManageVos.forEach(x -> {
            x.setOperateCarnos(operateCarnoService.selectList(
                    new QueryWrapper<OperateCarno>().
                            eq("member_id", x.getId()).eq("is_del", 0)));
        });

        PageInfo<MemberManageVo> page = new PageInfo<>(memberManageVos);

        return page;
    }

    /**
     * 描 述： TODO(查询当日注册人数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link java.lang.Integer}
     */
    public Integer getTodayAddCount() {

        return memberManageMapper.getTodayAddCount();
    }

    public Long getMemberTotality() {

        LambdaQueryWrapper<MemberManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberManage::getIs_del, 0);
        Long counts = this.selectCount(wrapper);


        return counts;
    }


    /**
     * create by zonglina at 2022/1/12 14:36
     * description:
     * 充值更新
     *
     * @return : null
     * @param:null
     */
    public Integer rechargeUp(Integer member_id, String recharge_amount, String gift_amount) {
        MemberManage memberManage = selectById(member_id);
        BigDecimal balance = new BigDecimal(recharge_amount).add(new BigDecimal(memberManage.getBalance()).add(new BigDecimal(gift_amount)));
        memberManage.setBalance(balance.toString());
        return updateById(memberManage);
    }


}
