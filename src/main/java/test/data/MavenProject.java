package test.data;

import java.util.List;


public class MavenProject extends Project {
 
	MavenProject( String localRep, String groupId, String artifactId , String version,
			       List<Project> dependencies, SourceCodeManagementSystem scm ) {
		super( localRep, dependencies, scm ) ;
		if( groupId != null ) {
			setAttribute( "groupId",    groupId ) ;
			setAttribute( "artifactId", artifactId ) ;
			setAttribute( "version",    version ) ;
			StringBuilder builder = new StringBuilder() ;
			builder.append(groupId).append( ":" )
			       .append( artifactId ).append( ":")
			       .append( version );
			setAttribute( "ID", builder.toString() ) ;
		}		
	} 

	@Override
	public String getId() {
		return getAttribute( "ID");
	}
}
