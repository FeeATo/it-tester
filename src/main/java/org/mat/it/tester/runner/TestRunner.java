package org.mat.it.tester.runner;

import com.google.gson.Gson;
import org.mat.it.tester.exceptions.TesterRuntimeException;
import org.mat.it.tester.model.CaseFolder;
import org.mat.it.tester.model.CenarioFolder;
import org.mat.it.tester.model.InvolvedClasses;
import org.mat.it.tester.model.TestClasses;
import org.mat.it.tester.validator.CenariosAccess;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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

                        Class<?> mockCLass = mockClasses.get(0);
                        Object mockObject = Constructors.newInstanceOf(mockCLass, caseFolder);

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
