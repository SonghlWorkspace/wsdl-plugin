package com.wsdl.test.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowResponse extends BaseResponse {

	private static final long serialVersionUID = 2199947677698246239L;
	
	private ResponseHeader responseHeader;
	
	private String msg;
	
}
