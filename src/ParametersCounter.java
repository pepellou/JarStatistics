import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ParametersCounter {

	// Code adapted from:http://www.rgagnon.com/javadetails/java-0513.html
	public static List getClasseNamesInPackage(String jarName,
			String packageName) {
		ArrayList classes = new ArrayList();

		packageName = packageName.replaceAll("\\.", "/");
		try {
			JarInputStream jarFile = new JarInputStream(new FileInputStream(
					jarName));
			JarEntry jarEntry;

			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if ((jarEntry.getName().startsWith(packageName))
						&& (jarEntry.getName().endsWith(".class"))) {
					classes.add(jarEntry.getName().replaceAll("/", "\\."));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

}
