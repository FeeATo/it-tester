package org.mat.it.tester.model;

import java.io.File;

public class TestCenario {

    public static final String INPUT_FILE = "entrada.json";
    public static final String EXPECTED_OUTPUT_FILE = "saidaEsperada.json";
    public static final File CENARIOS_FOLDER = new File("target/classes/cenarios");

    static {
        if (!CENARIOS_FOLDER.exists()) {
            throw new IllegalArgumentException("Pasta 'cenários' não encontrada");
        }
    }
}
