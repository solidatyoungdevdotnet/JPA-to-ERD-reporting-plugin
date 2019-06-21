/**
 * 
 */
package net.youngdev.maven.plugins.reporting;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * @author myoung
 *
 */
public class ReflexiveAnnotationUtils {
	/**
	 * 
	 * @param clazz
	 * @param annoClass
	 * @param propertyName
	 * @return
	 */
	public static Object getAnnotationPropertyForClass(Class<?> clazz, String annotationName, String propertyName) {
		Object o = null;
		try {
			for (Annotation a : clazz.getAnnotations()) {
				if (a.annotationType().getName().endsWith(annotationName)) {
					Annotation ann = a;

					if (ann != null) {
						o = ann.annotationType().getMethod(propertyName).invoke(ann);
					}
				}
			}

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException beanEx) {
			throw new IllegalArgumentException(
					clazz.getCanonicalName() + " property " + propertyName + " does not exist.", beanEx);

		}
		return o;
	}
	
	/**
	 * 
	 * @param clazz
	 * @param annoClass
	 * @param propertyName
	 * @return
	 */
	public static Annotation getAnnotationForBeanProperty(Class<?> clazz, 
			String beanProperty, String fqAnnoationClass) {
		Annotation o = null;
		try {
			for (Annotation a: clazz.getDeclaredField(beanProperty).getAnnotations()) {
				if (a.annotationType().getCanonicalName().equals(fqAnnoationClass)) {
					o = a;
					break;
				}
			}
		} catch (NoSuchFieldException | SecurityException  beanEx ) {
			throw new IllegalArgumentException(" property "+ beanProperty +" with annotation "
					+fqAnnoationClass +" does not exist for class "+clazz.getCanonicalName(), 
					beanEx);
			
		}
		return o;
	}

}
