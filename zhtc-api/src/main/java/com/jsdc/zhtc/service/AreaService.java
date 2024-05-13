package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.AreaDao;
import com.jsdc.zhtc.mapper.AreaMapper;
import com.jsdc.zhtc.model.Area;
import com.jsdc.zhtc.vo.AreaVo;
import com.jsdc.zhtc.vo.TreeStructureVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: AreaService <br/>
 * Description: <br/>
 * date: 2021/12/30 10:47<br/>
 *
 * @author bn<br       />
 */
@Service
@Transactional
public class AreaService extends BaseService<AreaDao, Area> {

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private StreetService streetService;


    /**
     * 区域全数据
     *
     * @param area
     * @return
     */
    public List<Area> toList(Area area) {
        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(area.getArea_name())) {
            queryWrapper.like("area_name", area.getArea_name());
        }

        queryWrapper.eq("status", 0);
        queryWrapper.eq("is_del", 0);
        return selectList(queryWrapper);
    }


    /**
     * 区域列表展示数据
     *
     * @param pageIndex
     * @param pageSize
     * @param area
     * @return
     */
    public PageInfo<AreaVo> toList(Integer pageIndex, Integer pageSize, Area area) {

        PageHelper.startPage(pageIndex, pageSize);

        List<AreaVo> areaList = areaMapper.toList(area);

        PageInfo<AreaVo> page = new PageInfo<>(areaList);


        return page;
    }

    /**
     * 描 述： TODO(获取区 街道 路段树形结构)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link List< TreeStructureVo>}
     */
    public List<TreeStructureVo> getParkingTreeStructure() {
        ArrayList<TreeStructureVo> areaTSV = new ArrayList<>();

        TreeStructureVo areaMRBean = new TreeStructureVo();
        areaMRBean.setLabel("全部");
        areaMRBean.setId(0);
        areaMRBean.setGrade(0);
        areaTSV.add(areaMRBean);

        List<AreaVo> areaList = areaMapper.getAll(null);
        if (areaList != null && areaList.size() > 0) {
            for (AreaVo item : areaList) {
                //获取街道信息
                ArrayList<TreeStructureVo> streetList = streetService.getParkingTreeStructure(item.getId());

                TreeStructureVo areaBean = new TreeStructureVo();
                areaBean.setLabel(item.getArea_name());
                areaBean.setId(item.getId());
                areaBean.setGrade(1);
                areaBean.setChildren(streetList);

                areaTSV.add(areaBean);
            }
        }
        return areaTSV;
    }
}
