package org.mat.it.tester;

import org.mat.it.tester.generator.ClassGenerator;
import org.mat.it.tester.model.CaseFolder;
import org.mat.it.tester.model.CenarioFolder;
import org.mat.it.tester.model.MockFolder;
import org.mat.it.tester.runner.TestRunner;
import org.mat.it.tester.validator.CenariosAccess;
import org.mat.it.tester.validator.ClassFinder;
import org.mat.it.tester.validator.ClassToTestValidator;
import org.mat.it.tester.validator.MockClassValidator;

import java.io.File;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class ItTester {
    public static void runTests(Class<?> classe) {

        try {
            List<Class<?>> classList = listClassesInProject(classe);
            Map<Class<?>, List<Method>> classToTest = new HashMap<>();
            for (Class<?> classz : classList) {
                Map<Class<?>, List<Method>> validatedClass = ClassToTestValidator.validateClass(classz);
                if (validatedClass != null) {
                    classToTest.putAll(validatedClass);
                }
            }

            Map<Class<?>, List<Method>> classToMock = new HashMap<>();
            for (Class<?> classz : classList) {
                if (!classToTest.containsKey(classz)) {
                    Map<Class<?>, List<Method>> validatedMockClass = MockClassValidator.validateClass(classz);
                    if (validatedMockClass != null){
                        classToMock.putAll(validatedMockClass);
                    }
                }
            }


            List<Class<?>> mockInstances = classToMock.entrySet().stream()
                    .map(entry->ClassGenerator.generateMockClass(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            TestRunner.runTests(classToTest, mockInstances);
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

