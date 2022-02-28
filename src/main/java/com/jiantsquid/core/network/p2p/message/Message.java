package com.jiantsquid.core.network.p2p.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.jiantsquid.core.data.Data;
import com.jiantsquid.core.identity.NetworkIdentity;


public class Message extends Data {

	private NetworkIdentity from ;
	
	private List<NetworkIdentity> route = new ArrayList<>() ;
	
	protected Message() {}
	
	public Message( NetworkIdentity from ) {
		
		this.from = from ;
	}
	
	public void addRouteEntity( NetworkIdentity entity ) {
		route.add( entity ) ;
	}
	
	public boolean allreadyProcessedBy( NetworkIdentity entity ) {
		return route.contains( entity ) ;
	}
	
	public void addUserData( String key, Data userData ) {
		setData( key, Collections.singletonList( userData ) ) ;
	}
	
	public Map<String,String> getParameters() {
		return new HashMap<>( getAttributes() ) ;
	}
	
	public String getParameter( String parameterName ) {
		return getAttribute( parameterName ) ;
	}

	public NetworkIdentity getFrom() {
		return from;
	}
	
	protected void setFrom( NetworkIdentity from ) {
		this.from = from ;
	}
	@Override
	public boolean equals( Object o ) {
		if( !( o instanceof Message) ) {
			return false ;
		}
		
		Message m = (Message) o ;
		return from.equals( m.getFrom() ) && getAttributes().equals(m.getAttributes() ) ;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash( from, getAttributes() ) ;
	}
}
