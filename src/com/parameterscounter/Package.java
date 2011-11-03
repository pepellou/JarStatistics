package com.parameterscounter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Package {

	private String jar;

	public Package(String jarName) {
		this.jar = jarName;
	}

	// Code adapted from:http://www.rgagnon.com/javadetails/java-0513.html
	public List getClasses() {
		ArrayList classes = new ArrayList();

		try {
			JarInputStream jarFile = new JarInputStream(
					new FileInputStream(jar));
			JarEntry jarEntry;

			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if (jarEntry.getName().endsWith(".class")) {
					classes.add(jarEntry.getName().replaceAll("/", "\\."));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

}
