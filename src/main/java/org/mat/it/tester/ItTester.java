package org.mat.it.tester;

import org.mat.it.tester.runner.TestRunner;
import org.mat.it.tester.validator.ClassFinder;
import org.mat.it.tester.validator.ClassSrvValidator;

import java.io.File;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItTester {
    public static void runTests(Class<?> classe) {

        try {
            List<Class<?>> classList = listClassesInProject(classe);
            List<Method> methodsToTest = classList.stream().map(ClassSrvValidator::getMethodsToTest).flatMap(Collection::stream).collect(Collectors.toList());
            TestRunner.runTests(methodsToTest);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<Class<?>> listClassesInProject(Class<?> classe) throws URISyntaxException, MalformedURLException, ClassNotFoundException {
        URI uri = classe.getProtectionDomain().getCodeSource().getLocation().toURI();
        File parentFile = new File(uri);
        File[] classes = ClassFinder.findClasses(parentFile);
        File rootDir = new File("target/classes");

        URLClassLoader loader = new URLClassLoader(new URL[]{rootDir.toURI().toURL()});

        List<Class<?>> classList = new ArrayList<>();
        for (File classz : classes) {
            String classname = rootDir.toURI().relativize(classz.toURI()).getPath().replace("/", ".").replace(".class", "");
            classList.add(loader.loadClass(classname));
        }
        return classList;
    }
}

