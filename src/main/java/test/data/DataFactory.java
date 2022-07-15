package test.data;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

//import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Scm;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.utils.Settings;
import org.owasp.dependencycheck.dependency.Dependency;

public class DataFactory {

	private static final DataFactory instance = new DataFactory() ;
	
	public DataFactory instance() {
		return instance ;
	}
	
	public static Project createProject( String localRep, SourceCodeManagementSystem scm ) {
		return new MavenProject( localRep, null, null, null, null, scm ) ;
	}
	
	private static File getPomFile( String localRepositoryFile, String groupId, String artifactId, String version ) {
		String groupIdPath =groupId.contains( "." ) ? groupId.replace( ".", File.separator ) : groupId ;
		String depPath = localRepositoryFile + File.separator 
				+ groupIdPath  + File.separator 
				+ artifactId + File.separator 
				+ version + File.separator ;
		File depDirectory = new File( depPath ) ;
		File[] pom = depDirectory.listFiles( new FilenameFilter()  {
	
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith( ".pom" ) ;
			}
			
		}) ;
		
		return pom == null || pom.length == 0 ? null : pom[0] ;
	}
	
	private static String parseProperties( Properties properties, String stringToParse ) {
		int index = -1 ;
		if( stringToParse == null ) {
			return null ;
		}
		while( ( index = stringToParse.indexOf( '$' ) ) > -1 ) {
			int index2 = stringToParse.indexOf( '}' ) ;
			String propertyName = stringToParse.substring( index + 2, index2 ) ;
			String property     = properties.getProperty( propertyName ) ;
			stringToParse = stringToParse.replace( "${" + propertyName + "}", property == null ? "" : property ) ;
		}
		return stringToParse ;
	}
	
	public static Project createProject( Project project ) {
 		MavenXpp3Reader mavenreader = new MavenXpp3Reader();
		String pomFile2 = project.getLocalRepository() + "\\pom.xml" ;
		Model model;
		try {
			model = mavenreader.read(new FileReader(pomFile2));
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
			return null ;
		}
		org.apache.maven.project.MavenProject  mavenProject = new org.apache.maven.project.MavenProject(model);

		Settings settings = new Settings() ;
		Engine engine = new Engine( settings ) ;
		List<Dependency> dependenciesList = engine.scan( project.getLocalRepository() ) ;
		engine.close();
		List<Project> dependencies = new ArrayList<>() ;
		for( Dependency dep : dependenciesList ) {
			System.out.println( "-> MAVEN dependency: " + dep.toString() ) ;
			mavenreader = new MavenXpp3Reader();
			String actualPath = dep.getActualFilePath() ;
			String pom = getPomFile( actualPath ) ;
			try {
				model       = mavenreader.read( new FileReader( actualPath.substring(0, actualPath.lastIndexOf( File.separator ) ) + File.separator + pom ) ) ;
			} catch (IOException | XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue ;
			}
		
			Properties properties = model.getProperties() ;
			String groupId    = parseProperties( properties, model.getGroupId() ) ;
			String artifactId = parseProperties( properties, model.getArtifactId() ) ;
			String version    = parseProperties( properties, model.getVersion() ) ;
			
			Scm scm = checkParentsScms( properties, project.getLocalRepository(), model.getParent() ) ;
			String depPath = project.getLocalRepository()+ File.separator + "dependencies" 
					+ File.separator + groupId
					+ File.separator + artifactId
					+ File.separator + version;
			
			
			
			SourceCodeManagementSystem scms = null ;
			if( scm != null ) {
				String devConnection = scm.getDeveloperConnection() ;
				String url = parseProperties( properties, scm.getUrl() ) ;
				scms = createSourceCodeManagementSystem( scm.getConnection(), devConnection, url, depPath ) ;
			}
			
			dependencies.add( new MavenProject( depPath,
					groupId, artifactId, version, null, scms ) ) ;
		}
		return new MavenProject( project.getLocalRepository(), 
				mavenProject.getGroupId(), mavenProject.getArtifactId(), mavenProject.getVersion(),
				dependencies, project.getSMC() ) ; 
				
	}
	
	private static String getPomFile( String actualpath ) {
		File dir = new File( actualpath.substring( 0, actualpath.lastIndexOf( File.separator) ) ) ;
		String[] pom = dir.list( new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				
				return "pom.xml".equals( name ) || name.endsWith( ".pom" ) ;
			}
			
		}) ;
		return pom == null || pom.length == 0 ? null : pom[0] ;
	}
	
	private static Scm checkParentsScms( Properties properties, String localRepository, Parent parent ) {
		MavenXpp3Reader mavenreader = new MavenXpp3Reader();
		Scm scm = null ;
		while( parent != null && scm == null ) {
			File parentPom = getPomFile( localRepository, parent.getGroupId(), parent.getArtifactId(), parent.getVersion() ) ;
			
			Model model = null ;
			try {
				model = mavenreader.read( new FileReader( parentPom ) ) ;
				Properties properties2 = model.getProperties() ;
				for( Entry<Object,Object> entry : properties2.entrySet() ) {
					properties.setProperty( (String) entry.getKey(), (String) entry.getValue() ) ;
				}
			} catch (IOException | XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if( model != null ) {
				scm = model.getScm() ;
				System.out.println( "           PARENT SCM " + ( scm == null ? "NULL" :scm.getUrl() ) ) ;
			} else {
				System.out.println( "                     cannot create model for pom " + parentPom ) ;
			}
			
			parent = model.getParent() ;
		}
		return scm ;
		
	}
	 public static SourceCodeManagementSystem createSourceCodeManagementSystem( String connection, String devConnection, String uri, String localRepository ) {
		 
		 String[] splittedConnection = connection == null ? null : connection.split( ":" ) ;
		 SourceCodeManagementSystem scm = null ;
		 if( splittedConnection == null || "git".equals( splittedConnection[1] ) ) {
			 scm = new GitSCM( uri, localRepository ) ;
		 } else if( "svn".equals( splittedConnection[1] ) ) {
			 scm = new SvnSCM( devConnection, localRepository ) ;
		 }
		 return scm ;
	 }
}
