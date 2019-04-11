package com.wsdl.test;

import com.wsdl.WsdlService;
import com.wsdl.test.bean.ShowService;

public class Test {
	
	public static void main(String[] args) {
		WsdlService w = new WsdlService();
		w.generator(ShowService.class);
	}
	
}
