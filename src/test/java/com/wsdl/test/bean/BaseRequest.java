package com.wsdl.test.bean;

import java.io.Serializable;

public abstract class BaseRequest implements RpcRequest,Serializable {

	private static final long serialVersionUID = -2072708159842353321L;

	@Override
	public String toString() {
		return "BaseRequest{}";
	}
	
}
