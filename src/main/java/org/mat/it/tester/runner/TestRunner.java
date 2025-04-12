package org.mat.it.tester.runner;

import com.google.gson.Gson;
import org.mat.it.tester.exceptions.TesterRuntimeException;
import org.mat.it.tester.anotations.MethodToTest;
import org.mat.it.tester.model.TestCenario;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;

import static org.mat.it.tester.model.TestCenario.CENARIOS_FOLDER;

public class TestRunner {

    private static Gson gson = new Gson();

    public static void runTests(List<Class<?>> classToRunTests) {
        try {
            for (Class classz : classToRunTests) {
                Object instance = classz.getConstructor().newInstance();

                for (Method method : classz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(MethodToTest.class)) {
                        Object methodParameter = gson.fromJson(new FileReader(buildInputFile(method.getAnnotation(MethodToTest.class))), method.getParameters()[0].getType());
                        Object returnn = method.invoke(instance, methodParameter);

                        System.out.println(returnn);
                    }
                }

            }
        } catch (Exception ex) {
            throw new TesterRuntimeException(ex);
        }

    }

    private static File buildInputFile(MethodToTest methodToTest) {
        return new File(new File(CENARIOS_FOLDER, methodToTest.pastaCenarios()), TestCenario.INPUT_FILE);
    }
}
