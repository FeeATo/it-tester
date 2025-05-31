package org.mat.it.tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultComparator {

    public static void compare(Object obj1, Object obj2, Map<String, Object> classeAtual) throws InvocationTargetException, IllegalAccessException {
        if (obj1.getClass().equals(obj2.getClass())) {
            if (obj1 instanceof Iterable) {
                compareIterables((Iterable<?>) obj1, (Iterable<?>) obj2, classeAtual);
            } else {
                compareObjects(obj1, obj2, classeAtual);
            }
        } else {
            System.out.println("obj1 não é da mesma classe que o obj2");
        }
    }

    private static void compareObjects(Object obj1, Object obj2, Map<String, Object> classeAtual) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> camposClasse = new  HashMap<>();
        classeAtual.put(obj1.getClass().getName(), camposClasse);
        Map<String, Method> methods1Map = Arrays.stream(obj1.getClass().getMethods()).filter(m -> m.getName().startsWith("get")).collect(Collectors.toMap(Method::getName, method -> method));
        Method[] methods2 = obj2.getClass().getMethods();
        for (Method method2 : Arrays.stream(methods2).filter(m -> m.getName().startsWith("get") && isNotNativeMethod(m)).collect(Collectors.toList())) {
            Method method1 = methods1Map.get(method2.getName());

            Object field1Obj = method1.invoke(obj1, null);
            Object field2Obj = method2.invoke(obj2, null);


            compareFields(field1Obj, field2Obj, camposClasse, method1.getName().replace("get",""));
        }
    }

    private static void compareIterables(Iterable<?> obj1, Iterable<?> obj2, Map<String, Object> classeAtual) throws InvocationTargetException, IllegalAccessException {
        int indexItemList1 = 0;
        int indexItemList2 = 0;
        for (Object itemList1Obj : obj1) {
            for (Object itemList2Obj : obj2) {
                if (indexItemList1 == indexItemList2) {
                    compareFields(itemList1Obj, itemList2Obj, classeAtual, indexItemList1+"");
                    indexItemList2 = 0;
                    break;
                }
                indexItemList2++;
            }
            indexItemList1++;
        }
    }

    private static void compareFields(Object obj1, Object obj2, Map<String, Object> classeCampos, String fieldIdentifier) throws InvocationTargetException, IllegalAccessException {
        if (obj1 == null && obj2 == null) {
            classeCampos.put(fieldIdentifier, "OK");
            System.out.println(fieldIdentifier + " OK");
        } else if (obj1 != null && obj2 != null) {
            if (obj1.getClass().equals(obj2.getClass())) {

                if (isPrimitiveOrPrimitiveWrapperOrString(obj1.getClass())) {
                    boolean saoIguais = obj1.equals(obj2);
                    if (saoIguais) {
                        classeCampos.put(fieldIdentifier, "OK");
                        System.out.println(fieldIdentifier + " OK");
                    } else {
                        classeCampos.put(fieldIdentifier, assembleDifferenceMessage(obj1, obj2));
                        System.out.println(fieldIdentifier + " " + assembleDifferenceMessage(obj1, obj2));
                    }
                } else {
                    Map<String, Object> campoNovaClasse = new HashMap<>();
                    classeCampos.put(fieldIdentifier, campoNovaClasse);
                    compare(obj1, obj2, campoNovaClasse);
                }
            } else {
                throw new RuntimeException("obj1 e obj2 de classes diferentes");
            }
        } else {
            classeCampos.put(fieldIdentifier, assembleDifferenceMessage(obj1, obj2));
            System.out.println(fieldIdentifier + " " + assembleDifferenceMessage(obj1, obj2));
        }
    }

    private static String assembleDifferenceMessage(Object obj1, Object obj2) {
        String result = "DIFFERENT (";
        if (obj1 instanceof String) {
            result += "'"+obj1+"' != '"+obj2+"'";
        } else {
            result += obj1 + " != " + obj2;
        }
        result += ")";
        return result;
    }

    private static boolean isNotNativeMethod(Method m) {
        return !m.getName().equals("getClass");
    }

    public static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
        return (type.isPrimitive() && type != void.class) ||
                type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }
}
