import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Varun Chandresekar
 *
 */
public class DynamicLibraryLoader {
	
	private static final Logger logger = Logger.getLogger(DynamicLibraryLoader.class
			.getName());

	public DynamicLibraryLoader() {
		super();
	}

	public static URLClassLoader LOADER = (URLClassLoader)ClassLoader.getSystemClassLoader();
	
	/**Constructor load the library to systemClassLoader
	 * @param libraryPath
	 * @throws Exception
	 * @Usage new DynamicLibraryLoader(libraryPath);
	 */
	public static void loadLibrary(String libraryPath) throws Exception {
		boolean libLoaded = false;
		try {
	        File jar = new File(libraryPath);
			URL url = jar.toURI().toURL();
	        for (URL it : java.util.Arrays.asList(LOADER.getURLs())){
	            if (it.equals(url)){
	            	libLoaded = true;
	            }                
	        }   
	        /*Disallow if already loaded*/
	        if(libLoaded==false){
		        Method method = URLClassLoader.class.getDeclaredMethod(
		        		"addURL", new Class[]{URL.class}  );
		        method.setAccessible(true);
		        method.invoke(LOADER, new Object[]{url});
	        }
		} catch (MalformedURLException murle) {
			logger.log(Level.SEVERE, "ERROR in loadLibrary(String libraryPath): ", murle);
			throw new Exception("_MalformedURLException_|Message|"+murle.getMessage());
		} catch (SecurityException se) {
			logger.log(Level.SEVERE, "ERROR in loadLibrary(String libraryPath): ", se);
			throw new Exception("_SecurityException_|Message|"+se.getMessage());
		} catch (NoSuchMethodException nsme) {
			logger.log(Level.SEVERE, "ERROR in loadLibrary(String libraryPath): ", nsme);
			throw new Exception("_NoSuchMethodException_|Message|"+nsme.getMessage());
		} catch (IllegalArgumentException iarge) {
			logger.log(Level.SEVERE, "ERROR in loadLibrary(String libraryPath): ", iarge);
			throw new Exception("_IllegalArgumentException_|Message|"+iarge.getMessage());
		} catch (IllegalAccessException iacce) {
			logger.log(Level.SEVERE, "ERROR in loadLibrary(String libraryPath): ", iacce);
			throw new Exception("_IllegalAccessException_|Message|"+iacce.getMessage());
		} catch (InvocationTargetException invtgte) {
			logger.log(Level.SEVERE, "ERROR in loadLibrary(String libraryPath): ", invtgte);
			throw new Exception("_InvocationTargetException_|Message|"+invtgte.getMessage());
		}
	}
	
	/**ClassLoader
	 * @param classpath
	 * @return
	 * @throws Exception
	 * @Usage T instance = new DynamicLibraryLoader(libraryPath).loadClass(classpath);
	 */
	public static Object loadClass(String classpath) throws Exception{
		Object instance = null;
		try {
			Constructor<?> cs = LOADER.loadClass(classpath).getConstructor();
			instance = cs.newInstance();
		} catch (SecurityException se) {
			logger.log(Level.SEVERE, "ERROR in loadClass(String classpath): ", se);
			throw new Exception("_SecurityException_|Message|"+se.getMessage());
		} catch (NoSuchMethodException nsme) {
			logger.log(Level.SEVERE, "ERROR in loadClass(String classpath): ", nsme);
			throw new Exception("_NoSuchMethodException_|Message|"+nsme.getMessage());
		} catch (ClassNotFoundException cnfe) {
			logger.log(Level.SEVERE, "ERROR in loadClass(String classpath): ", cnfe);
			throw new Exception("_ClassNotFoundException_|"+classpath+"|Message|"+cnfe.getMessage());
		} catch (IllegalArgumentException iarge) {
			logger.log(Level.SEVERE, "ERROR in loadClass(String classpath): ", iarge);
			throw new Exception("_IllegalArgumentException_|Message|"+iarge.getMessage());
		} catch (InstantiationException iste) {
			logger.log(Level.SEVERE, "ERROR in loadClass(String classpath): ", iste);
			throw new Exception("_InstantiationException_|Message|"+iste.getMessage());
		} catch (IllegalAccessException iacce) {
			logger.log(Level.SEVERE, "ERROR in loadClass(String classpath): ", iacce);
			throw new Exception("_IllegalAccessException_|Message|"+iacce.getMessage());
		} catch (InvocationTargetException invtgte) {
			logger.log(Level.SEVERE, "ERROR in loadClass(String classpath): ", invtgte);
			throw new Exception("_InvocationTargetException_|Message|"+invtgte.getMessage());
		}
        return instance;
	}
	
	/**Method Invoker
	 * @param <T>
	 * @param classpath
	 * @param methodName
	 * @param methodArgsClass
	 * @param methodArgs
	 * @return
	 * @throws Exception
	 * @Usage T obj = new DynamicLibraryLoader(libraryPath).invokeMethod(classpath, methodName, methodArgsClass, methodArgs);
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(String classpath,String methodName,Class<?>[] methodArgsClass,Object[] methodArgs) throws Exception{
		T retVal = null;
		try {
			Method testMethod = loadClass(classpath).getClass().getMethod(methodName, methodArgsClass);
			retVal = (T) testMethod.invoke(LOADER, methodArgs);
		} catch (SecurityException se) {
			logger.log(Level.SEVERE, "ERROR in invokeMethod(String classpath,String methodName,Class<?>[] methodArgsClass,Object[] methodArgs): ", se);
			throw new Exception("_SecurityException_|Message|"+se.getMessage());
		} catch (NoSuchMethodException nsme) {
			logger.log(Level.SEVERE, "ERROR in invokeMethod(String classpath,String methodName,Class<?>[] methodArgsClass,Object[] methodArgs): ", nsme);
			throw new Exception("_NoSuchMethodException_|"+methodName+"|Message|"+nsme.getMessage());
		} catch (IllegalArgumentException iarge) {
			logger.log(Level.SEVERE, "ERROR in invokeMethod(String classpath,String methodName,Class<?>[] methodArgsClass,Object[] methodArgs): ", iarge);
			throw new Exception("_IllegalArgumentException_|Message|"+iarge.getMessage());
		} catch (IllegalAccessException iacce) {
			logger.log(Level.SEVERE, "ERROR in invokeMethod(String classpath,String methodName,Class<?>[] methodArgsClass,Object[] methodArgs): ", iacce);
			throw new Exception("_IllegalAccessException_|Message|"+iacce.getMessage());
		} catch (InvocationTargetException invtgte) {
			logger.log(Level.SEVERE, "ERROR in invokeMethod(String classpath,String methodName,Class<?>[] methodArgsClass,Object[] methodArgs): ", invtgte);
			throw new Exception("_InvocationTargetException_|Message|"+invtgte.getMessage());
		}
		return retVal;
	}
	
	/**Method Invoker
	 * @param libraryPath
	 * @param classpath
	 * @param methodName
	 * @param methodArgsClass
	 * @param methodArgs
	 * @return
	 * @throws Exception
	 * @Usage T obj = DynamicLibraryLoader.invokeMethod(libraryPath, classpath, methodName, methodArgsClass, methodArgs);
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(String libraryPath,String classpath,String methodName,Class<?>[] methodArgsClass,Object[] methodArgs) throws Exception{
		loadLibrary(libraryPath);
		return (T) invokeMethod(classpath, methodName, methodArgsClass, methodArgs);
	}
}
