package com.xskj.shifubang.been;

import java.util.List;

//ʵ����

public class DetailOrdersBeen {

	private String msg;// ��Ϣ
	private String status;// ״̬
	private List<T> object;// ����
	
	public List<T> getObject() {
		return object;
	}

	public void setObject(List<T> object) {
		this.object = object;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private java.util.List<ListEntity> List;

	public java.util.List<ListEntity> getList() {
		return List;
	}

	public void setList(java.util.List<ListEntity> list) {
		List = list;
	}
	
	public static class T {
		private String orderState;

		public String getOrderState() {
			return orderState;
		}

		public void setOrderState(String orderState) {
			this.orderState = orderState;
		}
	}

	public static class ListEntity {
		private String goodsImg;

		public String getGoodsImg() {
			return goodsImg;
		}

		public void setGoodsImg(String goodsImg) {
			this.goodsImg = goodsImg;
		}

	}

}
