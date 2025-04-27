package org.mat.it.tester.runner;

import com.google.gson.Gson;
import org.mat.it.tester.exceptions.TesterRuntimeException;
import org.mat.it.tester.model.CaseFolder;
import org.mat.it.tester.model.CenarioFolder;
import org.mat.it.tester.validator.CenariosAccess;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.burningwave.core.assembler.StaticComponentContainer.Constructors;

public class TestRunner {

    private static Gson gson = new Gson();

    public static void runTests(Map<Class<?>, List<Method>> classToTestMap, List<Class<?>> mockClasses) {
        try {
            for (Map.Entry<Class<?>, List<Method>> classToTestAndMethods : classToTestMap.entrySet()) {

                Class<?> classToTest = classToTestAndMethods.getKey();

                for (Method method : classToTestAndMethods.getValue()) {
                    CenarioFolder cenarioFolder = CenariosAccess.CENARIOS_FOLDERS.get(method.getName());

                    for (CaseFolder caseFolder : cenarioFolder.getCaseFolderList()) {

                        Class<?> mockCLass = mockClasses.get(0);
                        Object mockObject = Constructors.newInstanceOf(mockCLass, caseFolder);

                        Object classToTestInstance = Constructors.newInstanceOf(classToTest, mockObject);

                        Object methodParameter = gson.fromJson(new FileReader(caseFolder.getInput()), method.getParameters()[0].getType());
                        Object returnn = method.invoke(classToTestInstance, methodParameter);

                        System.out.println(returnn);
                    }

                }
            }
        } catch (Exception ex) {
            throw new TesterRuntimeException(ex);
        }

    }
}
