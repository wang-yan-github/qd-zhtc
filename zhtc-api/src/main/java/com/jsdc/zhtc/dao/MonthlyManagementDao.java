package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.MonthlyManagement;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.MonthlyManagementVo;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;

/**
 * 类 名: 包月管理
 * 描 述: MonthlyManagementDao
 * 作 者: lw
 * 创 建：2022/1/4 10:17
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Repository
public class MonthlyManagementDao extends BaseDao<MonthlyManagement> {

    /**
     * 描 述： TODO(包月充值统计 按时间)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getTimeCensus(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select  tims as gpby , ( ");
        sqlbd.append("  select count(1) ss from ( ");

        sqlbd.append("   select t6.carno_id  from monthly_management t6 ");
        sqlbd.append("   LEFT JOIN paymonthly_config t7 ON t6.paymonthly_config_id = t7.id ");
        sqlbd.append(" where CONVERT(varchar(100), t6.create_time, 23) =t3.tims  and t6.is_del=0 ");
        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t7.parking_type = '").append(data.getStr2()).append("' ");
        sqlbd.append("  GROUP BY t6.carno_id  ) as t ");

        sqlbd.append(" ) as carnoNum ");
        sqlbd.append(" ,count( 1 ) as quantum,sum( convert(decimal(15, 2), t3.total_cost) ) as amount ");
        sqlbd.append(" from  (");
        sqlbd.append("  select  CONVERT(varchar(100), t1.create_time, 23) tims , t2.car_no, t1.* from  monthly_management t1");
        sqlbd.append("  LEFT JOIN operate_carno t2 on t1.carno_id = t2.id where 1=1");

        if (StringUtils.isNotBlank(data.getCarNo()))
            sqlbd.append(" and t2.car_no =  '").append(data.getCarNo()).append("' ");

        sqlbd.append(" ) t3");
        sqlbd.append(" left join operate_carno t2 on t3.carno_id = t2.id ");
        sqlbd.append(" left join paymonthly_config t4 on t3.paymonthly_config_id = t4.id ");
        //sqlbd.append(" left join paymonthly_parkingplace t5 on t4.id = t5.paymonthly_config_id ");

        sqlbd.append(" where 1=1 ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and tims >=  '").append(data.getStartTime()).append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and tims <=  '").append(data.getEndTime()).append("' ");

        if (data.getVariance() != null || data.getVariance3() != null) {
            sqlbd.append(" and t4.id in ( ");
            sqlbd.append("  select paymonthly_config_id from paymonthly_parkingplace where parkingplace_id in (  ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null)
                sqlbd.append(" and t1.id = ").append(data.getVariance3());
            sqlbd.append("   ) ");

            sqlbd.append(" ) ");

        }

        sqlbd.append("  and t3.is_del = ").append(GlobalData.ISDEL_NO);

        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");

        sqlbd.append(" GROUP BY tims");
        sqlbd.append(" ORDER BY tims desc");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(包月充值统计 按车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getLicenceCensus(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select t2.car_no as gpby, t2.car_type carType, count(1) as quantum, sum( convert(decimal(15, 2), t1.total_cost) ) as amount ");
        sqlbd.append(" from monthly_management t1 ");
        sqlbd.append(" LEFT JOIN operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append(" left join paymonthly_config t4 on t1.paymonthly_config_id = t4.id ");

        sqlbd.append(" where 1=1  ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and CONVERT(varchar, t1.create_time, 23) >=  '").append(data.getStartTime()).append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and CONVERT(varchar, t1.create_time, 23) <=  '").append(data.getEndTime()).append("' ");

        if (StringUtils.isNotBlank(data.getCarNo()))
            sqlbd.append(" and t2.car_no =  '").append(data.getCarNo()).append("' ");

        if (data.getVariance() != null || data.getVariance3() != null) {
            sqlbd.append(" and t4.id in ( ");
            sqlbd.append("  select paymonthly_config_id from paymonthly_parkingplace where parkingplace_id in (  ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null)
                sqlbd.append(" and t1.id = ").append(data.getVariance3());
            sqlbd.append("   ) ");

            sqlbd.append(" ) ");

        }

        sqlbd.append("  and t1.is_del = ").append(GlobalData.ISDEL_NO);
        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");

        sqlbd.append(" GROUP BY  t2.car_no , t2.car_type");

        return sqlbd.toString();
    }


    /**
     * 描 述： TODO(车牌总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getLicenceCount(CommonVo data) {


        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select count(1) as  count1 from ( ");
        sqlbd.append("  select DISTINCT  carno_id from monthly_management t1 ");
        sqlbd.append("   LEFT JOIN operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append("  left join paymonthly_config t4 on t1.paymonthly_config_id = t4.id ");
        //sqlbd.append("  left join paymonthly_parkingplace t5 on t4.id = t5.paymonthly_config_id ");
        sqlbd.append("  where 1=1  ");
        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append("  and CONVERT(varchar, t1.create_time, 23) >=  '").append(data.getStartTime()).append("' ");

        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append("  and CONVERT(varchar, t1.create_time, 23) <=  '").append(data.getEndTime()).append("' ");

        if (StringUtils.isNotBlank(data.getCarNo()))
            sqlbd.append("  and t2.car_no =  '").append(data.getCarNo()).append("' ");

        if (data.getVariance() != null || data.getVariance3() != null) {
            sqlbd.append(" and t4.id in ( ");
            sqlbd.append("  select paymonthly_config_id from paymonthly_parkingplace where parkingplace_id in (  ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null)
                sqlbd.append(" and t1.id = ").append(data.getVariance3());
            sqlbd.append("   ) ");

            sqlbd.append(" ) ");

        }

        sqlbd.append("  and t1.is_del = ").append(GlobalData.ISDEL_NO);

        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");

        sqlbd.append(" ) t1 ");

        return sqlbd.toString();

    }


    /**
     * 描 述： TODO(充值笔数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getRechargeCount(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select count(1) as  count1 from ( ");
        sqlbd.append("  select DISTINCT payment_id from monthly_management t1 ");
        sqlbd.append("   LEFT JOIN operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append("  left join paymonthly_config t4 on t1.paymonthly_config_id = t4.id ");
        //sqlbd.append("  left join paymonthly_parkingplace t5 on t4.id = t5.paymonthly_config_id ");
        sqlbd.append("  where 1=1  ");
        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append("  and CONVERT(varchar, t1.create_time, 23) >=  '").append(data.getStartTime()).append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append("  and CONVERT(varchar, t1.create_time, 23) <=  '").append(data.getEndTime()).append("' ");

        if (StringUtils.isNotBlank(data.getCarNo()))
            sqlbd.append("  and t2.car_no =  '").append(data.getCarNo()).append("' ");

        if (data.getVariance() != null || data.getVariance3() != null) {
            sqlbd.append(" and t4.id in ( ");
            sqlbd.append("  select paymonthly_config_id from paymonthly_parkingplace where parkingplace_id in (  ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null)
                sqlbd.append(" and t1.id = ").append(data.getVariance3());
            sqlbd.append("   ) ");

            sqlbd.append(" ) ");

        }

        sqlbd.append("  and t1.is_del = ").append(GlobalData.ISDEL_NO);

        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");

        sqlbd.append(" ) t1 ");

        return sqlbd.toString();

    }

    /**
     * 描 述： TODO(充值总额)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getRechargeAmount(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select  sum( convert(decimal(15, 2), t1.total_cost) ) as amount  from monthly_management t1");
        sqlbd.append("  LEFT JOIN operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append("  left join paymonthly_config t4 on t1.paymonthly_config_id = t4.id ");
        // sqlbd.append("  left join paymonthly_parkingplace t5 on t4.id = t5.paymonthly_config_id ");
        sqlbd.append(" where 1=1  ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append("  and CONVERT(varchar, t1.create_time, 23) >=  '").append(data.getStartTime()).append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append("  and CONVERT(varchar, t1.create_time, 23) <=  '").append(data.getEndTime()).append("' ");

        if (StringUtils.isNotBlank(data.getCarNo()))
            sqlbd.append("  and t2.car_no =  '").append(data.getCarNo()).append("' ");

        if (data.getVariance() != null || data.getVariance3() != null) {
            sqlbd.append(" and t4.id in ( ");
            sqlbd.append("  select paymonthly_config_id from paymonthly_parkingplace where parkingplace_id in (  ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null)
                sqlbd.append(" and t1.id = ").append(data.getVariance3());
            sqlbd.append("   ) ");

            sqlbd.append(" ) ");

        }

        sqlbd.append("  and t1.is_del = ").append(GlobalData.ISDEL_NO);

        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO( 用户包月管理信息 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data variance 区域 variance2 街道 variance3 路段
     * @return {@link String}
     */
    public String selectManageInfo(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select distinct t2.car_no carNo , t2.car_type carType,  t3.login_name userName," +
                " t6.payment_type paymentType ,t7.company_name company_name, t6.amount ," +
                " t1.* from monthly_management t1 ");
        sqlbd.append(" left join operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append(" left join sys_user t3 on t1.create_user = t3.id ");
        sqlbd.append(" left join paymonthly_config t4 on t1.paymonthly_config_id = t4.id ");
        sqlbd.append(" left join paymonthly_parkingplace t5 on t4.id = t5.paymonthly_config_id ");
        sqlbd.append(" left join payment_order t6 on t1.payment_id = t6.id and t6.is_del='0' and t6.status='2'");
        sqlbd.append(" left join operate_company t7 on t1.companyId = t7.id ");

        sqlbd.append(" where 1=1  and t1.isTheCompany = 1");
        if (StringUtils.isNotBlank(data.getStr())) {
            sqlbd.append(" and t2.car_no like '%").append(data.getStr()).append("' ");

        }
        if (StringUtils.isNotBlank(data.getDwName())) {
            sqlbd.append(" and t1.dwName like '%").append(data.getDwName()).append("%' ");

        }
        if (data.getVariance() != null || data.getVariance3() != null) {
            //if ( data.getStr2().equals("1") ){
            sqlbd.append(" and t5.parkingplace_id in ( ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null)
                sqlbd.append(" and t1.id = ").append(data.getVariance3());

            sqlbd.append(" ) ");
            //  }
        }

        sqlbd.append(" and t1.is_del = ").append(GlobalData.ISDEL_NO);
        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");

        sqlbd.append(" order by t1.start_time desc ");
        System.out.println(sqlbd.toString());
        return sqlbd.toString();
    }

    //企业包月
    public String selectManageInfo2(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select distinct t1.monthly_code,t1.paymonthly_config_id,t1.months,t1.companyId,t1.isTheCompany,  t3.login_name userName, t6.payment_type paymentType ,t7.company_name company_name ,t6.amount , t1.start_time,t1.end_time from monthly_management t1 ");
        sqlbd.append(" left join operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append(" left join sys_user t3 on t1.create_user = t3.id ");
        sqlbd.append(" left join paymonthly_config t4 on t1.paymonthly_config_id = t4.id ");
        sqlbd.append(" left join paymonthly_parkingplace t5 on t4.id = t5.paymonthly_config_id ");
        sqlbd.append(" left join payment_order t6 on t1.payment_id = t6.id  and t6.is_del='0' and t6.status='2'");
        sqlbd.append(" left join operate_company t7 on t1.companyId = t7.id ");

        sqlbd.append(" where 1=1 and t1.isTheCompany = 2");
        if (StringUtils.isNotBlank(data.getStr())) {
            sqlbd.append(" and car_no= '").append(data.getStr()).append("' ");

        }
        if (data.getVariance() != null || data.getVariance3() != null) {
            //if ( data.getStr2().equals("1") ){
            sqlbd.append(" and t5.parkingplace_id in ( ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null) {
                sqlbd.append(" and t1.id = ").append(data.getVariance3());
            }


            sqlbd.append(" ) ");
            //  }
        }

        sqlbd.append(" and t1.is_del = ").append(GlobalData.ISDEL_NO);
        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");

        sqlbd.append(" order by t1.start_time desc ");
        System.out.println(sqlbd.toString());
        return sqlbd.toString();
    }

    public String info(String monthly_code) {
        String sql = "SELECT \n" +
                "\t\t\n" +
                "\t\t\tt2.car_no carNo,t2.car_type carType\n" +
                "\t\tFROM\n" +
                "\t\t\tmonthly_management t1\n" +
                "LEFT JOIN operate_carno t2 ON t1.carno_id = t2.id\n" +
                "WHERE t1.isTheCompany = '2' and t1.monthly_code = '" + monthly_code + "'";
        return sql;
    }

    public String num(String monthly_code) {
        String sql = "SELECT carno_id carId ,num\n" +
                "\t\tFROM\n" +
                "\t\t\tmonthly_management \n" +
                "where monthly_code='" + monthly_code + "' and isTheCompany = 2 ";
        return sql;
    }

    /**
     * 描 述： TODO( 用户包月管理信息 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String findManageInfo(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select t2.car_no carNo , t1.* from monthly_management t1 ");
        sqlbd.append(" left join operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append(" where t1.id = ").append(data.getVariance());
        sqlbd.append(" and t1.is_del = ").append(GlobalData.ISDEL_NO);
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(查询在包月车辆总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link String}
     */
    public String getNormalMPCount(CommonVo data) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append("select count( DISTINCT t1.carno_id ) counts from monthly_management t1");
        sqlbd.append(" left join operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append(" left join paymonthly_config t4 on t1.paymonthly_config_id = t4.id ");
        sqlbd.append(" left join paymonthly_parkingplace t5 on t4.id = t5.paymonthly_config_id ");

        sqlbd.append(" where 1=1 ");

        if (StringUtils.isNotBlank(data.getStr())) {
            sqlbd.append(" and t2.car_no= '").append(data.getStr()).append("' ");

        }
        if (data.getVariance() != null || data.getVariance3() != null) {
            //if ( data.getStr2().equals("1") ){
            sqlbd.append(" and t5.parkingplace_id in ( ");

            sqlbd.append(" select t1.id from park t1 ");

            sqlbd.append(" left join street t2 on t1.street_id = t2.id ");
            sqlbd.append(" left join area t3 on t2.area_id = t3.id ");
            sqlbd.append(" where 1=1 ");
            if (data.getVariance() != null)
                sqlbd.append(" and t3.id = ").append(data.getVariance());
            if (data.getVariance2() != null)
                sqlbd.append(" and t2.id = ").append(data.getVariance2());
            if (data.getVariance3() != null)
                sqlbd.append(" and t1.id = ").append(data.getVariance3());

            sqlbd.append(" ) ");
            //  }
        }
        //sqlbd.append( " and CONVERT(varchar, end_time , 120) >= '" ).append(sdf.format(new Date())).append("' ");
        sqlbd.append(" and t1.is_del = ").append(GlobalData.ISDEL_NO);
        if (StringUtils.isNotBlank(data.getStr2()))
            sqlbd.append(" and t4.parking_type = '").append(data.getStr2()).append("' ");
        return sqlbd.toString();
    }


    //根据停车id或者路段id、车牌id查询是否包月
    public String selectByCarId(Integer car_id, Integer parkingplace_id, String parkType, String dateString) {
        String sql = "select  count(1) from monthly_management met LEFT JOIN paymonthly_config c on met.paymonthly_config_id=c.id " +
                " LEFT JOIN paymonthly_parkingplace p on c.id = p.paymonthly_config_id where met.is_del='0' " +
                " and met.carno_id= " + car_id + " and p.parkingplace_id= " + parkingplace_id + " and c.parking_type= " + parkType;
        sql += " and ( met.start_time <='" + dateString + "' and  met.end_time>='" + dateString + "')";
        return getQueryHandler(sql).getSql();
    }

    //根据停车id或者路段id、车牌id查询是否包月
    public String selectInfoByCarId(Integer car_id, Integer parkingplace_id, String parkType, String dateString) {
        String sql = "select  met.isTheCompany,met.companyId from monthly_management met LEFT JOIN paymonthly_config c on met.paymonthly_config_id=c.id " +
                " LEFT JOIN paymonthly_parkingplace p on c.id = p.paymonthly_config_id where met.is_del='0' " +
                " and met.carno_id= " + car_id + " and p.parkingplace_id= " + parkingplace_id + " and c.parking_type= " + parkType;
        sql += " and ( met.start_time <='" + dateString + "' and  met.end_time>='" + dateString + "')";
        sql += " order by met.isTheCompany desc";
        return getQueryHandler(sql).getSql();
    }

    /**
     * 微信小程序
     * 月卡订单列表
     * 已开、已失效
     * timeType 时间类型 0使用中 1已失效
     *
     * @author thr
     */
    public String selectPageList(MonthlyManagementVo bean) {
        String sql = "SELECT\n" +
                "\tc.name configName,\n" +
                "\tc.category,\n" +
                "\tc.parking_type,\n" +
                "\tc.price,\n" +
                "\tc.start_time sTime,\n" +
                "\tc.end_time eTime,\n" +
                "\to.car_no carNo,\n" +
                "\tCASE  WHEN m.end_time < GETDATE() THEN '1' Else '0' END as timeType,\n" +
                "\tm.*\n" +
                "FROM\n" +
                "\tpaymonthly_config c\n" +
                "\tLEFT JOIN monthly_management m ON c.id = m.paymonthly_config_id\n" +
                "\tLEFT JOIN operate_carno o ON o.id = m.carno_id \n" +
                "WHERE\n" +
                "\tc.is_del= '0' \n" +
                "\tAND m.is_del= '0'";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getCarIds())) {
                sql += " and m.carno_id in (" + bean.getCarIds() + ")";
            }
            // 时间类型 0使用中 1已失效
            if (notEmpty(bean.getTimeType())) {
                if (bean.getTimeType().equals("0")) {
                    sql += " and m.end_time >= GETDATE()";
                } else {
                    sql += " and m.end_time < GETDATE()";
                }
            }
        }
        sql += " ORDER BY m.end_time desc";
        return getQueryHandler(sql).getSql();
    }

    /**
     * 描 述： TODO(查询包月统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param parkinType 统计类型
     * @param dateStr    创建时间 年月日
     * @return {@link String}
     */
    public String getMonthlyPaymentCount(String parkinType, String dateStr, Integer park_id) {
        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select count(1) counts from monthly_management t1 ");
        sqlbd.append(" left join paymonthly_config t2 on t1.paymonthly_config_id = t2.id ");
        sqlbd.append(" where t1.is_del = 0 ");
        if (parkinType != null)
            sqlbd.append(" and t2.parking_type = ").append(parkinType);

        if (StringUtils.isNotBlank(dateStr))
            sqlbd.append(" and CONVERT(varchar, t1.create_time, 23) = '").append(dateStr).append("'  ");

        if (park_id != null) {
            sqlbd.append(" and t1.paymonthly_config_id in ( ");
            sqlbd.append("  SELECT\n" +
                    "\t\tpa.paymonthly_config_id \n" +
                    "\tFROM\n" +
                    "\t\tpaymonthly_config p\n" +
                    "\t\tLEFT JOIN paymonthly_parkingplace pa ON p.id = pa.paymonthly_config_id \n" +
                    "\tWHERE\n" +
                    "\t\tp.is_del = 0 \n" +
                    "\t\tAND pa.is_del = 0 ");
            if (notEmpty(parkinType)) {
                sqlbd.append("\t\tAND p.parking_type = \n").append(parkinType);
            }
            sqlbd.append("\tAND pa.parkingplace_id IN ( ").append(park_id);
            sqlbd.append("  ) ");
            sqlbd.append(" ) ");
        }

        return sqlbd.toString();

    }

    /**
     * 描 述： TODO(合计包月总费用)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param parkinType 统计类型
     * @param dateStr    创建时间 年月日
     * @return {@link String}
     */
    public String getMonthlyPaymentSumCost(String parkinType, String dateStr, Integer park_id) {
        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select sum( CONVERT(decimal( 15 , 2 ) , t1.total_cost ) ) sumTotalCost from monthly_management t1 ");
        sqlbd.append(" left join paymonthly_config t2 on t1.paymonthly_config_id = t2.id ");
        sqlbd.append(" where t1.is_del = ").append(GlobalData.ISDEL_NO);
        if (parkinType != null)
            sqlbd.append(" and t2.parking_type = ").append(parkinType);

        if (StringUtils.isNotBlank(dateStr))
            sqlbd.append(" and CONVERT(varchar, t1.create_time, 23) = '").append(dateStr).append("'  ");

        if (park_id != null) {
            sqlbd.append(" and t1.paymonthly_config_id in ( ");
            sqlbd.append("  SELECT\n" +
                    "\t\tpa.paymonthly_config_id \n" +
                    "\tFROM\n" +
                    "\t\tpaymonthly_config p\n" +
                    "\t\tLEFT JOIN paymonthly_parkingplace pa ON p.id = pa.paymonthly_config_id \n" +
                    "\tWHERE\n" +
                    "\t\tp.is_del = 0 \n" +
                    "\t\tAND pa.is_del = 0 ");
            if (notEmpty(parkinType)) {
                sqlbd.append("\t\tAND p.parking_type = \n").append(parkinType);
            }
            sqlbd.append("\tAND pa.parkingplace_id IN ( ").append(park_id);
            sqlbd.append("  ) ");
            sqlbd.append(" ) ");
        }

        return sqlbd.toString();

    }

    /**
     * 停车场
     * 包月收入
     * pc端包月金额统计
     * 微信小程序包月金额统计
     */
    public String getMonthlyPaymentSum(CommonVo vo) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\tSUM (\n" +
                "\t\tCAST (\n" +
                "\t\tt.amount AS DECIMAL ( 10, 2 ))) amount \n" +
                "FROM\n" +
                "\t( ");

        sqlbd.append(" SELECT SUM\n" +
                "\t(\n" +
                "\t\tCAST (\n" +
                "\t\tp.amount AS DECIMAL ( 10, 2 ))) amount \n" +
                "FROM\n" +
                "\tpayment_order p\n" +
                "\tINNER JOIN monthly_management m ON m.payment_id = p.id\n" +
                "\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id \n" +
                "WHERE\n" +
                "\tp.status = 2 \n" +
                "\tAND p.is_del = 0 \n" +
                "\tAND m.is_del = 0 \n" +
                "\tAND t2.is_del = 0 \n" +
                "\tAND pa.is_del = 0 ");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >=  '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <=  '").append(vo.getEndTime()).append("' ");
        }
        if (notEmpty(vo.getParkId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getParkId()).append(") ");
        }
        if (notEmpty(vo.getRoadId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getRoadId()).append(") ");
        }

        sqlbd.append(" UNION ALL\n");

        sqlbd.append(" SELECT SUM\n" +
                "\t(\n" +
                "\t\tCAST (\n" +
                "\t\tp.amount AS DECIMAL ( 10, 2 ))) amount \n" +
                "FROM\n" +
                "\tpayment_order p\n" +
                "\tINNER JOIN monthly_payment_record r ON r.payment_id = p.id\n" +
                "\tINNER JOIN monthly_management m ON m.id = r.monthly_management_id\n" +
                "\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id \n" +
                "WHERE\n" +
                "\tp.status = 2 \n" +
                "\tAND p.is_del = 0 \n" +
                "\tAND m.is_del = 0 \n" +
                "\tAND t2.is_del = 0 \n" +
                "\tAND pa.is_del = 0 ");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >=  '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <=  '").append(vo.getEndTime()).append("' ");
        }
        if (notEmpty(vo.getParkId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getParkId()).append(") ");
        }
        if (notEmpty(vo.getRoadId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getRoadId()).append(") ");
        }
        sqlbd.append(" ) t ");
        return sqlbd.toString();
    }

    /**
     * 今日停车订单数排名
     * 包月收入
     * 微信小程序包月金额统计
     */
    public String getMonthlyAmountGroup(CommonVo vo) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\tt.months_unit_price,\n" +
                "\tSUM ( t.months ) months,\n" +
                "\tSUM (\n" +
                "\t\tCAST (\n" +
                "\t\tt.amount AS DECIMAL ( 10, 2 ))) amount \n" +
                "FROM\n" +
                "\t( ");

        sqlbd.append(" SELECT\n" +
                "\t\tDISTINCT p.id,\n" +
                "\t\tp.months_unit_price,\n" +
                "\t\tp.months,\n" +
                "\t\tp.amount,\n" +
                "\t\tp.create_time,\n" +
                "\t\tc.car_no carNo,\n" +
                "\t\tt2.name configName \n" +
                "\tFROM\n" +
                "\t\tpayment_order p\n" +
                "\t\tINNER JOIN monthly_payment_record r ON r.payment_id = p.id\n" +
                "\t\tINNER JOIN monthly_management m ON m.id = r.monthly_management_id\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id\n" +
                "\t\tLEFT JOIN operate_carno c ON c.id = m.carno_id \n" +
                "\tWHERE\n" +
                "\t\tp.status = 2 \n" +
                "\t\tAND p.is_del = 0 \n" +
                "\t\tAND m.is_del = 0 \n" +
                "\t\tAND t2.is_del = 0 \n" +
                "\t\tAND pa.is_del = 0 \n");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >=  '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <=  '").append(vo.getEndTime()).append("' ");
        }
        if (notEmpty(vo.getParkId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getParkId()).append(") ");
        }
        if (notEmpty(vo.getRoadId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getRoadId()).append(") ");
        }
//        sqlbd.append(" GROUP BY p.months_unit_price ");

        sqlbd.append(" UNION ALL\n");

        sqlbd.append(" SELECT\n" +
                "\t\tDISTINCT p.id,\n" +
                "\t\tp.months_unit_price,\n" +
                "\t\tp.months,\n" +
                "\t\tp.amount,\n" +
                "\t\tp.create_time,\n" +
                "\t\tc.car_no carNo,\n" +
                "\t\tt2.name configName \n" +
                "\tFROM\n" +
                "\t\tpayment_order p\n" +
                "\t\tINNER JOIN monthly_management m ON m.payment_id = p.id\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id\n" +
                "\t\tLEFT JOIN operate_carno c ON c.id = m.carno_id \n" +
                "\tWHERE\n" +
                "\t\tp.status = 2 \n" +
                "\t\tAND p.is_del = 0 \n" +
                "\t\tAND m.is_del = 0 \n" +
                "\t\tAND t2.is_del = 0 \n" +
                "\t\tAND pa.is_del = 0 \n");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >=  '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <=  '").append(vo.getEndTime()).append("' ");
        }
        if (notEmpty(vo.getParkId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getParkId()).append(") ");
        }
        if (notEmpty(vo.getRoadId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getRoadId()).append(") ");
        }
//        sqlbd.append(" GROUP BY p.months_unit_price ");

        sqlbd.append(" ) t GROUP BY\n" +
                "\tt.months_unit_price");
        return sqlbd.toString();
    }

    /**
     * 停车场
     * 包月收入明细
     */
    public String getMonthlyPaymentData(CommonVo vo) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\t* \n" +
                "FROM\n" +
                "\t(\n" +

                "\tSELECT\n" +
                "\t\tDISTINCT p.id,\n" +
                "\t\tp.months_unit_price,\n" +
                "\t\tp.months,\n" +
                "\t\tp.amount,\n" +
                "\t\tp.create_time,\n" +
                "\t\tc.car_no carNo,\n" +
                "\t\tt2.name configName \n" +
                "\tFROM\n" +
                "\t\tpayment_order p\n" +
                "\t\tINNER JOIN monthly_payment_record r ON r.payment_id = p.id\n" +
                "\t\tINNER JOIN monthly_management m ON m.id = r.monthly_management_id\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id\n" +
                "\t\tLEFT JOIN operate_carno c ON c.id = m.carno_id \n" +
                "\tWHERE\n" +
                "\t\tp.status = 2 \n" +
                "\t\tAND p.is_del = 0 \n" +
                "\t\tAND m.is_del = 0 \n" +
                "\t\tAND t2.is_del = 0 \n" +
                "\t\tAND pa.is_del = 0 \n");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >=  '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <=  '").append(vo.getEndTime()).append("' ");
        }
        if (notEmpty(vo.getParkId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getParkId()).append(") ");
        }
        if (notEmpty(vo.getRoadId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getRoadId()).append(") ");
        }

        sqlbd.append(" UNION ALL\n" +

                "\tSELECT\n" +
                "\t\tDISTINCT p.id,\n" +
                "\t\tp.months_unit_price,\n" +
                "\t\tp.months,\n" +
                "\t\tp.amount,\n" +
                "\t\tp.create_time,\n" +
                "\t\tc.car_no carNo,\n" +
                "\t\tt2.name configName \n" +
                "\tFROM\n" +
                "\t\tpayment_order p\n" +
                "\t\tINNER JOIN monthly_management m ON m.payment_id = p.id\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id\n" +
                "\t\tLEFT JOIN operate_carno c ON c.id = m.carno_id \n" +
                "\tWHERE\n" +
                "\t\tp.status = 2 \n" +
                "\t\tAND p.is_del = 0 \n" +
                "\t\tAND m.is_del = 0 \n" +
                "\t\tAND t2.is_del = 0 \n" +
                "\t\tAND pa.is_del = 0 \n");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >=  '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <=  '").append(vo.getEndTime()).append("' ");
        }
        if (notEmpty(vo.getParkId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getParkId()).append(") ");
        }
        if (notEmpty(vo.getRoadId())) {
            sqlbd.append(" AND pa.parkingplace_id IN ( ").append(vo.getRoadId()).append(") ");
        }

        sqlbd.append(" ) t ");
        sqlbd.append(" ORDER BY\n" +
                "\tt.create_time DESC");
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 包月统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getMonthlyRentCount(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select t3.parkingplace_id parkId, ");
        sqlbd.append(" count(distinct t1.carno_id ) yzCarNum, ");
        sqlbd.append(" count(1) yzCounts, ");
        sqlbd.append(" Sum(convert(decimal(15,5), t1.total_cost )) yzTotalCost ");
        sqlbd.append(" from monthly_management t1 ");
        sqlbd.append(" left join paymonthly_config t2 on t1.paymonthly_config_id = t2.id ");
        sqlbd.append(" left join paymonthly_parkingplace t3 on t2.id = t3.paymonthly_config_id ");
        sqlbd.append(" where t1.is_del = 0 ");
        sqlbd.append(" and t2.is_del = 0 ");
        sqlbd.append(" and t3.is_del = 0 ");
        if (notEmpty(data.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(data.getParking_type());
        }
        if (StringUtils.isNotBlank(data.getStartTime())) {
            sqlbd.append(" and convert(varchar , t1.create_time , 20)>= '").append(data.getStartTime()).append(" 00:00:00").append("' ");
        }
        if (StringUtils.isNotBlank(data.getEndTime())) {
            sqlbd.append(" and convert(varchar , t1.create_time , 20)<= '").append(data.getEndTime()).append(" 23:59:59").append("' ");
        }
        if (StringUtils.isNotBlank(data.getStr())) {
            sqlbd.append(" and convert(varchar , t1.create_time , 20)<= '").append(data.getStr()).append(" 23:59:59").append("' ");
        }
        if (data.getVariance() != null) {
            sqlbd.append(" and t3.parkingplace_id = ").append(data.getVariance());
        }

        sqlbd.append(" group by t3.parkingplace_id ");

        return sqlbd.toString();
    }

    public String getMonthlyRentCountParkAndRoad(CommonVo vo) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\tt.parkId,\n" +
                "\tSUM ( t.yzCarNum ) yzCarNum,\n" +
                "\tSUM ( t.yzCounts ) yzCounts,\n" +
                "\tSUM (\n" +
                "\t\tCAST (\n" +
                "\t\tt.yzTotalCost AS DECIMAL ( 10, 2 ))) yzTotalCost \n" +
                "FROM\n" +
                "\t( ");

        sqlbd.append(" SELECT\n" +
                "\t\tpa.parkingplace_id parkId,\n" +
                "\t\tCOUNT ( DISTINCT m.carno_id ) yzCarNum,\n" +
                "\t\tCOUNT ( 1 ) yzCounts,\n" +
                "\t\tSUM (\n" +
                "\t\t\tCAST (\n" +
                "\t\t\tp.amount AS DECIMAL ( 10, 2 ))) yzTotalCost \n" +
                "\tFROM\n" +
                "\t\tpayment_order p\n" +
                "\t\tINNER JOIN monthly_management m ON m.payment_id = p.id\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id \n" +
                "\tWHERE\n" +
                "\t\tp.status = 2 \n" +
                "\t\tAND p.is_del = 0 \n" +
                "\t\tAND m.is_del = 0 \n" +
                "\t\tAND t2.is_del = 0 \n" +
                "\t\tAND pa.is_del = 0 \n");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >= '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <= '").append(vo.getEndTime()).append("' ");
        }
//        if (StringUtils.isNotBlank(vo.getStr())) {
//            sqlbd.append(" and CONVERT(varchar , p.create_time, 23) <= '").append(vo.getStr()).append("' ");
//        }
        if (vo.getVariance() != null) {
            sqlbd.append(" and pa.parkingplace_id = ").append(vo.getVariance());
        }
        sqlbd.append(" GROUP BY\n" +
                "\t\tpa.parkingplace_id");

        sqlbd.append(" UNION ALL\n");

        sqlbd.append(" SELECT\n" +
                "\t\tpa.parkingplace_id parkId,\n" +
                "\t\tCOUNT ( DISTINCT m.carno_id ) yzCarNum,\n" +
                "\t\tCOUNT ( 1 ) yzCounts,\n" +
                "\t\tSUM (\n" +
                "\t\t\tCAST (\n" +
                "\t\t\tp.amount AS DECIMAL ( 10, 2 ))) yzTotalCost \n" +
                "\tFROM\n" +
                "\t\tpayment_order p\n" +
                "\t\tINNER JOIN monthly_payment_record r ON r.payment_id = p.id\n" +
                "\t\tINNER JOIN monthly_management m ON m.id = r.monthly_management_id\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN paymonthly_parkingplace pa ON t2.id = pa.paymonthly_config_id \n" +
                "\tWHERE\n" +
                "\t\tp.status = 2 \n" +
                "\t\tAND p.is_del = 0 \n" +
                "\t\tAND m.is_del = 0 \n" +
                "\t\tAND t2.is_del = 0 \n" +
                "\t\tAND pa.is_del = 0 \n");
        if (notEmpty(vo.getParking_type())) {
            sqlbd.append(" and t2.parking_type = ").append(vo.getParking_type());
        }
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) >= '").append(vo.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.create_time, 23) <= '").append(vo.getEndTime()).append("' ");
        }
//        if (StringUtils.isNotBlank(vo.getStr())) {
//            sqlbd.append(" and CONVERT(varchar , p.create_time, 23) <= '").append(vo.getStr()).append("' ");
//        }
        if (vo.getVariance() != null) {
            sqlbd.append(" and pa.parkingplace_id = ").append(vo.getVariance());
        }
        sqlbd.append(" GROUP BY\n" +
                "\t\tpa.parkingplace_id");
        sqlbd.append(" ) t ");
        sqlbd.append(" GROUP BY\n" +
                "\t\tt.parkId");
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(月租车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getMonthCarRentalDetail(CommonVo data) {
        String road_or_park = "park";
        String parkNames = "park_name";

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select t1.name , t1.phone , t1.start_time  startTime, t1.end_time endTime,t3.car_no carNo, t1.months ,t2.price ,  ");
        sqlbd.append(" t1.total_cost totalCost, t1.create_time createTime, t3.companyName,  ");

        sqlbd.append(" (\n" +
                "\tSELECT\n" +
                "\t\tSTUFF((\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\t',' + p." + parkNames + " \n" +
                "\t\t\tFROM\n" +
                "\t\t\t\t" + road_or_park + " p\n" +
                "\t\t\t\tLEFT JOIN paymonthly_parkingplace pa ON p.id= pa.parkingplace_id and pa.is_del='0'\n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\tt2.id = pa.paymonthly_config_id  and p.is_del='0' FOR XML PATH ( '' )),\n" +
                "\t\t\t1,\n" +
                "\t\t\t1,\n" +
                "\t\t\t'' \n" +
                "\t\t)) parkNames ");

        sqlbd.append(" from monthly_management t1 ");
        sqlbd.append(" left join paymonthly_config t2 on t1.paymonthly_config_id = t2.id ");
        sqlbd.append(" left join ( select id ,car_no , stuff( ");
        sqlbd.append("   (select ','+t2.companyName from ( select t1.carno_id  carnoId, t2.company_name companyName from carno_company t1 ");
        sqlbd.append("   left join operate_company t2 on t1.company_id =t2.id ) t2 WHERE t2.carnoId = t1.id for xml path('') ),1,1,'') companyName ");
        sqlbd.append(" from operate_carno t1 ");
        sqlbd.append(" where t1.is_del = 0 ");
        sqlbd.append(" ) t3 on t1.carno_id =t3.id ");

        sqlbd.append(" where t2.parking_type = ").append(data.getVariance2());

        sqlbd.append(" and t1.is_del = 0 ");

        if (StringUtils.isNotBlank(data.getStartTime())) {
            sqlbd.append("and convert(varchar , t1.create_time , 23)>= '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotBlank(data.getEndTime())) {
            sqlbd.append("and convert(varchar , t1.create_time , 23)<= '").append(data.getEndTime()).append("' ");
        }
        if (StringUtils.isNotBlank(data.getStr())) {
            sqlbd.append("and convert(varchar , t1.create_time , 23)<= '").append(data.getStr()).append("' ");
        }
//        if (data.getVariance() != null) {
//            sqlbd.append(" and t2.id in ( select paymonthly_config_id from paymonthly_parkingplace where parkingplace_id = ").append(data.getVariance()).append(" )  ");
//        }
        if (StringUtils.isNotNull(data.getVariance()) || StringUtils.isNotNull(data.getArea_id()) || StringUtils.isNotNull(data.getStreet_id())) {
            sqlbd.append(" and t2.id in ( ");
            sqlbd.append("  SELECT\n" +
                    "\t\tpa.paymonthly_config_id \n" +
                    "\tFROM\n" +
                    "\t\tpaymonthly_config p2\n" +
                    "\t\tLEFT JOIN paymonthly_parkingplace pa ON p2.id = pa.paymonthly_config_id \n" +
                    "\tWHERE\n" +
                    "\t\tp2.is_del = 0 \n" +
                    "\t\tAND pa.is_del = 0 ");
            sqlbd.append("\t\tAND p2.parking_type = ").append(data.getVariance2());
            sqlbd.append("\tAND pa.parkingplace_id IN ( ");
            sqlbd.append("   select r.id from ").append(road_or_park).append(" r ");
            sqlbd.append("   where r.is_del = '0' and status = '0' ");
            if (StringUtils.isNotNull(data.getVariance())) {
                sqlbd.append("   and r.id = ").append(data.getVariance());
            }
            if (StringUtils.isNotNull(data.getArea_id())) {
                sqlbd.append("   and r.area_id = ").append(data.getArea_id());
            }
            if (StringUtils.isNotNull(data.getStreet_id())) {
                sqlbd.append("   and r.street_id = ").append(data.getStreet_id());
            }
            sqlbd.append("  ) ");
            sqlbd.append(" ) ");
        }
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(月租车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getMonthCarRentalStatistics(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select count( distinct carno_id) carCount , sum( CONVERT( decimal(15,5) , t1.total_cost ) ) sumAmount from monthly_management t1 ");
        sqlbd.append(" left join paymonthly_config t2 on t1.paymonthly_config_id = t2.id ");

        sqlbd.append(" where t2.parking_type = ").append(data.getVariance2());
        String road_or_park = "park";

        sqlbd.append(" and t1.is_del = 0 ");
        if (StringUtils.isNotBlank(data.getStartTime())) {
            sqlbd.append("and convert(varchar , t1.create_time , 23)>= '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotBlank(data.getEndTime())) {
            sqlbd.append("and convert(varchar , t1.create_time , 23)<= '").append(data.getEndTime()).append("' ");
        }
        if (StringUtils.isNotBlank(data.getStr())) {
            sqlbd.append("and convert(varchar , t1.create_time , 23)<= '").append(data.getStr()).append("' ");
        }
//        if (data.getVariance() != null) {
//            sqlbd.append(" and t2.id in ( select paymonthly_config_id from paymonthly_parkingplace where parkingplace_id = ").append(data.getVariance()).append(" )  ");
//        }
        if (StringUtils.isNotNull(data.getVariance()) || StringUtils.isNotNull(data.getArea_id()) || StringUtils.isNotNull(data.getStreet_id())) {
            sqlbd.append(" and t2.id in ( ");
            sqlbd.append("  SELECT\n" +
                    "\t\tpa.paymonthly_config_id \n" +
                    "\tFROM\n" +
                    "\t\tpaymonthly_config p2\n" +
                    "\t\tLEFT JOIN paymonthly_parkingplace pa ON p2.id = pa.paymonthly_config_id \n" +
                    "\tWHERE\n" +
                    "\t\tp2.is_del = 0 \n" +
                    "\t\tAND pa.is_del = 0 ");
            sqlbd.append("\t\tAND p2.parking_type = ").append(data.getVariance2());
            sqlbd.append("\tAND pa.parkingplace_id IN ( ");
            sqlbd.append("   select r.id from ").append(road_or_park).append(" r ");
            sqlbd.append("   where r.is_del = '0' and status = '0' ");
            if (StringUtils.isNotNull(data.getVariance())) {
                sqlbd.append("   and r.id = ").append(data.getVariance());
            }
            if (StringUtils.isNotNull(data.getArea_id())) {
                sqlbd.append("   and r.area_id = ").append(data.getArea_id());
            }
            if (StringUtils.isNotNull(data.getStreet_id())) {
                sqlbd.append("   and r.street_id = ").append(data.getStreet_id());
            }
            sqlbd.append("  ) ");
            sqlbd.append(" ) ");
        }

        return sqlbd.toString();
    }

    public String byExport(String isTheCompany, String parkingType) {
        String sql = "SELECT DISTINCT\n";
        if ("1".equals(isTheCompany)) {
            sql += " t2.car_no carNo,t2.car_type carType" +
                    ", t1.name,t1.phone,";
        }
        sql += "\t\t\tt4.name configName,\n" +
                "\t\t\tCONVERT ( VARCHAR ( 100 ), t1.start_time, 20 ) startTime,\n" +
                "\t\t\tCONVERT ( VARCHAR ( 100 ), t1.end_time, 20 ) endTime,\n" +
                "\t\t\tCONVERT ( VARCHAR ( 100 ), t1.create_time, 20 ) createTime,\n" +
                "\t\t\tt1.months,\n" +
                "\t\t\tt6.payment_type paymentType,\n" +
                "\t\t\tt6.amount,\n" +
                "\t\t\tt6.transferSerialNumber,\n" +
                "\t\t\tt3.login_name userName,\n" +
                "\t\t\tt7.company_name company_name\n" +
                "\t\tFROM\n" +
                "\t\t\tmonthly_management t1\n" +
                "\t\tLEFT JOIN operate_carno t2 ON t1.carno_id = t2.id\n" +
                "\t\tLEFT JOIN sys_user t3 ON t1.create_user = t3.id\n" +
                "\t\tLEFT JOIN paymonthly_config t4 ON t1.paymonthly_config_id = t4.id\n" +
                "\t\tLEFT JOIN paymonthly_parkingplace t5 ON t4.id = t5.paymonthly_config_id\n" +
                "\t\tLEFT JOIN payment_order t6 ON t1.payment_id = t6.id and t6.is_del='0' and t6.status='2'\n" +
                "\t\tLEFT JOIN operate_company t7 ON t1.companyId = t7.id\n" +
                "\t\tWHERE\n" +
                "\t\t\t1 = 1\n" +
                "\t\tAND t1.isTheCompany = \n" + isTheCompany +
                "\t\tAND t1.is_del = 0\n" +
                "\t\tAND t4.parking_type = '" + parkingType + "'" +
                "\t\torder by createTime desc";
        return sql;
    }

    /**
     * 支付流水明细关联包月订单
     */
    public String getMonthlyDataByPaymentId(Integer paymentId) {
        String sql = "SELECT\n" +
                "\t* \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tm.*,\n" +
                "\t\tc.car_no carNo,\n" +
                "\t\tt2.price price, \n" +
                "\t\tt2.name configName \n" +
                "\tFROM\n" +
                "\t\tmonthly_management m\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN operate_carno c ON m.carno_id = c.id \n" +
                "\tWHERE\n" +
                "\t\tm.is_del = 0 \n" +
                "\t\tAND t2.is_del = 0 ";
        sql += " AND m.payment_id = " + paymentId;
        sql += " UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tm.*,\n" +
                "\t\tc.car_no carNo,\n" +
                "\t\tt2.price price, \n" +
                "\t\tt2.name configName \n" +
                "\tFROM\n" +
                "\t\tmonthly_payment_record r\n" +
                "\t\tINNER JOIN monthly_management m ON m.id = r.monthly_management_id\n" +
                "\t\tLEFT JOIN paymonthly_config t2 ON m.paymonthly_config_id = t2.id\n" +
                "\t\tLEFT JOIN operate_carno c ON m.carno_id = c.id \n" +
                "\tWHERE\n" +
                "\t\tm.is_del = 0 \n" +
                "\tAND t2.is_del = 0 \n";
        sql += " AND r.payment_id = " + paymentId;
        sql += ") t";
        return sql;
    }
}
