package test.data;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

class GitSCM extends SourceCodeManagementSystem {

	GitSCM( String uri, String localRepository ) {
		super( uri, localRepository ) ;
	}

	@Override
	public boolean checkout() {
		try {
			Git.cloneRepository().setDirectory( new File(  getAttribute( LOCAL_REPOSITORY ) ) )
			.setURI( getAttribute( URI ) ).call();
		} catch (GitAPIException e) {
			System.err.println( "-->GIT CANNOT CHECKOUT: " + getAttribute( URI ) + " " + e.getMessage()  ) ; 
			return false ;
		}
		return true ;
	} 

}
