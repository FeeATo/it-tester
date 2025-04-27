package org.mat.it.tester.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CenarioFolder {
    
    private String cenarioName;
    private List<CaseFolder> caseFolderList;

    public CenarioFolder(String cenarioName, List<CaseFolder> caseFolderList) {
        this.cenarioName = cenarioName;
        this.caseFolderList = caseFolderList;
    }

    public CenarioFolder(File cenarioFile) {
        this.cenarioName = cenarioFile.getName();

        try {
            File[] caseFiles = cenarioFile.listFiles();
            if (caseFiles != null) {
                caseFolderList = new ArrayList<>();
                for (File caseFolderFile : caseFiles) {
                    if (caseFolderFile.isDirectory()) {
                        caseFolderList.add(new CaseFolder(caseFolderFile));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage().concat(" cenário '"+cenarioFile.getName()+"';"));
        }
    }

    public List<CaseFolder> getCaseFolderList() {
        return caseFolderList;
    }

    public void setCaseFolderList(List<CaseFolder> caseFolderList) {
        this.caseFolderList = caseFolderList;
    }

    public String getCenarioName() {
        return cenarioName;
    }

    public void setCenarioName(String cenarioName) {
        this.cenarioName = cenarioName;
    }
}
