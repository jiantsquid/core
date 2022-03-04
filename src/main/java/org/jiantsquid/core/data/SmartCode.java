package org.jiantsquid.core.data;


public class SmartCode extends Data {

	
	public final static String MAIN_CLASS = "MAIN_CLASS" ;
	public final static String MAIN_CODE  = "MAIN_CODE" ;
	
	public String getMainclass() {
		return (String) getAttribute( MAIN_CLASS ) ;
	}
	public String getCode() {
		return (String) getAttribute( MAIN_CODE ) ;
	}
	
	public void setMainClass( String mainClass ) {
		setAttribute( MAIN_CLASS, mainClass ) ;
	}
	
	public void setCode( String code ) {
		setAttribute( MAIN_CODE, code ) ;
	}
	
	
}
