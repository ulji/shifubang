package com.xskj.shifubang.been;

import java.util.List;

//ʵ����

public class JpushAllOrdersBeen {
	private String msg;// ��Ϣ
	private String status;// ���ص�״̬

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
		private String sendAddress; // ��ַ
		private String orderCreateTime; // �µ�ʱ��
		private String orderState; // ״̬
		
		private String buyerName; // �������
		private String buyerPhone; // ��ҵ绰
		private String elevator; // ����
		private String column4; // ��ϸ��ַ
		private String serviceTime; // �ӵ���ʼʱ��
		private String orderPrice; // �۸�
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

		private String column3; // ״̬

		public String getSendAddress() {
			return sendAddress;
		}

		public void setSendAddress(String sendAddress) {
			this.sendAddress = sendAddress;
		}

		private String orderId; // item��Ψһ��ʶ��
		private String column1; // ��������

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

		private String msg;// ��Ϣ
		private String status;// ״̬

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
