package org.mat.it.tester.model;

import org.mat.it.tester.validator.MockClassValidator;

import java.util.List;

public class ToBeMockedClasses extends InvolvedClasses {
    public ToBeMockedClasses(List<Class<?>> classList) {
        super();
        classList.forEach(classz -> MockClassValidator.validateClass(classz, this));
    }
}
