package com.jsdc.zhtc.service;

import com.jsdc.zhtc.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

/**
 * ClassName: OperateAnalysisService
 * Description:
 * date: 2022/1/4 10:30
 *
 * @author wp
 */
@Service
@Transactional
public class OperateAnalysisService {

    /**
     * create by wp at 2022/1/4 17:03
     * description: 获取两个日期间的时间差（分钟数）
     *
     * @param oldTime
     * @param newTime
     * @return long
     */
    public static long getTime(String oldTime, String newTime) {
        try {
            if (StringUtils.equals(oldTime, newTime)) {
                oldTime += " 00:00:00";
                newTime += " 23:59:59";
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long NTime = df.parse(newTime).getTime();
                //从对象中拿到时间
                long OTime = df.parse(oldTime).getTime();
                long diff = (NTime - OTime) / 1000 / 60;
                return diff;
            } else {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                long NTime = df.parse(newTime).getTime();
                //从对象中拿到时间
                long OTime = df.parse(oldTime).getTime();
                long diff = (NTime - OTime) / 1000 / 60;
                return diff;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return 0;
    }

}
