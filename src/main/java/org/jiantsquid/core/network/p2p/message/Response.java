package org.jiantsquid.core.network.p2p.message;

import java.util.Map;

import org.jiantsquid.core.data.Data;
import org.jiantsquid.core.identity.NetworkIdentity;


public class Response extends Message {

	public void merge( NetworkIdentity from, Map<String,String> parameters, Map<String,Data> userData ) {
		
		setFrom( from ) ;
		if( parameters != null ) {
			getAttributes_().putAll( parameters ) ;
		}
		if( userData != null ) {
			userData.putAll( userData ) ;
		}
	}
}
