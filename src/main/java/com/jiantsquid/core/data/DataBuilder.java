package com.jiantsquid.core.data;

public abstract class DataBuilder<DATACLASS extends Data> {
 
	protected DATACLASS data ;
	
	public DataBuilder() {
		createData() ;
	}
	
	abstract DataBuilder createData() ;
	
	public Data setAttribute( String name, String attribute ) {
		data.setAttribute( name, attribute ) ;
		return data ;
	}
	
	public void setData( String name, Data data ) {
		data.setData( name, data ) ;
	}
	
	public Data setRawData( String name, byte[] rawData ) {
		data.setRawData( name, rawData ) ;
		return data ;
	}
	
	public Data build() {
		return data ;
	}
}
