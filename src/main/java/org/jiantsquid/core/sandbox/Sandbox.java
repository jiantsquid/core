package org.jiantsquid.core.sandbox;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;

import org.jiantsquid.core.smartcontractgui.SmartContractGUI;

public class Sandbox  {
	// Members to specify applet and callbacks
	protected String classname;
	
	private Class<?> appClass ;
	private Compile compile ;
	
	public Sandbox( String classname, String code)  {
		SwitchableSecurityManager.getInstance().setCheck( true ) ;
		this.classname = classname;
		compile = new Compile() ;
       
		compile.compile( classname, code ) ;
		appClass = compile.getClass(classname) ;
		SwitchableSecurityManager.getInstance().setCheck( false ) ;
	}
	
	public Class<?> getAppClass() {
		return appClass ;
	}
	
	@SuppressWarnings("unchecked")
    public SmartContractGUI<JComponent> getContract() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	    return ((SmartContractGUI<JComponent>) getAppClass().getDeclaredConstructor().newInstance()) ;
	}
}
