package org.jiantsquid.core.data;

import java.util.Date;


public class Log extends Data {

	public final static String MESSAGE   = "message" ;
	public final static String IDENTITY  = "identity" ;
	
	public Log() {}
	
	public Log( String identity, String message ) {
		setAttribute( MESSAGE,   message  ) ;
		setAttribute( IDENTITY,  identity ) ;
	}


	public String getMessage() {
		return (String) getAttribute( MESSAGE ) ;
	}


	public String getIdentity() {
		return (String) getAttribute( IDENTITY ) ;
	}

	@Override
	public String toString() {	
		return new Date( getTimestamp() ).toString() + " " + getIdentity() + " " + getMessage() ;
	}
}
