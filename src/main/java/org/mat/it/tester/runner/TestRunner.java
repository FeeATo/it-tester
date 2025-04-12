package org.mat.it.tester.runner;

import com.google.gson.Gson;
import org.mat.it.tester.exceptions.TesterRuntimeException;
import org.mat.it.tester.anotations.MethodToTest;
import org.mat.it.tester.model.TestCenario;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;

public class TestRunner {

    private static Gson gson = new Gson();

    public static void runTests(List<Method> classToRunTests) {
        try {
            for (Method method : classToRunTests) {
                MethodToTest methodToTest = method.getAnnotation(MethodToTest.class);

                File pastaCenarios = new File("target/classes/cenarios", methodToTest.pastaCenarios());

                Object parameter = gson.fromJson(new FileReader(new File(pastaCenarios, TestCenario.INPUT_FILE)), method.getParameters()[0].getType());
                System.out.println(parameter);
            }
        } catch (Exception ex) {
            throw new TesterRuntimeException(ex);
        }

    }
}
