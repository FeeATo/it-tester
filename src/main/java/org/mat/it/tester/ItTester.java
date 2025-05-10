package org.mat.it.tester;

import org.mat.it.tester.generator.ClassGenerator;
import org.mat.it.tester.model.*;
import org.mat.it.tester.runner.TestRunner;
import org.mat.it.tester.validator.ClassFinder;

import java.io.File;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class ItTester {
    public static void runTests(Class<?> classe) {

        try {
            List<Class<?>> classList = listClassesInProject(classe);
            TestClasses testClasses = new TestClasses(classList);
            MockClasses mockClasses = new MockClasses(classList);


            List<Class<?>> mockInstances = mockClasses.getInvolvedClasses().stream()
                    .map(ic->ClassGenerator.generateMockClass(ic.getClassz(), ic.getMethods()))
                    .collect(Collectors.toList());

            TestRunner.runTests(testClasses, mockInstances);
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

