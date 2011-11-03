package com.parameterscounter;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PackageTest {

	private static Package aKnownPackage;
	private static int knownJarNumOfClasses;
	private static String aKnownClass;

	@BeforeClass
	public static void setUp() {
		aKnownPackage = new Package("junit-4.1.jar");
		knownJarNumOfClasses = 96;
		aKnownClass = "junit.textui.TestRunner.class";
	}

	@Test
	public void can_get_classes() throws Exception {
		List classes = aKnownPackage.getClasses();

		assertEquals(knownJarNumOfClasses, classes.size());
		assertTrue(classes.contains(aKnownClass));
	}

}
