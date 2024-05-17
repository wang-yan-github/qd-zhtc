package com.jsdc.zhtc.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.IdUtils;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.CarOwnerDao;
import com.jsdc.zhtc.model.CarOwner;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@Transactional
public class CarOwnerService extends BaseService<CarOwnerDao, CarOwner> {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDictService sysDictService;
    @Value("${excel-export-path}")
    private String localPath;

    /**
     * 查询
     */
    public PageInfo<CarOwner> selectAll(PageVo<CarOwner> data) {
        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());

        }
        LambdaQueryWrapper<CarOwner> wrapper = new LambdaQueryWrapper<>();
        CarOwner bean = data.getBean();
        if (null != bean) {
            wrapper.eq(StringUtils.isNotEmpty(bean.getType()), CarOwner::getType, bean.getType());
            wrapper.like(StringUtils.isNotEmpty(bean.getName()), CarOwner::getName, bean.getName());
            wrapper.like(StringUtils.isNotEmpty(bean.getPhone()), CarOwner::getPhone, bean.getPhone());
        }

        wrapper.eq(CarOwner::getIs_del, GlobalData.ISDEL_NO);
        wrapper.orderByDesc(CarOwner::getCreate_time);

        List<CarOwner> lists = selectList(wrapper);

        Map<String, String> sysDictMap = sysDictService.getLabel(GlobalData.CAR_OWNER_TYPE);
        for (CarOwner carOwner : lists) {
            carOwner.setType_name(sysDictMap.get(carOwner.getType()));
        }

        PageInfo<CarOwner> listPage = new PageInfo<>(lists);
        return listPage;
    }

    /**
     * 根据id查询
     */
    public ResultInfo selectById(CarOwner bean) {
        CarOwner data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error(null, "未查询到数据");
        }
    }

    /**
     * 新增数据
     */
    public ResultInfo saveData(CarOwner bean) {
        SysUser sysUser = sysUserService.getUser();
        bean.setId(IdUtil.simpleUUID());
        bean.setIs_del(GlobalData.ISDEL_NO);
        bean.setCreate_time(new Date());
        bean.setUpdate_user(sysUser.getId());
        bean.setCreate_user_name(sysUser.getUser_name());

        if (this.insert(bean) > 0) {
            return ResultInfo.success(null, "数据添加成功");
        } else {
            return ResultInfo.success("message", "数据添加失败");
        }
    }

    /**
     * 根据id更新
     */
    public ResultInfo updateInfo(CarOwner bean) {
        SysUser sysUser = sysUserService.getUser();
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(sysUser.getId());
        bean.setUpdate_user_name(sysUser.getUser_name());

        if (this.updateById(bean) > 0) {
            return ResultInfo.success(null, "操作成功");
        } else {
            return ResultInfo.error(null, "操作成功");
        }

    }

    /**
     * 根据id删除
     */
    public ResultInfo deleById(CarOwner bean) {
        bean.setIs_del(GlobalData.ISDEL_YES);
        if (this.updateById(bean) > 0) {
            return ResultInfo.success(null, "删除成功");
        } else {
            return ResultInfo.error(null, "操作失败");
        }
    }


    public ResultInfo templateImport(MultipartFile file) {
        //fileName 文件名
        String fileName = file.getOriginalFilename();
        boolean xlsx = false;
        if (fileName != null) {
            xlsx = fileName.endsWith(".xlsx");
        }
        if (!xlsx) {
            return ResultInfo.error("请上传以.xlsx结尾的文件");
        }
        //得到文件流
        InputStream inputStream = null;
        try {
            inputStream = file.getResource().getInputStream();
        } catch (IOException e) {
            return ResultInfo.error("请检查文件后，重新导入！");
        }
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Map<String, Object>> readAll = reader.read(2, 2, Integer.MAX_VALUE);
        if (readAll.isEmpty()) {
            return ResultInfo.error("空白模板，请填写内容！");
        }

        SysUser sysUser = sysUserService.getUser();
        Map<String, String> sysDictMap = sysDictService.getValue(GlobalData.CAR_OWNER_TYPE);
        for (int i = 0; i < readAll.size(); i++) {
            Map<String, Object> quMap = readAll.get(i);
            //获取表格中的数据
            String type = String.valueOf(quMap.get("车主属性"));
            if (StringUtils.isNotEmpty(type)) {
                if (StringUtils.isEmpty(sysDictMap.get(type))) {
                    return ResultInfo.error("请选择正确选择车主属性,第" + (i + 1) + "行");
                }
            } else {
                return ResultInfo.error("请选择车主属性,第" + i + "行");
            }

            CarOwner bean = new CarOwner();
            bean.setType(type);
            bean.setName(String.valueOf(quMap.get("姓名")));
            bean.setPhone(String.valueOf(quMap.get("手机号")));
            bean.setWork_unit(String.valueOf(quMap.get("工作单位")));
            bean.setPosition(String.valueOf(quMap.get("职务")));

            bean.setIs_del(GlobalData.ISDEL_NO);
            bean.setCreate_time(new Date());
            bean.setUpdate_user(sysUser.getId());
            bean.setCreate_user_name(sysUser.getUser_name());
            bean.insertOrUpdate();

        }
        return ResultInfo.error("导入成功");
    }

    public ResultInfo templateExport(PageVo<CarOwner> data) {
        TemplateExportParams params = new TemplateExportParams("车主管理导出.xlsx");
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

        PageInfo<CarOwner> CarOwners = this.selectAll(data);
        CarOwners.getList().forEach(carOwner -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", carOwner.getName());
            map.put("phone", carOwner.getPhone());
            map.put("type_name", carOwner.getType_name());
            map.put("create_time", String.valueOf(carOwner.getCreate_time()));
            map.put("create_user_name", carOwner.getCreate_user_name());
            listMap.add(map);
        });

        Map resultMap = new HashMap();
        resultMap.put("maplist", listMap);

        Workbook workbook = ExcelExportUtil.exportExcel(params, resultMap);
        File savefile = new File(localPath);
        FileOutputStream fos = null;
        String name = IdUtils.simpleUUID() + ".xlsx";
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        try {
            fos = new FileOutputStream(localPath + name);
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultInfo.success(name);
    }
}
