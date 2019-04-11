package com.wsdl;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name="wsdl")
public class WsdlMojo extends AbstractMojo {
	
	@Parameter(required=true)
	private String className;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		WsdlService service = new WsdlService();
		try {
			Class<?> clazz = Class.forName(className);
			service.generator(clazz);
		} catch (ClassNotFoundException e) {
			getLog().error(e);
		}
	}
	
}
