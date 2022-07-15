package org.jiantsquid.core.sandbox;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.StandardJavaFileManager;


/**
 * A utility that simplifies in-memory compilation of new classes.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("restriction")
public class Compile {

	private ClassFileManager fileManager ;
	
	public Class<?> getClass( String className3 ) {
		
		ByteArrayClassLoader c = new ByteArrayClassLoader(fileManager.classes());
        return fileManager.loadAndReturnMainClass(className3,
            (name, bytes) -> {
				try {
					return c.loadClass(name);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null ;
			});
	}
	
	byte[] getByteCode( String className ) {
		return fileManager.classes.get( className ) ;
	}
	
    public Compile compile(String className2, String content ) {
        
    	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    	fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));
    	
    	List<CharSequenceJavaFileObject> files = new ArrayList<>();
        files.add(new CharSequenceJavaFileObject(className2, content));
        StringWriter out = new StringWriter();

        List<String> options = new ArrayList<>();
        CompilationTask task = compiler.getTask(out, fileManager, null, options, null, files);

        task.call();
        
        return this ;
    }

    final class ByteArrayClassLoader extends ClassLoader {
        private final Map<String, byte[]> classes;

        ByteArrayClassLoader(Map<String, byte[]> classes) {
            super(ByteArrayClassLoader.class.getClassLoader());

            this.classes = classes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classes.get(name);

            if (bytes == null)
                return super.findClass(name);
            else
                return defineClass(name, bytes, 0, bytes.length);
        }
    }

    final class JavaFileObject extends SimpleJavaFileObject {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        JavaFileObject(String name, JavaFileObject.Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }

        byte[] getBytes() {
            return os.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() {
            return os;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    final class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final Map<String, JavaFileObject> fileObjectMap;
        private Map<String, byte[]> classes;

        ClassFileManager(StandardJavaFileManager standardManager) {
            super(standardManager);

            fileObjectMap = new HashMap<>();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location,
            String className,
            JavaFileObject.Kind kind,
            FileObject sibling
        ) {
            JavaFileObject result = new JavaFileObject(className, kind);
            fileObjectMap.put(className, result);
            return result;
        }

        boolean isEmpty() {
            return fileObjectMap.isEmpty();
        }

        Map<String, byte[]> classes() {
            if (classes == null) {
                classes = new HashMap<>();

                for (Entry<String, JavaFileObject> entry : fileObjectMap.entrySet())
                    classes.put(entry.getKey(), entry.getValue().getBytes());
            }

            return classes;
        }

        Class<?> loadAndReturnMainClass(String mainClassName, ThrowingBiFunction<String, byte[], Class<?>> definer) {
            Class<?> result = null;

            for (Entry<String, byte[]> entry : classes().entrySet()) {
                Class<?> c = definer.apply(entry.getKey(), entry.getValue());
                if (mainClassName.equals(entry.getKey()))
                    result = c;
            }

            return result;
        }
    }

    @FunctionalInterface
    interface ThrowingBiFunction<T, U, R> {
        R apply(T t, U u) ;
    }

    final class CharSequenceJavaFileObject extends SimpleJavaFileObject {
        final CharSequence content;

        public CharSequenceJavaFileObject(String className, CharSequence content) {
            super(URI.create("string:///" + ( className.contains( "." ) ? className.replace('.', '/') : className )  + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }
}


