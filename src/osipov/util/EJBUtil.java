package osipov.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.Util;

public class EJBUtil {

	private static final String WAS_LOCAL_PREFIX = "local:ejb";
	private static final String LIBRARY_APP_NAMING_PROVIDER = "corbaloc:iiop:localhost:2809";
	private static final String WEBSPHERE_FACTORY = "com.ibm.websphere.naming.WsnInitialContextFactory";
	
	private static final Map ctxMap = new HashMap();
	
	public static InitialContext getInitialContext(String factory, String provider) throws NamingException {
		if (!ctxMap.containsKey(factory + "/" + provider)) {
			 Hashtable env = new Hashtable();
			 env.put(Context.INITIAL_CONTEXT_FACTORY, WEBSPHERE_FACTORY);
			 env.put(Context.PROVIDER_URL, LIBRARY_APP_NAMING_PROVIDER);
			 ctxMap.put(factory + "/" + provider, new InitialContext(env));
			}
		InitialContext ctx = (InitialContext)ctxMap.get(factory + "/" + provider);
		return ctx;
	}
	
	/**
	 * Initialize the target home
	 * @return null if JNDI does not contain a specified reference or if the 
	 * if the specified class could not be loaded using Class.forName
	 **/
	public static EJBHome getEJBHome(Map cache, String clazz, String jndi) throws ClassNotFoundException, NamingException {
		Class ejbHome = Class.forName(clazz);
		EJBHome home = (EJBHome)cache.get(jndi);
		if (home == null) {
			home = lookupEJBHome(jndi, ejbHome);
			if (home != null) 
				cache.put(jndi, home);
		}
		return home;
	}
	
	/**
	 * Initialize the target home
	 **/
	public static EJBHome lookupEJBHome(String jndi, Class clazz) throws NamingException {
		InitialContext ctx = getInitialContext(WEBSPHERE_FACTORY, LIBRARY_APP_NAMING_PROVIDER);
		return (ctx == null) ? null : (EJBHome)PortableRemoteObject.narrow(ctx.lookup(jndi), clazz);
	}
	
	/**
	 * Initialize the target home
	 * @return null if JNDI does not contain a specified reference or if the 
	 * if the specified class could not be loaded using Class.forName
	 **/
	public static EJBLocalHome getEJBLocalHome(Map cache, String clazz, String jndi) throws NamingException, ClassNotFoundException {
		Class ejbHome = Class.forName(clazz);
		EJBLocalHome home = (EJBLocalHome)cache.get(jndi);
		if (home == null) {
			home = lookupEJBLocalHome(jndi, ejbHome);
			if (home != null) {
				cache.put(jndi, home);
			}
		}
		return home;
	}
	/**
	 * Initialize the local home
	 **/
	public static EJBLocalHome lookupEJBLocalHome(String jndi, Class clazz) throws NamingException {
		InitialContext ctx = getInitialContext(WEBSPHERE_FACTORY, LIBRARY_APP_NAMING_PROVIDER);
		return (ctx == null) ? null : (EJBLocalHome)PortableRemoteObject.narrow(ctx.lookup(jndi), clazz);
	}
	
	public static String convertToClass(String jndi) {
		if (jndi == null || jndi.length() == 0) return jndi;
		jndi = (jndi.startsWith(WAS_LOCAL_PREFIX) ? jndi.substring(WAS_LOCAL_PREFIX.length()) : jndi);
		jndi = jndi.substring("/ejb/".length());
		jndi = jndi.replace('/', '.');
		return jndi;
	}
	
	public static String convertToJNDI(String clazz) {
		if (clazz == null || clazz.length() == 0) return clazz;
		boolean local = clazz.endsWith("LocalHome");
		String ejb = (local) ? clazz.substring(0, clazz.lastIndexOf("LocalHome")) : clazz.substring(0, clazz.lastIndexOf("Home"));
		ejb = ejb.substring(ejb.lastIndexOf('.') > -1 ? ejb.lastIndexOf('.') + 1 : 0);
		return ((local) ? WAS_LOCAL_PREFIX + "/" : "") + "ejb/" + ejb; 
	}
	
	public static Object createEJB(Map cache, String clazz, Object[] args, Object def) {
		try {
			return createEJB(cache, clazz, Util.getClass(args), args);
		} catch (IllegalArgumentException e) {
			return def;
		} catch (SecurityException e) {
			return def;
		} catch (NamingException e) {
			return def;
		} catch (ClassNotFoundException e) {
			return def;
		} catch (IllegalAccessException e) {
			return def;
		} catch (InvocationTargetException e) {
			return def;
		} catch (NoSuchMethodException e) {
			return def;
		}
	}
	
	public static Object createEJB(Map cache, String clazz, Class[] types, Object[] args, Object def) {
		try {
			return createEJB(cache, clazz, types, args);
		} catch (IllegalArgumentException e) {
			return def;
		} catch (SecurityException e) {
			return def;
		} catch (NamingException e) {
			return def;
		} catch (ClassNotFoundException e) {
			return def;
		} catch (IllegalAccessException e) {
			return def;
		} catch (InvocationTargetException e) {
			return def;
		} catch (NoSuchMethodException e) {
			return def;
		}
	}
	public static Object createEJB(Map cache, String clazz, Class[] types, Object[] args) throws NamingException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		Object objHome = jndi.startsWith(WAS_LOCAL_PREFIX) ? (Object)getEJBLocalHome(cache, jndi) : (Object)getEJBHome(cache, jndi);
		Object objHome = clazz.endsWith("LocalHome") ? (Object)getEJBLocalHome(cache, clazz, convertToJNDI(clazz)) : (Object)getEJBHome(cache, clazz, convertToJNDI(clazz));
		objHome = (objHome == null) ? null : objHome.getClass().getMethod("create", types).invoke(objHome, args);
		return objHome;
	}
}
