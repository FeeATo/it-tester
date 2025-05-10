package org.mat.it.tester.model;

import org.mat.it.tester.validator.MockClassValidator;

import java.util.List;

public class MockClasses extends InvolvedClasses {
    public MockClasses(List<Class<?>> classList) {
        super();
        classList.forEach(classz -> MockClassValidator.validateClass(classz, this));
    }
}
