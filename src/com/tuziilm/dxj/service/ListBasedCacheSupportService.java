package com.tuziilm.dxj.service;

import com.tuziilm.dxj.common.Tuple;
import com.tuziilm.dxj.domain.Id;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ע�⣺**�����ݱ��ܼ̳д˽ӿ�**�����Ӹ��²���ʱ��Ӧ�ü���resetCache(),���򻺴������Ч
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 * @param <T>
 */
public abstract class ListBasedCacheSupportService<T extends Id> extends RedisSupportService<T> {
	protected final AtomicReference<Collection<T>> cache= new AtomicReference<Collection<T>>();

	public ListBasedCacheSupportService() {
		super("list");
	}

	public Collection<T> getCache(){
		Collection<T> expect= cache.get();
		Tuple<Boolean, Long> nldResult = needLoadData();
		if(expect==null || nldResult.first){
			Collection<T> update = listForCache();
			cache.compareAndSet(expect, update);
			version=nldResult.second;
			return update;
		}
		return expect;
	}
	
	protected Collection<T> listForCache(){
		return  listAll();
	}
}
