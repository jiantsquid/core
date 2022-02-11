package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.sonar.wsclient.SonarClient;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jiantsquid.core.data.Data;

class TestJenkins {

    String remoteRepository  = "https://github.com/jiantsquid/core.git" ;
	String localRepository = "C:\\Users\\Greg\\Documents\\development\\gitrepo"  ;
	String pomFile2 = localRepository + "\\pom.xml" ;
	

	String settings = "<settings>"
			+ "    <pluginGroups>"
			+ "        <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>"
			+ "    </pluginGroups>"
			+ "    <profiles>"
			+ "        <profile>"
			+ "            <id>sonar</id>"
			+ "            <activation>"
			+ "                <activeByDefault>true</activeByDefault>"
			+ "            </activation>"
			+ "            <properties>"
			+ "                <!-- Optional URL to server. Default value is http://localhost:9000 -->"
			+ "                <sonar.host.url>"
			+ "                  http://myserver:9000"
			+ "                </sonar.host.url>"
			+ "            </properties>"
			+ "        </profile>"
			+ "     </profiles>"
			+ "</settings>" ;
	
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
    
	private void run() throws IOException, InterruptedException, RefAlreadyExistsException, 
			RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException, XmlPullParserException {

		// get source code from github		
		File localRepositoryFile = new File( localRepository ) ;
		deleteDirectory( new File( localRepository ) ) ;
		localRepositoryFile.mkdirs() ;
		
		// clone repository
		Git.cloneRepository().setDirectory( localRepositoryFile )
               .setURI(remoteRepository ).setCloneAllBranches(true).call();
		
		// compile maven
		FileReader reader = null;
		
		MavenXpp3Reader mavenreader = new MavenXpp3Reader();


		Model model = mavenreader.read(new FileReader(pomFile2));
		
		MavenProject project = new MavenProject(model);
		
		List<Dependency> deps = project.getDependencies();

		// Get dependency details
		for (Dependency d: deps) {      
		    System.out.print(d.getArtifactId());
		    System.out.print(":");
		    System.out.println(d.getVersion()); 
		}        
		
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( new File( pomFile2 ) ) ;
		request.setGoals( Collections.singletonList( "compile" ) );
		request.setLocalRepositoryDirectory( localRepositoryFile ) ;
		
		File setting = new File( localRepositoryFile + File.separator + "settings.xml" ) ;
		RandomAccessFile sets = new RandomAccessFile( setting, "rw" ) ;
		sets.writeUTF( settings ) ;
		sets.close();
		
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(new File("C:\\Users\\Greg\\Documents\\development\\apache-maven-3.8.4"));
		invoker.setMavenExecutable( new File("C:\\Users\\Greg\\Documents\\development\\apache-maven-3.8.4\\bin\\mvn.cmd" ) ) ;
		try
		{
		  invoker.execute( request );
		  
		}
		catch (MavenInvocationException e)
		{
		  e.printStackTrace();
		}
		
		
		invoker = new DefaultInvoker();
		invoker.setMavenHome(new File("C:\\Users\\Greg\\Documents\\development\\apache-maven-3.8.4"));
		invoker.setMavenExecutable( new File("C:\\Users\\Greg\\Documents\\development\\apache-maven-3.8.4\\bin\\mvn.cmd" ) ) ;
		request.setGoals( Collections.singletonList( "verify sonar:sonar -Dsonar.projectBaseDir=" + localRepository
				+ " -Dsonar.login=110135c194c457155225f2b7ea4882e066bbe7a7" ) ) ;
		invoker.setOutputHandler( new InvocationOutputHandler() {

			@Override
			public void consumeLine(String line) {
				System.out.println( line ) ;
			}
			
		}) ;
		
		try
		{
			InvocationResult result = invoker.execute( request );
		}
		catch (MavenInvocationException e)
		{
		  e.printStackTrace();
		}
		SonarClient client = SonarClient.builder()
				.login( "admin" )
				.password( "jiantsquid" ).url( "http://localhost:9000" ).build() ;
		String response = client.get( "/api/ce/task", "id", "AX7ZUB6cXzxPHGoV7CDm" ) ;
		System.out.println( response ) ;
		
		response = client.get( "/api/project_analyses/search", "project", "com.jiantsquid.poc:com.jiantsquid.core" ) ;
		System.out.println( response ) ;
		
		response = client.get( "api/qualityprofiles/search", "project", "com.jiantsquid.poc:com.jiantsquid.core", "language", "java" ) ;
		System.out.println( response ) ;
		
//		response = client.get( "api/measures/component", "project", "com.jiantsquid.core:com.jiantsquid.core" ) ;
//		System.out.println( response ) ;
		
		
		response = client.get( "api/issues/search", "project", "com.jiantsquid.poc:com.jiantsquid.core" ) ;
		System.out.print( response ) ;
		System.out.flush() ;
		JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET ) ;
		jsonFactory.disable( JsonParser.Feature.AUTO_CLOSE_SOURCE ) ;
		jsonFactory.disable( JsonGenerator.Feature.IGNORE_UNKNOWN ) ;
		
		ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		objectMapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false ) ;
		
		
		Issues issues = objectMapper.readValue( response, Issues.class ) ;
		
		int toto = 0 ;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException, XmlPullParserException {
		new TestJenkins().run() ;
	}

}
