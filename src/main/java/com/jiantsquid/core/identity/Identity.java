package com.jiantsquid.core.identity;

import com.jiantsquid.core.data.Data;

public abstract class Identity extends Data {

	
	protected Identity() {}
	
	public abstract boolean is( Identity entity ) ;
	
	public abstract String getId() ;
	
}
