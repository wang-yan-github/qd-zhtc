package com.jsdc.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BaseService<D extends BaseDao<T>, T> implements BaseMapper<T> {
    @Autowired
    private BaseMapper<T> mapper;
    @Autowired
    protected D dao;

    @Override
    public int insert(T t) {
        return mapper.insert(t);
    }

    @Override
    public int deleteById(Serializable serializable) {
        return mapper.deleteById(serializable);
    }

    @Override
    public int deleteById(T entity) {
        return mapper.deleteById(entity);
    }

    @Override
    public int deleteByMap(Map<String, Object> map) {
        return mapper.deleteByMap(map);
    }

    @Override
    public int delete(Wrapper<T> wrapper) {
        return mapper.delete(wrapper);
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> collection) {return mapper.deleteBatchIds(collection);}

    @Override
    public int updateById(T t) {
        return mapper.updateById(t);
    }

    @Override
    public int update(T t, Wrapper<T> wrapper) {
        return mapper.update(t, wrapper);
    }

    @Override
    public T selectById(Serializable serializable) {
        return mapper.selectById(serializable);
    }

    @Override
    public List<T> selectBatchIds(Collection<? extends Serializable> collection) {return mapper.selectBatchIds(collection);}

    @Override
    public List<T> selectByMap(Map<String, Object> map) {
        return mapper.selectByMap(map);
    }

    @Override
    public T selectOne(Wrapper<T> wrapper) {
        return mapper.selectOne(wrapper);
    }

    @Override
    public Long selectCount(Wrapper<T> wrapper) {
        return mapper.selectCount(wrapper);
    }

    @Override
    public List<T> selectList(Wrapper<T> wrapper) {
        return mapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        return mapper.selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<T> wrapper) {
        return mapper.selectObjs(wrapper);
    }

    @Override
    public <P extends IPage<T>> P selectPage(P page, Wrapper<T> queryWrapper) {
        return mapper.selectPage(page, queryWrapper);
    }

    @Override
    public <P extends IPage<Map<String, Object>>> P selectMapsPage(P page, Wrapper<T> queryWrapper) {
        return mapper.selectMapsPage(page, queryWrapper);
    }
}
