package com.wsdl.test.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowRequest extends BaseRequest {

	private static final long serialVersionUID = 3406668130275201571L;
	
	private RequestHeader requestHeader;
	
	private String msg;

}
