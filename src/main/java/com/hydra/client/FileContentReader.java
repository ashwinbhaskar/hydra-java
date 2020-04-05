package com.hydra.client;

import java.io.IOException;

public interface FileContentReader {
    String fileToBase64(String filePath) throws IOException;
}
