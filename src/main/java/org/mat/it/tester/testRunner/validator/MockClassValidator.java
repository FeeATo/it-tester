package org.mat.it.tester.testRunner.validator;

import org.mat.it.tester.testRunner.anotations.MockedReturn;
import org.mat.it.tester.testRunner.model.ToBeMockedClasses;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MockClassValidator {

    public static void validateClass(Class<?> classz, ToBeMockedClasses toBeMockedClasses) {
        List<Method> methodWithMockedResult = Arrays.stream(classz.getMethods())
                .filter(method -> method.getAnnotation(MockedReturn.class) != null)
                .peek(method -> validateMethod(classz, method))
                .collect(Collectors.toList());
        if (!methodWithMockedResult.isEmpty()) {
            toBeMockedClasses.addMethods(classz, methodWithMockedResult);
        }
    }

    private static void validateMethod(Class<?> clazzs, Method method) {
        try {
            MockedReturn mockedReturn = method.getAnnotation(MockedReturn.class);
            if (mockedReturn.file() == null || mockedReturn.file().isEmpty()) {
                throw new IllegalArgumentException("Método sem arquivo com resultado mockado");
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage() + " " + ValidatorUtils.determinaMethodSignature(clazzs, method));
        }
    }
}