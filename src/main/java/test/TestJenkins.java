package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Scm;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.sonar.wsclient.SonarClient;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import test.data.DataFactory;
import test.data.Project;
import test.data.SourceCodeManagementSystem;
import test.sonardata.Issue;
import test.sonardata.Issues;
//  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 
class TestJenkins {

    String remoteRepository  = "https://github.com/jiantsquid/core.git" ;
	String localRepository = "C:\\Users\\Greg\\Documents\\development\\gitrepo"  ;
	String pomFile2 = localRepository + "\\pom.xml" ;
	
	private File localRepositoryFile  ;

	
	
	 //////////////////////////////////////////////////
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
    
   
    private test.data.Project cloneJiantSquidRepository() throws InvalidRemoteException, TransportException, GitAPIException {
    	
		deleteDirectory( localRepositoryFile ) ;
		localRepositoryFile.mkdirs() ;
		
		// checkout repository
		return checkoutProject( remoteRepository, localRepository ) ;
    }  
    
    private test.data.Project checkoutProject( String url, String localRep ) {
    	// checkout repository
		SourceCodeManagementSystem scm = DataFactory.createSourceCodeManagementSystem( null, null, url, localRep) ;
		if( !scm.checkout() ) {
			return null ;
		}
			
		// return new project
		return DataFactory.createProject( localRep, scm ) ;
    }
//////////////////////////////////////////////////
    private void compileProject( File pomFile, File localRepository ) throws IOException {
		new MavenBuilder().buid( localRepository, pomFile ) ;
    }
    
    private Issues analyzeProject() throws JsonMappingException, JsonProcessingException {
		new MavenBuilder().analyze( localRepositoryFile ) ;
		
		SonarClient client = SonarClient.builder()
				.login( "admin" )
				.password( "jiantsquid" ).url( "http://localhost:9000" ).build() ;
		String response = client.get( "api/issues/search", "project", "com.jiantsquid.poc:com.jiantsquid.core" ) ;
		
		System.out.println( "____________________________________________________________________________________" ) ;
		JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET ) ;
		jsonFactory.disable( JsonParser.Feature.AUTO_CLOSE_SOURCE ) ;
		jsonFactory.disable( JsonGenerator.Feature.IGNORE_UNKNOWN ) ;
		
		ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false ) ;
		
		
		return objectMapper.readValue( response, Issues.class ) ;
    }
    
    private Map<Project,Boolean> checkoutStatus = new HashMap<>() ;
 
	private void run() throws IOException, InterruptedException, RefAlreadyExistsException, 
			RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException, XmlPullParserException {

		localRepositoryFile = new File( localRepository ) ;
		
		// get source code from github		
		Project project = cloneJiantSquidRepository() ;
		new MavenBuilder().buid(localRepositoryFile, new File( pomFile2 ) ) ;
		
		
		// Get dependency details
		project = DataFactory.createProject( project ) ;
		
		Map<String,Project> dependencies = new HashMap<>() ;
		for( Project dependency : project.getDependencies() ) {
			if( dependencies.containsKey( dependency.getId() ) ) {
				System.out.print( "DEPENDENCY: " + dependency.getId() + " already checked out" ) ;
				continue ;
			}
				
			SourceCodeManagementSystem scm = dependency.getSMC() ;
			System.out.print( "CHECKOUT DEPENDENCY: " + dependency.getId() ) ;
			if( scm != null ) {
				System.out.println() ;
				boolean status = dependency.getSMC().checkout() ;
				dependencies.put( project.getId(), project ) ;
				checkoutStatus.put( dependency, status ) ;
			} else {
				System.out.println( " SCM NULL" ) ;
			}
		}
		
		System.out.println( "________________________________________________________________" ) ;
		
		for( Map.Entry<Project,Boolean> entry : checkoutStatus.entrySet() ) {
			System.out.println( entry.getKey().getId() + " " + entry.getValue() ) ;
		}
		System.out.println( "________________________________________________________________" ) ;
		// compile maven
		//compileProject( new File( pomFile2 ), localRepositoryFile ) ;
		//compileDependencies( deps ) ;
		
//		Issues issues = analyzeProject( ) ; 
//		
//		System.out.println( issues.getIssues().size() + " " + issues.getComponents().size() ) ; 
//		System.out.println( "REPORT REPORT REPORT REPORT REPORT" ) ;
//		for( Issue issue : issues.getIssues() ) {
//			System.out.println( issue.getRule() + " " + issue.getResolution() + " " 
//					+ issue.getScope() + " " + issue.getComponent() + " " + issue.getDebt() + " " 
//					+ issue.getEffort() + " " + issue.getMessage() ) ;
//		}
		//NEED TO CREATE PROJECT WITH DEPENDENCIES ISSUES, AND SCM's HERE
	}
	
	private File getPomFile( String groupId, String artifactId, String version ) {
		String groupIdPath =groupId.contains( "." ) ? groupId.replace( ".", File.separator ) : groupId ;
		String depPath = this.localRepositoryFile + File.separator 
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

	
	private void compileDependencies( List<Dependency> dependencies ) throws IOException, XmlPullParserException, InvalidRemoteException, TransportException, GitAPIException {
				
		for( Dependency dep : dependencies ) { 
			System.err.println( "*********COMPILE DEPENDENCY " + dep.getGroupId()+ ":" + dep.getArtifactId() ) ;
			String groupIdPath = dep.getGroupId().contains( "." ) ? dep.getGroupId().replace( ".", File.separator ) : dep.getGroupId() ;
			File pom = getPomFile( dep.getGroupId(), dep.getArtifactId(), dep.getVersion() ) ;
			
			MavenXpp3Reader mavenreader = new MavenXpp3Reader();
			
			Model model          = mavenreader.read( new FileReader( pom ) ) ;
			
			Parent parent = model.getParent() ;
			File parentPom = null ;
			if( parent != null ) {
				parentPom = getPomFile( parent.getGroupId(), parent.getArtifactId(), parent.getVersion() ) ;
			}
			Scm scm = model.getScm() ;
			if( scm != null ) {
				System.err.println( "SCM: " + scm.getConnection() + " " + scm.getUrl() + " " + scm.getTag() ) ;
				
//				File directory = new File( localRepositoryFile + File.separator + scm.getTag() ) ;
//				Git.cloneRepository().setDirectory( directory )
//	            .setURI(scm.getUrl() ).call();
//				
//				InvocationRequest request = new DefaultInvocationRequest();
//				request.setPomFile( new File( directory.getCanonicalPath() + File.separatorChar + "pom.xml" ) ) ;
//				request.setGoals( Collections.singletonList( "compile" ) );
//				request.setLocalRepositoryDirectory( depDirectory ) ;
//				request.setRecursive( true ) ;
//				new MavenBuilder().invokeMaven( request ) ;
			} else if( parentPom != null ){
				Parent p = parent ;
				while( p != null ) {
					System.err.println( "------> NO SCM FOR " + groupIdPath + " look in parent pom:") ;
					System.err.println( parentPom.getAbsolutePath() ) ;
					Model parentModel          = mavenreader.read( new FileReader( parentPom ) ) ;
					MavenProject parentProject = new MavenProject(parentModel);
					Scm parentSCM = parentProject.getScm() ;
					System.err.println( "parent SCM: " + (parentSCM== null ? "NO SCM" : parentSCM.getUrl() ) ) ;
					System.err.println( "PARENT parent=" + parentProject.getParent() ) ;
					parentPom = getPomFile( p.getGroupId(), p.getArtifactId(), p.getVersion() ) ;
					p = parentModel.getParent() ;
				}
			} else {
				System.err.println( "cannot find SCM" ) ;
			}
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, 
	       RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, 
	       CheckoutConflictException, GitAPIException, XmlPullParserException {
		new TestJenkins().run() ;
	}

}
