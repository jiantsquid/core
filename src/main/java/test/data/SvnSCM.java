package test.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

public class SvnSCM extends SourceCodeManagementSystem {

	SvnSCM( String uri, String localRepository ) {
		super( uri, localRepository ) ;
	}
	
	@Override
	public boolean checkout() {
		try {
			Files.createDirectories( Path.of(getAttribute( LOCAL_REPOSITORY ) ) ) ;
			
			String devConnection = getAttribute( URI ) ;
			String uri = devConnection.substring( devConnection.indexOf( "http") ) ;
					
			SVNURL source = SVNURL.parseURIEncoded( uri ) ;
			SvnTarget sourceTarget =  SvnTarget.fromURL( source ) ;
			SvnTarget target = SvnTarget.fromFile( new File( getAttribute( LOCAL_REPOSITORY ) ) ) ;
			SvnCheckout checkout = null ;
			try {
				SvnOperationFactory svnOperationFactory = new SvnOperationFactory() ;
				checkout = svnOperationFactory.createCheckout();
			} catch (Exception e) {
				System.err.println( "-->SVN1 CANNOT CHECKOUT: " + getAttribute( URI ) + " " + e.getMessage() ) ; 
				return false ;
			}
			checkout.setSource( sourceTarget );
			checkout.setSingleTarget( target ) ;
			checkout.run();
			
		} catch (SVNException e) {
			System.err.println( "-->SVN2 CANNOT CHECKOUT: " + getAttribute( URI ) + " " + e.getMessage()  ) ;
			return false ;
		} catch (IOException e) {
			System.err.println( "-->SVN3 CANNOT CHECKOUT: " + getAttribute( URI ) + " " + e.getMessage() ) ; 
			return false ;
		}
		return true;
	}

}
