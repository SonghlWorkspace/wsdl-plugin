package com.wsdl.test.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseHeader extends BaseResponse {

	private static final long serialVersionUID = -6673909981473785067L;

	private String hostSerialNo;
	
	private String responseTimeStamp;
	
	private String errNo;
	
	private String errorInfo;
	
}
