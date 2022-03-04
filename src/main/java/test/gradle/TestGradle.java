package test.gradle;

import java.util.List;

import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.dependency.Dependency;
import org.owasp.dependencycheck.dependency.EvidenceType;
import org.owasp.dependencycheck.utils.Settings;

public class TestGradle {

	public static void main(String[] args) {
		Settings settings = new Settings() ;
		Engine engine = new Engine( settings ) ;
		
		//List<Dependency> dependencies = engine.scan( "C:\\Users\\Greg\\Documents\\development\\gitrepo\\dependencies\\org.codehaus.sonar\\sonar-ws-client\\jar\\sonar-core" ) ;
		List<Dependency> dependencies = engine.scan( "C:\\Users\\Greg\\Documents\\development\\gitrepo" ) ;
		
		for( Dependency dep : dependencies ) {
			System.out.println( dep.toString() + " " + dep.getDescription() + dep.getName() + dep.getVersion() + "#" + dep.getVulnerableSoftwareIdentifiersCount()) ;
			dep.getEvidence( EvidenceType.PRODUCT ).forEach( (e) -> System.out.println( "   " + e.getName()  + " " +e.getSource()  + " " + e.getValue() ) ) ;
		}
		engine.close();
	}

}
