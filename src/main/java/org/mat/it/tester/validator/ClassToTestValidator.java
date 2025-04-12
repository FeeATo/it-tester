package org.mat.it.tester.validator;

import com.mat.shared.util.Utils;
import org.mat.it.tester.anotations.ClassToTest;
import org.mat.it.tester.anotations.MethodToTest;
import org.mat.it.tester.model.TestCenario;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mat.it.tester.model.TestCenario.CENARIOS_FOLDER;

public class ClassToTestValidator {

    public static Class<?> validateClass(Class<?> classz) throws IllegalArgumentException {
        Annotation annotations = classz.getAnnotation(ClassToTest.class);
        if (annotations != null) {
            List<Method> classMethodsToTest = Arrays.stream(classz.getMethods())
                    .filter(method -> method.getAnnotation(MethodToTest.class) != null)
                    .collect(Collectors.toList());
            for (Method method : classMethodsToTest) {
                validateMethod(classz, method);
            }
            if (!classMethodsToTest.isEmpty()) {
                return classz;
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

            File pastaCenarios = new File(CENARIOS_FOLDER, methodToTest.pastaCenarios());
            if (!pastaCenarios.exists()) {
                throw new IllegalArgumentException("Pasta de cenários '" + methodToTest.pastaCenarios() +"' não encontrada");
            }
            File inputFile = new File(pastaCenarios, TestCenario.INPUT_FILE);
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("Arquivo de entrada do cenário '" + methodToTest.pastaCenarios() +"' não encontrado");
            }
            File outputFile = new File(pastaCenarios, TestCenario.EXPECTED_OUTPUT_FILE);
            if (!outputFile.exists()) {
                throw new IllegalArgumentException("Arquivo de saída do cenário '" + methodToTest.pastaCenarios() +"' não encontrado");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + " " + ValidatorUtils.determinaMethodSignature(classz, method));
        }
    }

}
