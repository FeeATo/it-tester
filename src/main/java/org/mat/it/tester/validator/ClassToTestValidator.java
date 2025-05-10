package org.mat.it.tester.validator;

import com.mat.shared.util.Utils;
import org.mat.it.tester.anotations.MethodToTest;
import org.mat.it.tester.model.TestClasses;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassToTestValidator {

    public static void validateClass(Class<?> classz, TestClasses testClasses) throws IllegalArgumentException {
        List<Method> classMethodsToTest = Arrays.stream(classz.getMethods())
                .filter(method -> method.getAnnotation(MethodToTest.class) != null)
                .peek(method -> validateMethod(classz, method))
                .collect(Collectors.toList());
        if (!classMethodsToTest.isEmpty()) {
            testClasses.addMethods(classz, classMethodsToTest);
        }
    }

    private static void validateMethod(Class<?> classz, Method method) {
        MethodToTest methodToTest = method.getAnnotation(MethodToTest.class);
        try {
            if (Utils.isNothing(methodToTest.pastaCenarios())) {
                throw new IllegalArgumentException("Pasta de cenários de teste não definida");
            }
            if (method.getParameterCount() > 1) {
                throw new IllegalArgumentException("Método não pode ter mais de um parâmetro");
            } else if (method.getParameterCount() == 0) {
                throw new IllegalArgumentException("Método tem que ter pelo menos um parâmetro");
            }
            if (method.getReturnType().equals(void.class)) {
                throw new IllegalArgumentException("Método não pode ter retorno como void");
            }
            if (method.getReturnType().isPrimitive()) {
                throw new IllegalArgumentException("Método não pode retornar um tipo primitivo");
            }

            CenariosAccess.accessCenarioDirectory(methodToTest.pastaCenarios());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + " " + ValidatorUtils.determinaMethodSignature(classz, method));
        }
    }

}
