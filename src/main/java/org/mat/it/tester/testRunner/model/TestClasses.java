package org.mat.it.tester.testRunner.model;

import org.mat.it.tester.testRunner.validator.ClassToTestValidator;

import java.util.List;

public class TestClasses extends InvolvedClasses {

    public TestClasses(List<Class<?>> classList) {
        super();
        classList.forEach(classz -> ClassToTestValidator.validateClass(classz, this));
    }
}
