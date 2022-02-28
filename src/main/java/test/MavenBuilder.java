package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MavenBuilder {

	private static final String settings = "<settings>"
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
			+ "                <sonar.host.url>"
			+ "                  http://localhost:9000"
			+ "                </sonar.host.url>"
			+ "            </properties>"
			+ "        </profile>"
			+ "     </profiles>"
			+ "</settings>" ;
	
	public void buid( File localRepository, File pomFile ) throws IOException {
		File setting = new File( localRepository + File.separator + "settings.xml" ) ;
		RandomAccessFile sets = new RandomAccessFile( setting, "rw" ) ;
		sets.writeUTF( settings ) ;
		sets.close();
		
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( pomFile ) ;
		request.setGoals( Collections.singletonList( "compile" ) );
		request.setLocalRepositoryDirectory( localRepository ) ;
		request.setRecursive( true ) ;
		invokeMaven( request ) ;
	}
	
	public void analyze( File localRepository ) {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setGoals( Collections.singletonList( "verify sonar:sonar -Dsonar.projectBaseDir=" + localRepository
				+ " -Dsonar.login=110135c194c457155225f2b7ea4882e066bbe7a7" ) ) ;
		
		invokeMaven( request );
	}
	public void invokeMaven( InvocationRequest request ) {
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
    }
}
