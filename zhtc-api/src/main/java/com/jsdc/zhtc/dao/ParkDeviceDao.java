package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.ParkDevice;
import com.jsdc.zhtc.vo.ParkDeviceVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: ParkDeviceDao <br/>
 * Description: <br/>
 * date: 2022/1/13 11:16<br/>
 *
 * @author bn<br       />
 */
@Repository
public class ParkDeviceDao extends BaseDao<ParkDevice> {


    public String toList(ParkDeviceVo deviceVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT device.id,device.device_code,device.berth_code,device.park_id" +
                ",device.device_type,device.passageway,device.channel_id,channel_name," +
                "device.is_double_way,device.upload_mode,device.longitude" +
                ",device.latitude,device.soft_version," +
                "device.hard_version,device.recent_upgrade" +
                ",device.sn_no,device.mac,device.agrver,device.net_status," +
                "device.last_heartbeat_time,device.status" +
                ",device.is_use,device.create_time,device.create_user" +
                ",device.update_time,device.update_user,device.is_del,device.serialNo" +
                ",street.area_id,park.street_id,area.area_name,street.street_name,park.park_name,park.address " +
                "from park_device device ");


        sql.append("LEFT JOIN park ON device.park_id=park.id ");
        sql.append("LEFT JOIN street ON park.street_id=street.id ");
        sql.append("LEFT JOIN area on street.area_id=area.id ");


        sql.append("where 1=1 ");
        if (StringUtils.isNotEmpty(deviceVo.getDevice_code())) {
            sql.append(" AND device.device_code LIKE '%" + deviceVo.getDevice_code() + "%' ");
        }
        if (StringUtils.isNotEmpty(deviceVo.getBerth_code())) {
            sql.append(" AND device.berth_code LIKE '%" + deviceVo.getBerth_code() + "%' ");
        }
        if (deviceVo.getPark_id() != null) {
            sql.append(" AND device.park_id=" + deviceVo.getPark_id());
        }
        if (deviceVo.getStreet_id() != null) {
            sql.append(" AND device.street_id=" + deviceVo.getStreet_id());
        }
        if (deviceVo.getArea_id() != null) {
            sql.append(" AND device.area_id=" + deviceVo.getArea_id());
        }
        if (StringUtils.isNotEmpty(deviceVo.getDevice_type())) {
            sql.append(" AND device.device_type=" + deviceVo.getDevice_type());
        }


        if (deviceVo.getIsBind() != null) {
            if (deviceVo.getIsBind() == 0) {
                sql.append(" AND device.park_id is NULL ");
            }
            if (deviceVo.getIsBind() == 1) {
                sql.append(" AND device.park_id is NOT NULL ");
            }
        }

        if (notEmpty(deviceVo.getStatus())) {
            sql.append(" AND device.status=" + deviceVo.getStatus());
        }

        sql.append(" AND device.is_del=0 ");
        sql.append(" AND park.is_del=0 ");
        return sql.toString();
    }


    public String getParkDevice(ParkDeviceVo parkDeviceVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT device.id,device.device_code,device.berth_code,device.park_id\" +\n" +
                "  \",device.device_type,device.passageway,\" +\n" +
                "  \"device.is_double_way,device.upload_mode,device.longitude\" +\n" +
                "  \",device.latitude,device.soft_version,\" +\n" +
                "  \"device.hard_version,device.recent_upgrade\" +\n" +
                "  \",device.sn_no,device.mac,device.agrver,device.net_status,\" +\n" +
                "  \"device.last_heartbeat_time,device.status\" +\n" +
                "  \",device.is_use,device.create_time,device.create_user\" +\n" +
                "  \",device.update_time,device.update_user,device.is_del\" +\n" +
                "  \",street.area_id,park.street_id,area.area_name,street.street_name,park.address," +
                " park.park_name,device.serialNo from park_device device ");
        sql.append("LEFT JOIN park ON device.park_id=park.id AND park.is_del=0");
        sql.append("LEFT JOIN street ON park.street_id=street.id ");
        sql.append("LEFT JOIN area on street.area_id=area.id ");
        sql.append("where 1=1 ");
        if (parkDeviceVo.getId() != null) {
            sql.append(" AND device.id=" + parkDeviceVo.getId());
        }

        if (notEmpty(parkDeviceVo.getStatus())) {
            sql.append(" AND device.status=" + parkDeviceVo.getStatus());
        }


        sql.append(" AND device.is_del=0 ");
        return sql.toString();
    }


}
