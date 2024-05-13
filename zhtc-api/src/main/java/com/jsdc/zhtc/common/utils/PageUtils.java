package com.jsdc.zhtc.common.utils;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: PageUtils
 * Description:
 * date: 2022/2/17 14:30
 *
 * @author Administrator
 **/
public class PageUtils {

    public static PageInfo pageHelper(List list, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        int total = list.size();
        page.setTotal(total);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        if (startIndex > endIndex) {
            page.addAll(new ArrayList());
            PageInfo pageInfo = new PageInfo<>(page);
            return pageInfo;
        } else {
            page.addAll(list.subList(startIndex, endIndex));
            PageInfo pageInfo = new PageInfo<>(page);
            return pageInfo;
        }
    }
}
