package org.mat.it.tester;

import org.mat.it.tester.testRunner.generator.ClassGenerator;
import org.mat.it.tester.testRunner.model.*;
import org.mat.it.tester.testRunner.runner.TestRunner;
import org.mat.it.tester.testRunner.validator.ClassFinder;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class ItTester {
    public static void runTests(Class<?> classe) {

        try {
            List<Class<?>> classList = listClassesInProject(classe);
            TestClasses testClasses = new TestClasses(classList);
            ToBeMockedClasses toBeMockedClasses = new ToBeMockedClasses(classList);

            List<Class<?>> mockInstances = toBeMockedClasses.getInvolvedClasses().stream()
                    .map(ic->ClassGenerator.generateMockClass(ic.getClassz(), ic.getMethods()))
                    .collect(Collectors.toList());

            TestRunner.runTests(testClasses, mockInstances);
        } catch (MalformedURLException | ClassNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<Class<?>> listClassesInProject(Class<?> classe) throws URISyntaxException, MalformedURLException, ClassNotFoundException {
        URI uri = classe.getProtectionDomain().getCodeSource().getLocation().toURI();
        File parentFile = new File(uri);
        File[] classes = ClassFinder.findClasses(parentFile);
        File rootDir = new File("target/classes");

        try (URLClassLoader loader = new URLClassLoader(new URL[]{rootDir.toURI().toURL()})) {
            List<Class<?>> classList = new ArrayList<>();
            for (File classz : classes) {
                String classname = rootDir.toURI().relativize(classz.toURI()).getPath().replace("/", ".").replace(".class", "");
                classList.add(loader.loadClass(classname));
            }
            return classList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

