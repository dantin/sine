package com.cosmos.sine.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static com.cosmos.sine.core.Consts.*;

/**
 * Package scanner utility.
 *
 * @author BSD
 */
public class PackageScanner {

    /**
     * Scan classes in package.
     *
     * @param packageName package name
     * @return scanned class
     * @throws ClassNotFoundException
     */
    public List<Class<?>> scan(String packageName) throws ClassNotFoundException {

        List<Class<?>> classes = new LinkedList<>();

        try {
            // Thread context ClassLoader, which is the current classloader for the current thread.
            // It can load resources that are not available on it's own classloader.
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = loader.getResources(packageName.replace(DOT_CHAR, SLASH_CHAR));
            while (urls.hasMoreElements()) {
                URI uri = urls.nextElement().toURI();
                switch (uri.getScheme().toLowerCase()) {
                    case JAR_PROTOCOL:
                        scanFromJarProtocol(loader, classes, uri.getRawSchemeSpecificPart());
                        break;
                    case FILE_PROTOCOL:
                        scanFromFileProtocol(loader, classes, uri.getPath(), packageName);
                        break;
                    default:
                        throw new URISyntaxException(uri.getScheme(), "unknown schema " + uri.getScheme());
                }

            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * Load class using specified class loader.
     *
     * @param loader class loader
     * @param classPath class path string
     * @return class
     * @throws ClassNotFoundException class not found exception
     */
    private Class<?> loadClass(ClassLoader loader, String classPath) throws ClassNotFoundException {
        classPath = classPath.substring(0, classPath.length() - CLASS_SUFFIX.length());
        return loader.loadClass(classPath);
    }

    /**
     * Scan class from file system.
     *
     * @param loader class loader
     * @param classes classes container
     * @param dir package specified directory
     * @param packageName package name
     * @throws ClassNotFoundException class not found exception
     */
    private void scanFromFileProtocol(ClassLoader loader, List<Class<?>> classes, String dir, String packageName) throws ClassNotFoundException {
        File directory = new File(dir);
        File[] files = directory.listFiles();
        if (directory.isDirectory() && files != null) {
            for (File classFile : files) {
                if (!classFile.isDirectory() && classFile.getName().endsWith(CLASS_SUFFIX) && !classFile.getName().contains(INNER_CLASS_TOKEN)) {
                    String className = String.format("%s.%s", packageName, classFile.getName());
                    classes.add(loadClass(loader, className));
                }
            }
        }
    }

    /**
     * Scan class from jar file.
     *
     * @param loader class loader
     * @param classes class container
     * @param fullPath jar file path
     * @throws ClassNotFoundException class not found exception
     */
    private void scanFromJarProtocol(ClassLoader loader, List<Class<?>> classes, String fullPath) throws ClassNotFoundException {
        final String jar = fullPath.substring(0, fullPath.lastIndexOf(JAR_FILE_SEPARATOR_CHAR));
        final String parent = fullPath.substring(fullPath.lastIndexOf(JAR_FILE_SEPARATOR_CHAR) + 2);
        JarEntry e;

        JarInputStream jarReader = null;
        try {
            jarReader = new JarInputStream(new URL(jar).openStream());
            while ((e = jarReader.getNextJarEntry()) != null) {
                String className = e.getName();
                if (!e.isDirectory() && className.startsWith(parent) && className.endsWith(CLASS_SUFFIX) && !className.contains(INNER_CLASS_TOKEN)) {
                    className = className.replace(SLASH_CHAR, DOT_CHAR);
                    classes.add(loadClass(loader, className));
                }
                jarReader.closeEntry();
            }
        } catch (IOException error) {
            error.printStackTrace();
        } finally {
            try {
                if (jarReader != null)
                    jarReader.close();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
    }
}
