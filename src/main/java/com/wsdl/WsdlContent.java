package com.wsdl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WsdlContent {
	
	private static final String EXPLAIN_FORMAT = "<!-- %s -->";
	
	private static ConcurrentLinkedQueue<String> methodObjs = new ConcurrentLinkedQueue<>();
	
	private static Map<String, Boolean> hashObjs = new HashMap<String, Boolean>();
	
	public static String buildHeader(String namespace) {
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<definitions name=\"IFWXML_MS\" targetNamespace=\""+namespace+"\"");
		sb.append(" xmlns:IFWXML=\""+namespace+"\"");
		sb.append(" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://schemas.xmlsoap.org/wsdl/\">");
		sb.append("<types><xsd:schema targetNamespace=\""+namespace+"\">");
		// RequestHeader
		sb.append("<xsd:complexType name=\"RequestHeader\">");
		sb.append("<xsd:sequence>");
		sb.append("<xsd:element name=\"businessAcceptId\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"serialNumber\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"version\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"cmdType\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"requester\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"channel\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"requestTimeStamp\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"teller1\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"teller2\" type=\"xsd:string\"/>");
		sb.append("</xsd:sequence>");
		sb.append("</xsd:complexType>");
		sb.append("<xsd:element name=\"RequestHeader\" type=\"IFWXML:RequestHeader\"/>");
		
		// ResponseHeader
		sb.append("<xsd:complexType name=\"ResponseHeader\">");
		sb.append("<xsd:sequence>");
		sb.append("<xsd:element name=\"hostSerialNo\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"responseTimeStamp\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"errorNo\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"errorInfo\" type=\"xsd:string\"/>");
		sb.append("</xsd:sequence>");
		sb.append("</xsd:complexType>");
		sb.append("<xsd:element name=\"ResponseHeader\" type=\"IFWXML:ResponseHeader\"/>");
		
		// ErrorInfo
		sb.append("<xsd:complexType name=\"ErrorInfo\">");
		sb.append("<xsd:sequence>");
		sb.append("<xsd:element name=\"errorMessageType\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"errorCode\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"errorMessage\" type=\"xsd:string\"/>");
		sb.append("<xsd:element name=\"errorState\" type=\"xsd:string\"/>");
		sb.append("</xsd:sequence>");
		sb.append("</xsd:complexType>");
		sb.append("<xsd:element name=\"ErrorInfo\" type=\"IFWXML:ErrorInfo\"/>");
		
		return sb.toString();
	}
	
	public static String buildMethod(String methodName, Class<?> in, Class<?> out) {
		StringBuffer sb = new StringBuffer();
		sb.append(explain(methodName));
		sb.append("<xsd:complexType name=\"").append(methodName).append("Request\">");
		sb.append("<xsd:annotation>");
		sb.append("<xsd:documentation>").append(methodName).append("</xsd:documentation>");
		sb.append("</xsd:annotation>");
		sb.append("<xsd:sequence minOccurs=\"0\">");
		sb.append("<xsd:element name=\"requestHeader\" type=\"IFWXML:RequestHeader\"/>");
		sb.append(buildMethodElement(in));
		sb.append("</xsd:sequence>");
		sb.append("</xsd:complexType>");
		sb.append("<xsd:element name=\"").append(methodName).append("Request\" type=\"IFWXML:").append(methodName).append("Request\"/>");
		sb.append("<xsd:complexType name=\"").append(methodName).append("Response\">");
		sb.append("<xsd:sequence minOccurs=\"0\">");
		sb.append("<xsd:element name=\"responseHeader\" type=\"IFWXML:ResponseHeader\"/>");
		sb.append(buildMethodElement(out));
		sb.append("</xsd:sequence>");
		sb.append("</xsd:complexType>");
		sb.append("<xsd:element name=\"").append(methodName).append("Response\" type=\"IFWXML:").append(methodName).append("Response\"/>");
		return sb.toString();
	}
	
	public static String buildBloks(String methodName, Class<?> in, Class<?> out) {
		StringBuffer sb = new StringBuffer();
		sb.append("<message name=\"").append(methodName).append("Request\">");
		sb.append("<part element=\"IFWXML:").append(methodName).append("Request\" name=\"").append(methodName).append("\"/>");
		sb.append("</message>");
		sb.append("<message name=\"").append(methodName).append("Response\">");
		sb.append("<part element=\"IFWXML:").append(methodName).append("Response\" name=\"").append(methodName).append("\"/>");
		sb.append("</message>");
		return sb.toString();
	}
	
	public static String buildPortType(String methodName, Class<?> in, Class<?> out) {
		StringBuffer sb = new StringBuffer();
		sb.append("<operation name=\"").append(methodName).append("\">");
		sb.append("<input message=\"IFWXML:").append(methodName).append("Request\"/>");
		sb.append("<output message=\"IFWXML:").append(methodName).append("Response\"/>");
		sb.append("<fault message=\"IFWXML:ErrorInfo\" name=\"ErrorResponseType\"/>");
		sb.append("</operation>");
		return sb.toString();
	}
	
	public static String buildBindings(String methodName, Class<?> in, Class<?> out) {
		StringBuffer sb = new StringBuffer();
		sb.append("<operation name=\"").append(methodName).append("\">");
		sb.append("<soap:operation soapAction=\"http://www.ibm.com/ima/ifw/").append(methodName).append("\"/>");
		sb.append("<input>");
		sb.append("<soap:body use=\"literal\"/>");
		sb.append("</input>");
		sb.append("<output>");
		sb.append("<soap:body use=\"literal\"/>");
		sb.append("</output>");
		sb.append("<fault name=\"ErrorResponseType\">");
		sb.append("<soap:fault name=\"ErrorResponseType\" namespace=\"\" use=\"literal\"/>");
		sb.append("</fault>");
		sb.append("</operation>");
		return sb.toString();
	}
	
	public static String buildEnds(String serviceName) {
		String uri = serviceName.substring(8).replaceAll("\\.", "/");
		String simpleName = uri.substring(uri.lastIndexOf("/")+1);
		uri = uri.substring(0, uri.lastIndexOf("/")-3);
		StringBuffer sb = new StringBuffer();
		sb.append("<service name=\"").append(simpleName).append("\">");
		sb.append("<port binding=\"IFWXML:").append(simpleName).append("SOAPBinding\" name=\"").append(simpleName).append("SOAPPort\">");
		sb.append("<soap:address location=\"http://rpcserver.test.com:8080/").append(uri).append(simpleName).append("\"/>");
		sb.append("</port>");
		sb.append("</service>");
		sb.append("</definitions>");
		return sb.toString();
	}
	
	private static String buildMethodElement(Class<?> clazz) {
		StringBuffer sb = new StringBuffer();
		for(Class<?> cla = clazz; cla != null; cla = cla.getSuperclass()) {
			if(cla.getName().equals("com.wsdl.test.bean.BaseResponse") || cla.getName().equals("com.wsdl.test.bean.BaseRequest")) {
				break;
			}
			for(Field field : cla.getDeclaredFields()) {
				if(field.getName().equals("serialVersionUID") || field.getName().equals("requestHeader") ||
						 field.getName().equals("responseHeader")) {
					continue;
				}
				Type type = field.getGenericType();
				if(isBasicType(type, true)) {
					sb.append("<xsd:element name=\"").append(field.getName()).append("\" minOccurs=\"0\" maxOccurs=\"1\" type=\"xsd:string\"/>");
				} else if(type instanceof ParameterizedType) {
					Type[] types = ((ParameterizedType) type).getActualTypeArguments();
					if(types.length != 1) {
						throw new RuntimeException("wsdl不支持非List，Set类型" + field);
					}
					if(isBasicType(types[0], false)) {
						sb.append("<xsd:element name=\"").append(field.getName()).append("\" maxOccurs=\"unbounded\" type=\"xsd:string\"/>");
					} else {
						int p = types[0].getTypeName().lastIndexOf(".");
						sb.append("<xsd:element name=\"").append(field.getName()).append("\" maxOccurs=\"unbounded\" type=\"IFWXML:").
						append(types[0].getTypeName().substring(p+1)).append("\"/>");
						if(hashObjs.get(types[0].getTypeName()) == null) {
							methodObjs.add(types[0].getTypeName());
							hashObjs.put(types[0].getTypeName(), false);
						}
					}
				} else if(field.getName().equals("requestHeader") || field.getName().equals("responseHeader")) {
					continue;
				} else {
					int p = type.getTypeName().lastIndexOf(".");
					sb.append("<xsd:element name=\"").append(field.getName()).append("\"  minOccurs=\"0\" maxOccurs=\"1\" type=\"IFWXML:").
					append(type.getTypeName().substring(p+1)).append("\"/>");
					if(hashObjs.get(type.getTypeName()) == null) {
						methodObjs.add(type.getTypeName());
						hashObjs.put(type.getTypeName(), false);
					}
				}
			}
		}
		return sb.toString();
	}
	
	private static String explain(String s) {
		return String.format(EXPLAIN_FORMAT, s);
	}
	
	private static boolean isBasicType(Type type, boolean bool) {
		if(type.getTypeName().startsWith("java.util.List") || type.getTypeName().startsWith("java.util.Set")) {
			return false;
		} else if(type.getTypeName().equals("java.lang.String")) {
			return true;
		} else if(type.getTypeName().equals("java.lang.Double")) {
			return true;
		} else if(type.getTypeName().equals("java.lang.Long")) {
			return true;
		} else if(type.getTypeName().equals("java.lang.Integer")) {
			return true;
		} else if(type.getTypeName().equals("int")) {
			return true;
		} else if(type.getTypeName().equals("long")) {
			return true;
		}
		if(bool) {
			if(type.getTypeName().equals("java.util.Date")) {
				throw new RuntimeException("wsdl不支持此类型" + type.getTypeName());
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static String buildObjs() {
		StringBuffer sb = new StringBuffer();
		try {
			while(methodObjs.size() > 0) {
				String obj = methodObjs.poll();
				Class<?> clazz = Class.forName(obj);
				sb.append("<xsd:complexType name=\"").append(clazz.getSimpleName()).append("\">");
				sb.append("<xsd:sequence maxOccurs=\"1\" minOccurs=\"0\" >");
				sb.append(buildMethodElement(clazz));
				sb.append("</xsd:sequence>");
				sb.append("</xsd:complexType>");
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
	
}
