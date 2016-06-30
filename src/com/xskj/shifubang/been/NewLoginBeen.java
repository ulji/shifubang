package com.xskj.shifubang.been;

public class NewLoginBeen {
	private String msg;// 消息
	private String status;// 状态
	private String data;// 验证码
	private String userId; //请求结果，用户ID
	private String isAccreditation; //审核状态
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
	private String workerId; //请求结果，账户Id
	
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
