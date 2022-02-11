package com.jiantsquid.core.data;

// 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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

	private final Map<String,String> attributes = new ConcurrentHashMap<>() ;
	private final Map<String,Data>   data       = new ConcurrentHashMap<>() ;
	private final Map<String,byte[]> rawData    = new ConcurrentHashMap<>() ;
	
	public static final String TIMESTAMP = "TIMESTAMP" ;
	
	protected Data() {
		attributes.put( TIMESTAMP, Long.toString( System.currentTimeMillis() ) ) ;
	}
	
	public long getTimestamp() {
		return Long.parseLong( TIMESTAMP ) ;
	}
	
	public Map<String,String> getAttributes() {
		return new HashMap<>( attributes ) ;
	}
	
	protected Map<String,String> getAttributes_() {
		return attributes ;
	}
	
	protected void setAttribute( String name, String data ) {
		attributes.put( name, data ) ;
	}
	
	protected String getAttribute( String name ) {
		return attributes.get( name ) ;
	}
	
	protected void setData( String name, Data data ) {
		this.data.put( name, data ) ;
	}
	
	protected Data getData( String name ) {
		return data.get( name ) ;
	}
	
	protected void setRawData( String name, byte[] data ) {
		rawData.put( name, data ) ;
	}
	
	protected byte[] getRawData( String name ) {
		return rawData.get( name ) ;
	}
	
	@Override
	public String toString() {	
		return attributes.toString() ;
	}
}
