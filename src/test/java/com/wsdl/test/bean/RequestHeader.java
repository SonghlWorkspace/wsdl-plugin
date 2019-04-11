package com.wsdl.test.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestHeader extends BaseRequest {

	private static final long serialVersionUID = -5903100994049061312L;

	private String serviceId;
	
	private String businessAcceptId;
	
	private String serialNumber;
	
	private String version;
	
	private String cmdType;
	
	private String requester;
	
	private String channel;
	
	private String requestTimeStamp;
	
	private String organizationUnitId;
	
	private String teller1;
	
	private String teller2;

}
