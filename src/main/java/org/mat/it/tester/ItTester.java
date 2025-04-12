package org.mat.it.tester;

import org.mat.it.tester.generator.ClassGenerator;
import org.mat.it.tester.runner.TestRunner;
import org.mat.it.tester.validator.ClassFinder;
import org.mat.it.tester.validator.ClassToTestValidator;
import org.mat.it.tester.validator.MockClassValidator;

import java.io.File;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItTester {
    public static void runTests(Class<?> classe) {

        try {
            List<Class<?>> classList = listClassesInProject(classe);
            List<Class<?>> classesToValidate = classList.stream().map(ClassToTestValidator::validateClass).filter(Objects::nonNull).collect(Collectors.toList());
            List<Class<?>> classesToMock = classList.stream().filter(classz->!classesToValidate.contains(classz))
                    .map(MockClassValidator::validate)
                    .map(ClassGenerator::generateMockClass)
                    .collect(Collectors.toList());
            TestRunner.runTests(classesToValidate);
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

