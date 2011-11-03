package com.jarstats;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JarTest {

	private static Jar aKnownPackage;
	private static int knownJarNumOfClasses;
	private static String aKnownClass;
	private static int knownJarNumOfMethods;
	private static String aKnownMethod;

	@BeforeClass
	public static void setUp() {
		aKnownPackage = new Jar("junit.jar");
		knownJarNumOfClasses = 209;
		aKnownClass = "junit.textui.TestRunner.class";
		knownJarNumOfMethods = 96;
		aKnownMethod = "runAndWait";
	}

	@Test
	public void can_get_classes() throws Exception {
		List classes = aKnownPackage.getClasses();

		assertEquals(knownJarNumOfClasses, classes.size());
		assertTrue(classes.contains(aKnownClass));
	}

	@Test
	public void can_get_all_methods() throws Exception {
		Map<String, Method[]> methods = aKnownPackage.getMethods();

		assertEquals(knownJarNumOfMethods, methods.size());
		assertTrue(Arrays.asList(methods.get(aKnownClass)).contains(
				aKnownMethod));
	}

}
