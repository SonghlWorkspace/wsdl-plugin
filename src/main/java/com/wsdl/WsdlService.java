package com.wsdl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Method;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WsdlService {
	
	private static final String namespace = "http://shl.wsdl_demo.com/soap/shl/"; 
	
	public void generator(Class<?> clazz) {
		StringBuilder sbHeader = new StringBuilder();
		StringBuilder sbMethod= new StringBuilder();
		StringBuilder sbBloks= new StringBuilder();
		StringBuilder sbPortType = new StringBuilder();
		StringBuilder sbBindings = new StringBuilder();
		StringBuilder sbServices = new StringBuilder();
		sbHeader.append(WsdlContent.buildHeader(namespace));
		sbPortType.append("<portType name=\"").append(clazz.getSimpleName()).append("\">");
		sbBindings.append("<binding name=\"").append(clazz.getSimpleName()).append("SOAPBinding\" type=\"IFWXML:").append(clazz.getSimpleName()).append("\">");
		sbBindings.append("<soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\"/>");
		for (Method method : clazz.getMethods()) {
			Class<?>[] inputs = method.getParameterTypes();
			if(inputs.length != 1) {
				log.error("");
				continue;
			}
			if(method.getReturnType().getName().equals("void")) {
				log.error("");
				continue;
			}
			Class<?> in = inputs[0];
			Class<?> out = method.getReturnType();
			sbMethod.append(WsdlContent.buildMethod(method.getName(), in, out));
			sbBloks.append(WsdlContent.buildBloks(method.getName(), in, out));
			sbPortType.append(WsdlContent.buildPortType(method.getName(), in, out));
			sbBindings.append(WsdlContent.buildBindings(method.getName(), in, out));
		}
		sbMethod.append(WsdlContent.buildObjs());
		sbMethod.append("</xsd:schema>");
		sbMethod.append("</types>");
		
		sbBloks.append("<message name=\"ErrorInfo\">");
		sbBloks.append("<part element=\"IFWXML:ErrorInfo\" name=\"ErrorInfo\"/>");
		sbBloks.append("</message>");
		
		sbPortType.append("</portType>");
		
		sbBindings.append("</binding>");
		
		sbServices.append(WsdlContent.buildEnds(clazz.getName()));
		StringBuffer total = new StringBuffer();
		total.append(sbHeader).append(sbMethod).append(sbBloks).append(sbPortType).append(sbBindings).append(sbServices);
		File file = new File(clazz.getSimpleName() + ".wsdl");
		try {
			formatToXML(total.toString(), file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void formatToXML(String str, File file) throws Exception {
		SAXReader reader = new SAXReader();
		StringReader stringReader = new StringReader(str);
		Document document;
		document = reader.read(stringReader);
		stringReader.close();
		FileOutputStream fs = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fs, "utf-8");
		XMLWriter writer = new XMLWriter(osw, OutputFormat.createPrettyPrint());
		writer.write(document);
		writer.close();
		fs.close();
		osw.close();
	}
	
}
