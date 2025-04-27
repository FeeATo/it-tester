package org.mat.it.tester.generator;

import com.google.gson.Gson;
import org.burningwave.core.Virtual;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.assembler.ComponentSupplier;
import org.burningwave.core.classes.*;
import org.mat.it.tester.anotations.MockedReturn;
import org.mat.it.tester.model.CaseFolder;
import org.mat.it.tester.validator.CenariosAccess;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.burningwave.core.assembler.StaticComponentContainer.Constructors;

public class ClassGenerator {
    private final static String PACKAGE_NAME = "org.mat.it.tester.dynamicgeneration";

    public static Class<?> generateMockClass(Class<?> classz, List<Method> methods) {
        UnitSourceGenerator unitSourceGenerator = UnitSourceGenerator.create(PACKAGE_NAME)
                .addClass(generateClass(classz, methods))
                .addImport(Gson.class, java.nio.file.Files.class)
                .addImport("java.io.*");
//
//
        Class<?> mockClass = loadMockClass(unitSourceGenerator);

//        Map<Class<?>, Class<?>> classAndMockClass = new HashMap<>();
//        classAndMockClass.put(classz, mockClass);
        return mockClass;
    }

    private static ClassSourceGenerator generateClass(Class<?> classz, List<Method> methods) {
        ClassSourceGenerator extendedClass = ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create("MockedClass"))
                .addModifier(Modifier.PUBLIC)
                .addField(VariableSourceGenerator.create(CaseFolder.class, "caseFolder"), VariableSourceGenerator.create(Gson.class, "gson"))
                .addConstructor(createClassConstructor())
                .expands(classz);

        for (Method methodToOverride : methods) {
            List<VariableSourceGenerator> methodParameters = Arrays.stream(methodToOverride.getParameters()).map(param -> VariableSourceGenerator.create(param.getType(), param.getName())).collect(Collectors.toList());
            MockedReturn mockedReturn = methodToOverride.getAnnotation(MockedReturn.class);

            extendedClass.addMethod(
                    FunctionSourceGenerator.create(methodToOverride.getName())
                            .setReturnType(methodToOverride.getReturnType())
                            .addParameter(methodParameters.toArray(new VariableSourceGenerator[methodParameters.size()]))
                            .addModifier(Modifier.PUBLIC)
                            .addBodyCodeLine("try { ",
                                    "File mockFile = caseFolder.getMockFile(\"" + mockedReturn.file() + "\");",
                                    "String result = Files.readString(mockFile.toPath());",
                                    "return /*gson.fromJson(result, " + methodToOverride.getReturnType().getName() + ".class)*/result;",
                                    "} catch(IOException e) {",
                                    "   throw new RuntimeException(e);",
                                    "}")
            );
        }
        return extendedClass;
    }

    private static FunctionSourceGenerator createClassConstructor() {
        return FunctionSourceGenerator.create()
                .addModifier(Modifier.PUBLIC)
                .addParameter(VariableSourceGenerator.create(CaseFolder.class, "caseFolder"))
                .addBodyCodeLine("this.caseFolder = caseFolder;",
                        "this.gson = new Gson();");
    }

    public static Class<?> loadMockClass(UnitSourceGenerator unitSG) {
        System.out.println("\nGenerated code:\n" + unitSG.make());
        //With this we store the generated source to a path
        unitSG.storeToClassPath(System.getProperty("user.home") + "/Desktop/bw-tests");
        ComponentSupplier componentSupplier = ComponentContainer.getInstance();
        ClassFactory classFactory = componentSupplier.getClassFactory();
        /* this method compile all compilation units and upload the generated classes to default
        class loader declared with property "class-factory.default-class-loader" in
        burningwave.properties file (see "Overview and configuration").
        If you need to upload the class to another class loader use
        loadOrBuildAndDefine(LoadOrBuildAndDefineConfig) method */
        ClassFactory.ClassRetriever classRetriever = classFactory.loadOrBuildAndDefine(
                unitSG
        );
        Class<?> generatedClass = classRetriever.get(
                PACKAGE_NAME+".MockedClass"
        );

        classRetriever.close();
        return generatedClass;
    }
}
