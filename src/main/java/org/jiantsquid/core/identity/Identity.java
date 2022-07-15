package org.jiantsquid.core.identity;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.jiantsquid.core.data.Data;

public abstract class Identity extends Data {

	private transient Wallet wallet ;
	
	protected Identity() {
		
		try {
			wallet = new Wallet() ;
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract boolean is( Identity entity ) ;
	
	public abstract String getId() ;
	
	public final Wallet getWallet() {
	    return wallet ;
	}
}
