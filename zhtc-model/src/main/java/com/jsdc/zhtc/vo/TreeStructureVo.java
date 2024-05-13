package com.jsdc.zhtc.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TreeStructureVo implements Serializable {
    private Integer id;
    private String label;
    private Integer grade;
    private Integer areaId;
    private Integer streetId;
    private List<TreeStructureVo> children;

}
