package com.jiantsquid.core.data;

public class DataBuilder {
 
	private Data data = new Data() ;
	
	public DataBuilder() {}
	
	public Data setAttribute( String name, String attribute ) {
		data.setAttribute( name, attribute ) ;
		return data ;
	}
	
	public Data setRawData( String name, byte[] rawData ) {
		data.setRawData( name, rawData ) ;
		return data ;
	}
	
	public Data build() {
		return data ;
	}
}
