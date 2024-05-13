package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.StreetDao;
import com.jsdc.zhtc.mapper.StreetMapper;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.Street;
import com.jsdc.zhtc.vo.StreetVo;
import com.jsdc.zhtc.vo.TreeStructureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: StreetService <br/>
 * Description: <br/>
 * date: 2021/12/30 10:53<br/>
 *
 * @author bn<br       />
 */
@Service
@Transactional
public class StreetService extends BaseService<StreetDao, Street> {

    @Autowired
    private StreetMapper streetMapper;


    @Autowired
    private ParkService parkService;

    /**
     * 街道全数据
     *
     * @param street
     * @return
     */
    public List<Street> toList(Street street) {
        QueryWrapper<Street> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(street.getStreet_name())) {
            queryWrapper.eq("street_name", street.getStreet_name());
        }
        if (null != street.getArea_id()) {
            queryWrapper.eq("area_id", street.getArea_id());
        }

        queryWrapper.eq("status", 0);
        queryWrapper.eq("is_del", 0);
        return selectList(queryWrapper);
    }

    /**
     * 街道列表展示数据
     *
     * @param pageIndex
     * @param pageSize
     * @param street
     * @return
     */
    public PageInfo<StreetVo> toList(Integer pageIndex, Integer pageSize, Street street) {
        PageHelper.startPage(pageIndex, pageSize);

        List<StreetVo> streetVos = streetMapper.toList(street);

        PageInfo<StreetVo> page = new PageInfo<>(streetVos);

        return page;
    }


    /**
     * 描 述： TODO(获取区 街道 路段树形结构)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link List<  TreeStructureVo >}
     */
    public ArrayList<TreeStructureVo> getParkingTreeStructure(Integer id) {
        ArrayList<TreeStructureVo> streetTSV = new ArrayList<>();

        List<StreetVo> streetList = streetMapper.getAll(id);
        if (streetList != null && streetList.size() > 0) {
            for (StreetVo item : streetList) {

                //获取路段信息
                LambdaQueryWrapper<Park> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Park::getStreet_id, item.getId());
                wrapper.eq(Park::getIs_del, GlobalData.ISDEL_NO);
                wrapper.eq(Park::getStatus, 0);
                List<Park> parks = parkService.selectList(wrapper);

                ArrayList<TreeStructureVo> roadTSV = new ArrayList<>();
                if (parks != null && parks.size() > 0) {
                    for (Park park : parks) {
                        TreeStructureVo roadBean = new TreeStructureVo();
                        roadBean.setLabel(park.getPark_name());
                        roadBean.setId(park.getId());
                        roadBean.setGrade(3);
                        roadBean.setAreaId(id);
                        roadBean.setStreetId(item.getId());
                        roadTSV.add(roadBean);
                    }
                }

                //设置街道名称与id
                TreeStructureVo streetBean = new TreeStructureVo();
                streetBean.setLabel(item.getStreet_name());
                streetBean.setId(item.getId());
                streetBean.setGrade(2);
                streetBean.setAreaId(id);
                //设置路段信息
                streetBean.setChildren(roadTSV);
                streetTSV.add(streetBean);
            }

        }
        return streetTSV;
    }
}
