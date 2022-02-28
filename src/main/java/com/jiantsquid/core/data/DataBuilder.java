package com.jiantsquid.core.data;

import java.util.List;

public abstract class DataBuilder<DATACLASS extends Data> {
 
	protected DATACLASS data ;
	
	public DataBuilder() {
		createData() ;
	}
	
	protected abstract DATACLASS createData() ;
	
	public DataBuilder<DATACLASS> setAttribute( String name, String attribute ) {
		data.setAttribute( name, attribute ) ;
		return this ;
	}
	
	public DataBuilder<DATACLASS> setData( String name, List<Data> data ) {
		this.data.setData( name, data ) ;
		return this ;
	}
	
	public DataBuilder<DATACLASS> setRawData( String name, byte[] rawData ) {
		data.setRawData( name, rawData ) ;
		return this ;
	}
	
	public Data build() {
		return data ;
	}
}
