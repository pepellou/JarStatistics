import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.junit.Test;

public class ParametersCounterTest {

	@Test
	public void can_get_classes() throws Exception {
		String knownJarName = "junit-4.1.jar";
		int knownJarNumOfClasses = 96;
		String aKnownClass = "junit.textui.TestRunner.class";

		List classes = new ParametersCounter().getClasseNamesInPackage(
				knownJarName, "");

		assertEquals(knownJarNumOfClasses, classes.size());
		assertTrue(classes.contains(aKnownClass));
	}
}
