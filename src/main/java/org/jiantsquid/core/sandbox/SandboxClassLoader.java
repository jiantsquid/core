package org.jiantsquid.core.sandbox;


/**
 * 
 * Loads classes from the Applet. Gets the bytecode from a IAppletBytecodeSource.
 * 
 * It must have a nl.blackmantha.sandbox.CentralSandboxClassLoader as its parent.
 * 
 * While there should only be a single CentralSandboxClassLoader, every Applet should have
 * a SandboxClassLoader for it. That way, when all Objects of that Applet and this classloader 
 * become GC-able, the classes themselves can be unloaded.
 * 
 * @author github.com/Black-Mantha
 *
 */
public class SandboxClassLoader extends ClassLoader {
	
	private final Compile compile;

	public SandboxClassLoader(ClassLoader parent, Compile compile) {
		super(parent);
		if (!parent.getClass().getName().equals(CentralSandboxClassLoader.class.getCanonicalName())) {
			throw new RuntimeException("SandboxClassLoader must have a CentralSandboxClassLoader as parent");
		}
		this.compile = compile;
	}

	/**
	 * Make a class in the sandbox, after checking for forbidden code constructs
	 */
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] bytecode = compile.getByteCode(name);
		if (bytecode!=null) {
			return defineClass(name, bytecode, 0, bytecode.length);
		}
		throw new ClassNotFoundException(name);
	}
}
