package com.xskj.shifubang.been;

public class NewLoginBeen {
	private String msg;// ��Ϣ
	private String status;// ״̬
	private String data;// ��֤��
	private String userId; //���������û�ID
	private String isAccreditation; //���״̬
	public String getIsAccreditation() {
		return isAccreditation;
	}
	public void setIsAccreditation(String isAccreditation) {
		this.isAccreditation = isAccreditation;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	private String workerId; //���������˻�Id
	
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
