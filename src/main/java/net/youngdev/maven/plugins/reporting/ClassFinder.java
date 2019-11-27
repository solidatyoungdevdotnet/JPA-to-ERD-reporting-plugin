package net.youngdev.maven.plugins.reporting;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;

/**
 * This utility is shamelessly stolen from 
 * http://stackoverflow.com/questions/15519626/how-to-get-all-classes-names-in-a-package.
 * 
 * Do not add this class to non-test classpath without Extreme testing and code review
 * 
 * @author Matt Young
 * @version 
 */
@Deprecated // This impl has all kinds of issues cross platform.  pulling in Guava reflect to replace it
public class ClassFinder {
//	private static final Logger log = LoggerFactory.getLogger(ClassFinder.class);

    private static final char DOT = '.';

    private static final char SLASH = '/';

    private static final String CLASS_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    /**
     * list classes under package 
     * @param scannedPackage
     * @param classLoader 
     * @param sourceRoots 
     * @return
     */
    public static List<Class<?>> find(Log logger, String scannedPackage, String sourceRoot, ClassLoader classLoader) {
        String scannedPath = scannedPackage.replace(DOT, SLASH);

    	List<Class<?>> classes = new ArrayList<Class<?>>();
    	try {
    		List<File> files = Arrays.asList(new File(sourceRoot+File.separator+scannedPath).listFiles());
    		ArrayList<URL> scannedUrls =  new ArrayList<>();
	        for (File f: files) {
	        	if (f.isFile()) {
	        		scannedUrls.add(f.toURL());
	        	}
	        }
	        
	        if (scannedUrls == null || scannedUrls.size() == 0) {
	            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
	        }

        	//URLClassLoader loader = new URLClassLoader(scannedUrls.toArray(new URL[] {}));
	        for (URL scannedUrl : scannedUrls) {
	        	logger.error("found package url: {} for url {}"
	        			+scannedUrl.getFile() +" "+ scannedUrl);
	        	String fqcn = StringUtils.replace(StringUtils.remove(
	        			StringUtils.remove(scannedUrl.getPath(), 
	        					sourceRoot+File.separator ), CLASS_SUFFIX),File.separator,""+DOT);
    			logger.info("Identified "+fqcn+" for classloading");
    			Class<?> classToLoad = classLoader.loadClass(fqcn);
    			if (classToLoad != null ) {
    				classes.add(classToLoad);
    			} else {
    				logger.error("Could not load "+fqcn);
    			}

	        }
    	} catch (Exception e) {
    		logger.error("Exception occurred trying to iterate over multiple classpath urls" , e);
    	}
        return classes;
    }

    /**
     * if file is directory, list contents and call this method againw with children.
     * if file is java class, add it to the list
     * @param file
     * @param scannedPackage
     * @return
     */
    public static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + DOT + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_SUFFIX)) {
            int endIndex = resource.length() - CLASS_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
    
 

}