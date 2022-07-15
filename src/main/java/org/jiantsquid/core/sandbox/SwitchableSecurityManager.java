package org.jiantsquid.core.sandbox;

import java.lang.ref.WeakReference;
import java.security.Permission;

public class SwitchableSecurityManager extends SecurityManager {

	private static WeakReference<SwitchableSecurityManager> instance = new WeakReference<SwitchableSecurityManager>(null);

	private SwitchableSecurityManager() {}
	
	public synchronized static SwitchableSecurityManager getInstance() {
		SwitchableSecurityManager result;
		if (instance==null || (result= instance.get())==null) {
			result = new SwitchableSecurityManager();
			instance = new WeakReference<>(result);
			// Pre-load the Sandbox class before this SecurityManager is activated 
			try  {
				Class.forName( Sandbox.class.getCanonicalName());
			} catch (ClassNotFoundException e)  {
				throw new RuntimeException(e);
			}
		}
		return result;
	}
	
    private void checkPermissionImpl( Permission perm) {
       
        StackTraceElement[] stack  =Thread.currentThread().getStackTrace() ;
        for( StackTraceElement elem : stack ) {
            
            
            if( elem.getClassName().equals( "com.jiantsquid.system.common.sandbox.Compile" ) 
                || ( elem.getClassName().equals( "com.jiantsquid.system.common.sandbox.Sandbox" ) && elem.getMethodName().equals( "<init>" ) ) ) {
                return ;
            }
            
        }
        
        System.out.println( "************Perm " + perm.getName() + " " + perm.getActions() + " " + perm.getClass().getCanonicalName() ) ; 
        for( StackTraceElement elem : stack ) {
            System.out.print( elem.getClassName() + " " + elem.getMethodName() + "***");
            System.out.println() ;
        }
       
        throw new SecurityException("Permission "+perm+" not granted to smart GUI");
    }
	
	public void setCheck( boolean check ) {
	    isRestricted.set( check ) ;
	}
	
	/**
	 *  This static member is used to check if threads should be restricted. initialValue() supplies the default, but it can
	 *  be changed by classes which have access to this member. Note that both reflection and simply defining a class in this
	 *  package allow access, so it's not secure by itself. Suggested usage:
	 *  
	 *    Boolean wasRestricted = SwitchableSecurityManager.isRestricted.get();
	 *    SwitchableSecurityManager.isRestricted.set(Boolean.FALSE);
	 *    try {
	 *      < privileged code >
	 *    } finally {
	 *      SwitchableSecurityManager.isRestricted.set(wasRestricted);
	 *    }
	 */
	public static final ThreadLocal<Boolean> isRestricted = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
		    return false ;
		}
		
		public Boolean get() {
		    return super.get() ;
		}
	};
	
	private final Object reentryCheck = new Object();
	@Override
	public void checkPermission(Permission perm) {
		// reentryCheck is needed because otherwise, isRestricted.get() could get into an infinite loop 
		// via ThreadLocal.initialValue() and this method
		if (Thread.holdsLock(reentryCheck))  return;
		Boolean bool;
		synchronized (reentryCheck) { 
			bool = isRestricted.get();
		}
		if (bool.booleanValue()) {
		    checkPermissionImpl(perm);
		}
	}
}
