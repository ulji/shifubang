package com.xskj.shifubang.been;

import java.util.List;

//实体类

public class JpushAllOrdersBeen {
	private String msg;// 消息
	private String status;// 返回的状态

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

	public static class Object {
		private String sendAddress; // 地址
		private String orderCreateTime; // 下单时间
		private String orderState; // 状态
		
		private String buyerName; // 买家姓名
		private String buyerPhone; // 买家电话
		private String elevator; // 电梯
		private String column4; // 详细地址
		private String serviceTime; // 接单开始时间
		private String orderPrice; // 价格
		public String getOrderPrice() {
			return orderPrice;
		}

		public void setOrderPrice(String orderPrice) {
			this.orderPrice = orderPrice;
		}

		public String getServiceTime() {
			return serviceTime;
		}

		public void setServiceTime(String serviceTime) {
			this.serviceTime = serviceTime;
		}

		public String getBuyerName() {
			return buyerName;
		}

		public void setBuyerName(String buyerName) {
			this.buyerName = buyerName;
		}

		public String getBuyerPhone() {
			return buyerPhone;
		}

		public void setBuyerPhone(String buyerPhone) {
			this.buyerPhone = buyerPhone;
		}

		public String getElevator() {
			return elevator;
		}

		public void setElevator(String elevator) {
			this.elevator = elevator;
		}

		public String getColumn4() {
			return column4;
		}

		public void setColumn4(String column4) {
			this.column4 = column4;
		}



		public String getOrderState() {
			return orderState;
		}

		public void setOrderState(String orderState) {
			this.orderState = orderState;
		}

		public String getOrderCreateTime() {
			return orderCreateTime;
		}

		public void setOrderCreateTime(String orderCreateTime) {
			this.orderCreateTime = orderCreateTime;
		}

		public String getColumn3() {
			return column3;
		}

		public void setColumn3(String column3) {
			this.column3 = column3;
		}

		private String column3; // 状态

		public String getSendAddress() {
			return sendAddress;
		}

		public void setSendAddress(String sendAddress) {
			this.sendAddress = sendAddress;
		}

		private String orderId; // item的唯一标识符
		private String column1; // 服务类型

		public String getColumn1() {
			return column1;
		}

		public void setColumn1(String column1) {
			this.column1 = column1;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		private String msg;// 消息
		private String status;// 状态

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

	}
}
