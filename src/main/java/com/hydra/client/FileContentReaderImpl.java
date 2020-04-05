package com.hydra.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.codec.binary.Base64;

public class FileContentReaderImpl implements FileContentReader {

    @Override
    public String fileToBase64(String filePath) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(filePath));
        byte[] base64Content = Base64.encodeBase64(content);
        return new String(base64Content);
    }

}
