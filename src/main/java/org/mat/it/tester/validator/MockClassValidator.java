package org.mat.it.tester.validator;

import org.mat.it.tester.anotations.ClassToTest;
import org.mat.it.tester.anotations.MethodToTest;
import org.mat.it.tester.anotations.MockedReturnFile;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mat.it.tester.model.TestCenario.CENARIOS_FOLDER;

public class MockClassValidator {


    public static Class<?> validate(Class<?> classz) {
        List<Method> methodWithMockedResult = Arrays.stream(classz.getMethods())
                .filter(method -> method.getAnnotation(MockedReturnFile.class) != null)
                .collect(Collectors.toList());
        for (Method method : methodWithMockedResult) {
            validateMethod(classz, method);
        }
        if (!methodWithMockedResult.isEmpty()) {
            return classz;
        }
        return null;
    }

    private static void validateMethod(Class<?> clazzs, Method method) {
        try {
            MockedReturnFile mockedReturnFile = method.getAnnotation(MockedReturnFile.class);
            if (mockedReturnFile.file() == null || mockedReturnFile.file().isEmpty()) {
                throw new IllegalArgumentException("Método sem arquivo com resultado mockado");
            }

            File mockReturnFile = new File(CENARIOS_FOLDER, mockedReturnFile.file());
            if (!mockReturnFile.exists()) {
                throw new IllegalArgumentException("Arquivo de mock fornecido não existe");
            }
            if (mockReturnFile.isDirectory()) {
                throw new IllegalArgumentException("Arquivo de mock fornecido é uma pasta");
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage() + " " + ValidatorUtils.determinaMethodSignature(clazzs, method));
        }
    }
}