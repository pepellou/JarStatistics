package com.parameterscounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	public Package(String jarName) {
		this.jar = jarName;
		this.classes = null;
	}

	// Code adapted from:http://www.rgagnon.com/javadetails/java-0513.html
	public List getClasses() {
		if (classes == null) {
			classes = new ArrayList<String>();
			try {
				JarInputStream jarFile = new JarInputStream(
						new FileInputStream(jar));
				JarEntry jarEntry = jarFile.getNextJarEntry();
				while (jarEntry != null) {
					if (jarEntry.getName().endsWith(".class")) {
						classes.add(jarEntry.getName().replaceAll("/", "\\."));
					}
					jarEntry = jarFile.getNextJarEntry();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return classes;
	}

	public Map<String, Method[]> getMethods() throws ClassNotFoundException,
			IOException {
		Map<String, Method[]> map = new HashMap<String, Method[]>();
		for (String className : classes) {

			URLClassLoader clazzLoader;
			Class clazz;
			String filePath = "junit.jar";
			addFile(filePath);
			filePath = "jar:file://" + filePath + "!/";
			URL url = new File(filePath).toURL();
			clazzLoader = new URLClassLoader(new URL[] { url });
			clazz = clazzLoader.loadClass(className);

			map.put(className, clazz.getMethods());
		}
		return map;
	}

	private void addFile(String filePath) throws MalformedURLException,
			IOException {
		addURL(new File(filePath).toURL());
	}

	public void addURL(URL u) throws IOException {
		// URLClassLoader sysLoader = (URLClassLoader)
		// ClassLoader.getSystemClassLoader();
		URLClassLoader sysLoader = (URLClassLoader) this.getClass()
				.getClassLoader();
		URL urls[] = sysLoader.getURLs();
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].toString().toLowerCase()
					.equals(u.toString().toLowerCase())) {
				System.err.println("URL " + u + " is already in the CLASSPATH");
				return;
			}
		}
		Class<?> sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL",
					new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException(
					"Error, could not add URL to system classloader");
		}
	}

}
