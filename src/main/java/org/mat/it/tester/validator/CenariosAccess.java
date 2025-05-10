package org.mat.it.tester.validator;

import org.mat.it.tester.model.CaseFolder;
import org.mat.it.tester.model.CenarioFolder;
import org.mat.it.tester.model.MockFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Estrutura da pasta de cenários:
 *
 * cenarios
 *  \_ cenario (ex.: reservar)
 *           \_ caso (ex.: 1)
 *                 \_ mock
 *                       \_ arquivosMock.json
 *                 input.json
 *                 output.json
 *
 * **/
public class CenariosAccess {

    public static final String INPUT_FILE = "input.json";
    public static final String OUTPUT_FILE = "output.json";
    public static final String MOCK_FOLDER = "mock";
    public static final File CENARIOS = new File("target/classes/cenarios");
    public static final Map<String, CenarioFolder> CENARIOS_FOLDERS = new HashMap<>();
    
    private CaseFolder caseFolder;

    static {
        if (!CENARIOS.exists()) {
            throw new IllegalArgumentException("Pasta 'cenários' não encontrada");
        }

        File[] cenariosFolders = CENARIOS.listFiles();
        if (cenariosFolders != null) {
            for (File cenarioFile : cenariosFolders) {
                if (cenarioFile.isDirectory()) {
                    CenarioFolder cenarioFolder = new CenarioFolder(cenarioFile);
                    CENARIOS_FOLDERS.put(cenarioFolder.getCenarioName(), cenarioFolder);
                }
            }
        }
    }

    public static void accessCenarioDirectory(String directory) {
        Optional.ofNullable(CENARIOS_FOLDERS.get(directory))
                .orElseThrow(()->new IllegalArgumentException("Pasta de cenários '" + directory + "' não encontrada"));
    }

    public static File accessCaseFolder(File cenariosFolder, String cenarioDirectory) {
        File fileCenario = new File(cenariosFolder, cenarioDirectory);
        if (!fileCenario.exists()) {
            throw new IllegalArgumentException("Pasta do cenário '" + cenarioDirectory + "' não existe");
        }
        return fileCenario;
    }

    public CaseFolder getCaseFolder() {
        return caseFolder;
    }

    public void setCaseFolder(CaseFolder caseFolder) {
        this.caseFolder = caseFolder;
    }
}
