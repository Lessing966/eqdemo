package com.lessing.equipment.common.utils;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 分页工具类
 *
 * @author crazypenguin
 * @version 1.0.0
 * @createdate 2019/1/2
 */
public class PageUtils<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	//总记录数
	private long totalCount;
	//每页记录数
	private long pageSize;
	//总页数
	private long totalPage;
	//当前页数
	private long currPage;
	//列表数据
	private List<T> list;

	/**
	 * @param list       列表数据
	 * @param totalCount 总记录数
	 * @param pageSize   每页记录数
	 * @param currPage   当前页数
	 */
	public PageUtils(List<T> list, long totalCount, long pageSize, long currPage) {
		Page<T> page = new Page<T>(currPage, pageSize, totalCount);
		page.setRecords(list);
		copy(page);
	}

	public PageUtils(IPage<T> page) {
		copy(page);
	}

	private void copy(IPage<T> page) {
		this.list = page.getRecords();
		this.totalCount = page.getTotal();
		this.pageSize = page.getSize();
		this.currPage = page.getCurrent();
		this.totalPage = page.getPages();
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getCurrPage() {
		return currPage;
	}

	public void setCurrPage(long currPage) {
		this.currPage = currPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
