package org.mat.it.tester.validator;

import org.mat.it.tester.anotations.MockedReturn;
import org.mat.it.tester.model.CaseFolder;
import org.mat.it.tester.model.CenarioFolder;
import org.mat.it.tester.model.MockClasses;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MockClassValidator {

    public static final String MOCK_FOLDER = "mock";

    public static void validateClass(Class<?> classz, MockClasses mockClasses) {
        List<Method> methodWithMockedResult = Arrays.stream(classz.getMethods())
                .filter(method -> method.getAnnotation(MockedReturn.class) != null)
                .peek(method -> validateMethod(classz, method))
                .collect(Collectors.toList());
        if (!methodWithMockedResult.isEmpty()) {
            mockClasses.addMethods(classz, methodWithMockedResult);
        }
    }

    private static void validateMethod(Class<?> clazzs, Method method) {
        try {
            MockedReturn mockedReturn = method.getAnnotation(MockedReturn.class);
            if (mockedReturn.file() == null || mockedReturn.file().isEmpty()) {
                throw new IllegalArgumentException("Método sem arquivo com resultado mockado");
            }

            Map<String, CenarioFolder> cenariosFolders = CenariosAccess.CENARIOS_FOLDERS;
            for (CenarioFolder cenarioFolder : cenariosFolders.values()) {
                for (CaseFolder caseFolder : cenarioFolder.getCaseFolderList()) {
                    if (caseFolder.getMockFolder().getMockResults().get(mockedReturn.file()) == null) {
                        throw new IllegalArgumentException("Arquivo de mock fornecido não existe");
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage() + " " + ValidatorUtils.determinaMethodSignature(clazzs, method));
        }
    }
}