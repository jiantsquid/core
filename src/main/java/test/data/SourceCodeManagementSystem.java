package test.data;

import com.jiantsquid.core.data.Data;

public abstract class SourceCodeManagementSystem extends Data {

	protected static final String URI = "URI" ;
	protected static final String LOCAL_REPOSITORY = "LOCAL_REPOSITORY" ;
	
	SourceCodeManagementSystem( String uri, String localRepository ) {
		if( uri != null ) {
			setAttribute( URI, uri ) ;
		}
		setAttribute( LOCAL_REPOSITORY, localRepository ) ;
	}
	
	public abstract boolean checkout() ;
}
