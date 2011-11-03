package com.parameterscounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Package {

	private String jar;
	private ArrayList<String> classes;
	private JarInputStream jarInputStream;

	public Package(String jarName) {
		this.jar = jarName;
		this.classes = null;
		this.jarInputStream = null;
	}

	public List getClasses() throws FileNotFoundException, IOException {
		if (classes == null) {
			classes = new ArrayList<String>();
			for (String entry : getJarEntries()) {
				if (entry.endsWith(".class")) {
					classes.add(entry.replaceAll("/", "\\."));
				}
			}
		}
		return classes;
	}

	private ArrayList<String> getJarEntries() throws IOException,
			FileNotFoundException {
		ArrayList<String> entries = new ArrayList<String>();
		JarEntry jarEntry;
		while ((jarEntry = getJarInputStream().getNextJarEntry()) != null) {
			entries.add(jarEntry.getName());
		}
		return entries;
	}

	private JarInputStream getJarInputStream() throws IOException,
			FileNotFoundException {
		if (jarInputStream == null) {
			jarInputStream = new JarInputStream(new FileInputStream(jar));
		}
		return jarInputStream;
	}

	public Map<String, Method[]> getMethods() throws ClassNotFoundException,
			IOException, SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Map<String, Method[]> map = new HashMap<String, Method[]>();
		for (String className : classes) {
			map.put(className, getMethodsOfClass(className));
		}
		return map;
	}

	private Method[] getMethodsOfClass(String className)
			throws MalformedURLException, IOException, ClassNotFoundException,
			SecurityException, IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		String filePath = jar;
		addFileToClassPath(filePath);
		filePath = "jar:file://" + filePath + "!/";
		URL url = new File(filePath).toURL();
		URLClassLoader classLoader = new URLClassLoader(new URL[] { url });
		Class theClass = classLoader.loadClass(className);
		return theClass.getMethods();
	}

	private void addFileToClassPath(String filePath)
			throws MalformedURLException, IOException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		addURLToClassPath(new File(filePath).toURL());
	}

	public void addURLToClassPath(URL url) throws IOException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		URLClassLoader sysLoader = (URLClassLoader) this.getClass()
				.getClassLoader();
		if (alreadyInClassPath(url, sysLoader))
			return;
		Class<?> sysclass = URLClassLoader.class;
		Method method = sysclass.getDeclaredMethod("addURL",
				new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(sysLoader, new Object[] { url });
	}

	private boolean alreadyInClassPath(URL url, URLClassLoader sysLoader) {
		URL urlsInClassPath[] = sysLoader.getURLs();
		for (int i = 0; i < urlsInClassPath.length; i++) {
			if (urlsAreEquals(url, urlsInClassPath[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean urlsAreEquals(URL url1, URL url2) {
		return normalizeUrl(url1).equals(normalizeUrl(url2));
	}

	private String normalizeUrl(URL url1) {
		return url1.toString().toLowerCase();
	}

}
