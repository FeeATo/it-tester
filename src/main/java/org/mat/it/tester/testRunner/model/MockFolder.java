package org.mat.it.tester.testRunner.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MockFolder {
    
    private Map<String, File> mockResults;

    public MockFolder(File mockFolder) {
        if (mockFolder.isDirectory()) {
            File[] mockFiles = mockFolder.listFiles();

            if (mockFiles != null) {
                mockResults = new HashMap<>();
                for (File mockFile : mockFiles) {
                    mockResults.put(mockFile.getName().replace("."+mockFile.getName().split("\\.")[1],""), mockFile);
                }
            }
        } else {
            throw new IllegalArgumentException("'mock' deve ser uma pasta");
        }
    }

    public MockFolder(Map<String, File> mockResults) {
        this.mockResults = mockResults;
    }

    public Map<String, File> getMockResults() {
        return mockResults;
    }

    public void setMockResults(Map<String, File> mockResults) {
        this.mockResults = mockResults;
    }
}
