package org.mat.it.tester.validator;

import com.mat.shared.util.Utils;
import org.mat.it.tester.anotations.ClassToTest;
import org.mat.it.tester.anotations.MethodToTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassToTestValidator {

    /**
     * @return Return null if class doesn't have @ClassToTest annotation
     * **/
    public static Map<Class<?>, List<Method>> validateClass(Class<?> classz) throws IllegalArgumentException {
        Map<Class<?>, List<Method>> methodClassMap = new HashMap<>();

        Annotation annotations = classz.getAnnotation(ClassToTest.class);
        if (annotations != null) {
            List<Method> classMethodsToTest = Arrays.stream(classz.getMethods())
                    .filter(method -> method.getAnnotation(MethodToTest.class) != null)
                    .collect(Collectors.toList());
            for (Method method : classMethodsToTest) {
                validateMethod(classz, method);
            }
            methodClassMap.put(classz, classMethodsToTest);
            if (!classMethodsToTest.isEmpty()) {
                return methodClassMap;
            }
        }
        return null;
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
