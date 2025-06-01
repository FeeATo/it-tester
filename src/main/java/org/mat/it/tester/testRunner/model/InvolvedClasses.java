package org.mat.it.tester.testRunner.model;


import java.lang.reflect.Method;
import java.util.*;

/**
 * Representa uma coleção de classes e seus métodos envolvidos no processo do teste. Sendo mock ou sendo a classe a ser testada
 * **/
public class InvolvedClasses {

    protected InvolvedClasses() {
    }

    private final Set<InvolvedClass> involvedClasses = new HashSet<>();

    protected void addTestingClass(Class<?> classz) {
        involvedClasses.add(new InvolvedClass(classz));
    }

    public void addMethods(Class<?> classz, List<Method> methods) {
        InvolvedClass involvedClass = involvedClasses.stream()
                .filter(tc -> tc.getClass().equals(classz))
                .findFirst()
                .orElse(Optional.of(new InvolvedClass(classz)).stream().peek(involvedClasses::add).findAny().get());
        involvedClass.getMethods().addAll(methods);
    }

    public void addMethod(Class<?> classz, Method method) {
        this.addMethods(classz, Arrays.asList(method));
    }

    public Set<InvolvedClass> getInvolvedClasses() {
        return involvedClasses;
    }

    public static class InvolvedClass {
        private Class<?> classz;
        private List<Method> methods;

        protected InvolvedClass(Class<?> classz) {
            this.classz = classz;
        }

        public Class<?> getClassz() {
            return classz;
        }

        public List<Method> getMethods() {
            if (methods == null) {
                methods = new ArrayList<>();
            }

            return methods;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            InvolvedClass that = (InvolvedClass) o;
            return Objects.equals(classz, that.classz);
        }
    }

}
