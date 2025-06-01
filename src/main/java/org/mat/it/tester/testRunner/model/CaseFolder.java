package org.mat.it.tester.testRunner.model;

import java.io.File;

import static org.mat.it.tester.testRunner.validator.CenariosAccess.INPUT_FILE;
import static org.mat.it.tester.testRunner.validator.CenariosAccess.OUTPUT_FILE;
import static org.mat.it.tester.testRunner.validator.CenariosAccess.MOCK_FOLDER;

public class CaseFolder {
    
    private String caseName;
    private MockFolder mockFolder;
    private File input;
    private File output;

    public CaseFolder(File caseFolder) {
        caseName = caseFolder.getName();
        File mockFile = null;
        File[] files = caseFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(MOCK_FOLDER)) {
                    mockFile = assignFile(file, mockFile);
                    mockFolder = new MockFolder(mockFile);
                } else if (file.getName().equals(INPUT_FILE)) {
                    input = assignFile(file, input);
                } else if (file.getName().equals(OUTPUT_FILE)) {
                    output = assignFile(file, output);
                }
            }
        } else {
            throw new IllegalArgumentException("Pasta do caso '" + caseName + "' não pode ser utilizada, arquivos estão faltando ou estrutura incorreta;");
        }
        if (input == null) throw new IllegalArgumentException("Arquivo 'input' não encontrado;");
        if (output == null) throw new IllegalArgumentException("Arquivo 'output' não encontrado;");

    }

    public CaseFolder(String caseName, MockFolder mockFolder, File input, File output) {
        this.caseName = caseName;
        this.mockFolder = mockFolder;
        this.input = input;
        this.output = output;
    }

    public MockFolder getMockFolder() {
        return mockFolder;
    }

    public void setMockFolder(MockFolder mockFolder) {
        this.mockFolder = mockFolder;
    }

    public File getInput() {
        return input;
    }

    public void setInput(File input) {
        this.input = input;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    private static File assignFile(File file, File resultFile) {
        if (resultFile != null) {
            throw new IllegalArgumentException("Somente uma pasta '"+file.getName()+"' é permitida em cada caso");
        }
        resultFile = file;
        return resultFile;
    }

    public File getMockFile(String fileName) {
        return this.getMockFolder().getMockResults().get(fileName);
    }
}
