package test.data;

import java.util.Collections;
import java.util.List;

import org.jiantsquid.core.data.Data;

public abstract class Project extends Data {

	protected static final String LOCAL_REPOSITORY = "LOCAL_REPOSITORY" ;
	protected static final String SCM              = "SCM" ;
	protected static final String DEPENDENCIES     = "DEPENDENCIES" ;
	
	Project() {}
	
	Project( String localRep, List<Project> dependencies, SourceCodeManagementSystem scm ) {
		setAttribute( LOCAL_REPOSITORY, localRep ) ;
		setData( SCM, Collections.singletonList( scm ) ) ;
		if( dependencies != null ) {
			setData( DEPENDENCIES, dependencies ) ;
		}
	}
	
	public String getLocalRepository() {
		return getAttribute( LOCAL_REPOSITORY ) ;
	}
	
	public SourceCodeManagementSystem getSMC() {
		return (SourceCodeManagementSystem) getData( SCM ).get( 0 ) ;
	}
	
	public List<Project> getDependencies() {
		return (List<Project>) getData( DEPENDENCIES ) ;
	}
	
	public abstract String getId() ;
}
