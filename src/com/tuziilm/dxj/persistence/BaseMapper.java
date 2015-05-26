package com.tuziilm.dxj.persistence;

import com.tuziilm.dxj.common.Paginator;
import com.tuziilm.dxj.domain.Id;

import java.util.List;

/**
 * ibatis操作表的Mapper基础接口
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 */
public interface BaseMapper<T extends Id> {
	
    int deleteById(Integer id);
    
    int deleteByIds(int ids[]);

    int insert(T channel);

    List<T> selectAll();
    
    List<T> select(Paginator page);

    int count(Paginator page);

    T selectById(Integer id);
    
    int updateByIdSelective(T t);
    
    int updateById(T t);
}