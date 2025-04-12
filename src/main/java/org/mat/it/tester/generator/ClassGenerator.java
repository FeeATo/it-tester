package org.mat.it.tester.generator;

import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.TypeDeclarationSourceGenerator;
import org.burningwave.core.classes.UnitSourceGenerator;
import org.mat.it.tester.anotations.MockedReturnFile;

import java.lang.reflect.Modifier;

public class ClassGenerator {
    private final static String PACKAGE_NAME = "org.mat.it.tester.dynamicgeneration";
    public static Object generateMockClass(Class<?> classz) {


        UnitSourceGenerator.create(PACKAGE_NAME).addClass(generateClass(classz))



    }

    private static ClassSourceGenerator generateClass(Class<?> classz) {
        return ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create("ExtendedClass"))
                .addModifier(Modifier.PUBLIC)
                .addMethod(FunctionSourceGenerator.create("invoke"))
    }

    public static void teste() {
        UnitSourceGenerator unitSG = UnitSourceGenerator.create("packagename").addClass(
                ClassSourceGenerator.create(
                        TypeDeclarationSourceGenerator.create("MyExtendedClass")
                ).addModifier(
                        Modifier.PUBLIC
                        //generating new method that override MyInterface.convert(LocalDateTime)
                ).addMethod(
                        FunctionSourceGenerator.create("convert")
                                .setReturnType(
                                        TypeDeclarationSourceGenerator.create(Comparable.class)
                                                .addGeneric(GenericSourceGenerator.create(Date.class))
                                ).addParameter(VariableSourceGenerator.create(LocalDateTime.class, "localDateTime"))
                                .addModifier(Modifier.PUBLIC)
                                .addAnnotation(AnnotationSourceGenerator.create(Override.class))
                                .addBodyCodeLine("return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());")
                                .useType(ZoneId.class)
                ).addConcretizedType(
                        MyInterface.class
                ).expands(ToBeExtended.class)
        );
        System.out.println("\nGenerated code:\n" + unitSG.make());
//With this we store the generated source to a path
        unitSG.storeToClassPath(System.getProperty("user.home") + "/Desktop/bw-tests");
        ComponentSupplier componentSupplier = ComponentContainer.getInstance();
        ClassFactory classFactory = componentSupplier.getClassFactory();
//this method compile all compilation units and upload the generated classes to default
//class loader declared with property "class-factory.default-class-loader" in
//burningwave.properties file (see "Overview and configuration").
//If you need to upload the class to another class loader use
//loadOrBuildAndDefine(LoadOrBuildAndDefineConfig) method
        ClassFactory.ClassRetriever classRetriever = classFactory.loadOrBuildAndDefine(
                unitSG
        );
        Class<?> generatedClass = classRetriever.get(
                "packagename.MyExtendedClass"
        );
        ToBeExtended generatedClassObject =
                Constructors.newInstanceOf(generatedClass);
        generatedClassObject.printSomeThing();
        System.out.println(
                ((MyInterface)generatedClassObject).convert(LocalDateTime.now()).toString()
        );
//You can also invoke methods by casting to Virtual (an interface offered by the
//library for faciliate use of runtime generated classes)
        Virtual virtualObject = (Virtual)generatedClassObject;
//Invoke by using reflection
        virtualObject.invoke("printSomeThing");
//Invoke by using MethodHandle
        virtualObject.invokeDirect("printSomeThing");
        System.out.println(
                ((Date)virtualObject.invokeDirect("convert", LocalDateTime.now())).toString()
        );
        classRetriever.close();
    }
}
