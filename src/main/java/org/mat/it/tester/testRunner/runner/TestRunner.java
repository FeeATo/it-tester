package org.mat.it.tester.testRunner.runner;

import com.google.gson.Gson;
import org.mat.it.tester.testRunner.exceptions.TesterRuntimeException;
import org.mat.it.tester.testRunner.model.CaseFolder;
import org.mat.it.tester.testRunner.model.CenarioFolder;
import org.mat.it.tester.testRunner.model.InvolvedClasses;
import org.mat.it.tester.testRunner.model.TestClasses;
import org.mat.it.tester.testRunner.validator.CenariosAccess;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;

import static org.burningwave.core.assembler.StaticComponentContainer.Constructors;

public class TestRunner {

    private static Gson gson = new Gson();

    public static void runTests(TestClasses classToTestMap, List<Class<?>> mockClasses) {
        try {
            for (InvolvedClasses.InvolvedClass testClass : classToTestMap.getInvolvedClasses()) {

                Class<?> classToTest = testClass.getClassz();
                for (Method method : testClass.getMethods()) {
                    CenarioFolder cenarioFolder = CenariosAccess.CENARIOS_FOLDERS.get(method.getName());

                    for (CaseFolder caseFolder : cenarioFolder.getCaseFolderList()) {

                        Class<?> mockClass = mockClasses.get(0);
                        Object mockObject = Constructors.newInstanceOf(mockClass, caseFolder);

                        Object classToTestInstance = Constructors.newInstanceOf(classToTest, mockObject);

                        Object methodParameter = gson.fromJson(new FileReader(caseFolder.getInput()), method.getParameters()[0].getType());
                        Object returnn = method.invoke(classToTestInstance, methodParameter);

                        System.out.println("Cenário - " + caseFolder.getCaseName() + ": ");
                        System.out.println(gson.toJson(returnn));
                        System.out.println("================= \n");
                    }
                }
            }
        } catch (Exception ex) {
            throw new TesterRuntimeException(ex);
        }
    }
}
