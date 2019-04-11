package com.wsdl.test.bean;

import java.io.Serializable;

public abstract class BaseResponse implements RpcResponse, Serializable {

	private static final long serialVersionUID = -5890204449539240353L;
	public static final String SUCCESS_CODE = "000000";
	private String errCode;
	private String errMsg;

	@Override
	public Boolean isSuccess() {
		return SUCCESS_CODE.equals(errCode);
	}

	@Override
	public void setSuccess() {
		this.errCode = SUCCESS_CODE;
	}

	@Override
	public String getErrCode() {
		return errCode;
	}

	@Override
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	@Override
	public String getErrMsg() {
		return errMsg;
	}

	@Override
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
	public String toString() {
		return "BaseResponse{errCode='" + errCode + '\'' + ", errMsg='" + errMsg + '\'' + '}';
	}
	
}
