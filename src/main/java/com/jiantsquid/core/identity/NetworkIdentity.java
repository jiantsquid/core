package com.jiantsquid.core.identity;

public class NetworkIdentity extends Identity {

	protected NetworkIdentity() {}
	
	public NetworkIdentity( String host, int port ) {
		
		this.setAttribute( "host", host ) ;
		this.setAttribute( "port", String.valueOf( port ) ) ;
	}

	@Override
	public String getId() {
		return getHost() + ":" + getPort() ;
	}

	public String getHost() {
		return (String) getAttribute( "host" ) ;
	}
	
	public int getPort() {
		return Integer.parseInt( getAttribute( "port" ) ) ;
	}
	
	@Override
	public boolean is(Identity entity) {
		return entity.getId().equals( getId() ) ;
	}
}
