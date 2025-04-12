package org.mat.it.tester.validator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassFinder {

    public static File[] findClasses(File parentFile) {
        List<File> classFiles = new ArrayList<>();
        findClasses(parentFile, classFiles);

        return classFiles.toArray(new File[classFiles.size()]);
    }

    private static void findClasses(File parentFile,  List<File> classFiles) {
        File[] childFiles = parentFile.listFiles();
        if (childFiles != null) {
            for (File childFile : childFiles) {
                if (childFile.isFile() && childFile.getName().endsWith(".class")) {
                    classFiles.add(childFile);
                } else if (childFile.isDirectory()) {
                    findClasses(childFile, classFiles);
                }
            }
        }
    }

}
