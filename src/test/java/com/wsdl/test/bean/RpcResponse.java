package com.wsdl.test.bean;

public interface RpcResponse {
	
	/**
	 * 判断是否成功
	 * 
	 * @return
	 */
	Boolean isSuccess();
	
	/**
	 * 设置处理成功
	 */
	void setSuccess();
	
	/**
	 * 获取返回码
	 * @return
	 */
	String getErrCode();
	
	/**
	 * 设置返回码
	 * @param errCode
	 */
	void setErrCode(String errCode);
	
	/**
	 * 获取返回信息
	 * @return
	 */
	String getErrMsg();
	
	/**
	 * 设置返回信息
	 * @param errMsg
	 */
	void setErrMsg(String errMsg);
	
}
