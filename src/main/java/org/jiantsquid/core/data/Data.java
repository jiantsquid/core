package org.jiantsquid.core.data;

import java.io.Serializable;
import java.util.ArrayList;

// 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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Data implements Serializable {

	private final Map<String,String>     attributes = new ConcurrentHashMap<>() ;
	private final Map<String,List<? extends Data>> data       = new ConcurrentHashMap<>() ;
	private final Map<String,byte[]>     rawData    = new ConcurrentHashMap<>() ;
	
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
		if( data != null ) {
			attributes.put( name, data ) ;
		}
	}
	
	protected String getAttribute( String name ) {
		return attributes.get( name ) ;
	}
	
	protected void setData( String name, List<? extends Data> data ) {
		this.data.put( name, data ) ;
	}
	
	protected List<? extends Data> getData( String name ) {
		return new ArrayList<>( data.get( name ) ) ;
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
