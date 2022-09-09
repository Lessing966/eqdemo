package com.lessing.equipment.common.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * todo 查询参数(分页)
 *
 * @author crazypenguin
 * @version 1.0
 * @created 2018/4/17
 */
public class QueryExt<T> extends Page<T> {
	private static final String PAGE = "page";
	private static final String LIMIT = "limit";
	private static final String ORDER_BY_FIELD = "orderByField";
	private static final String IS_ASC = "isAsc";

	private Map condition;

	public QueryExt(Map<String, Object> params) {
		super(Integer.parseInt(params.getOrDefault(PAGE, 1).toString())
				, Integer.parseInt(params.getOrDefault(LIMIT, 10).toString()));

		String orderByField = params.getOrDefault(ORDER_BY_FIELD, "").toString();
		Boolean isAsc = Boolean.parseBoolean(params.getOrDefault(IS_ASC, Boolean.TRUE).toString());
		if (StringUtils.isNotEmpty(orderByField)) {
			OrderItem orderItem = null;
			if (isAsc) {
				orderItem = OrderItem.asc(orderByField);
			} else {
				orderItem = OrderItem.desc(orderByField);
			}
			this.addOrder(orderItem);
		}

		params.remove(PAGE);
		params.remove(LIMIT);
		params.remove(ORDER_BY_FIELD);
		params.remove(IS_ASC);
		this.condition = params;
	}

	public Map getCondition() {
		return condition;
	}

	public void setCondition(Map condition) {
		this.condition = condition;
	}
}
