package org.mat.it.tester.validator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ValidatorUtils {

    public static String determinaMethodSignature(Class<?> classz, Method method) {
        StringBuilder signature = new StringBuilder();
        signature.append("(")
                .append(classz.getName())
                .append(".")
                .append(method.getName())
                .append("(")
                .append(Arrays.stream(method.getParameters()).map(par -> par.getType().getSimpleName() + " " + par.getName()).collect(Collectors.joining(", ")))
                .append("))");
        return signature.toString();
    }

}
