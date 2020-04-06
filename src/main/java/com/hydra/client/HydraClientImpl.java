package com.hydra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydra.error.HydraException;
import com.hydra.model.FileContent;
import com.hydra.model.HydraRequest;
import com.hydra.model.HydraResponse;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class HydraClientImpl implements HydraClient {
    private OkHttpClient httpClient;
    private String dataSourceId;
    private ObjectMapper objectMapper;
    private String authorizationKey;
    private Map<String, String> fileExtensionToMimeType = new HashMap<>();
    private HashSet<String> validExtensions = new HashSet<>();
    private FileContentReader fileContentReader;

    public HydraClientImpl(String authorizationKey, String dataSourceId, FileContentReader fileContentReader) {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(20))
                .readTimeout(Duration.ofSeconds(20))
                .build();
        objectMapper = new ObjectMapper();
        this.dataSourceId = dataSourceId;
        this.authorizationKey = authorizationKey;
        this.fileContentReader = fileContentReader;
        fileExtensionToMimeType.put("bmp", "image/bmp");
        fileExtensionToMimeType.put("pdf", "application/pdf");
        fileExtensionToMimeType.put("gif", "image/gif");
        fileExtensionToMimeType.put("jpeg", "image/jpeg");
        fileExtensionToMimeType.put("jpg", "image/jpg");
        fileExtensionToMimeType.put("png", "image/png");
        validExtensions.addAll(fileExtensionToMimeType.keySet());
    }

    @Override
    public HydraResponse recognize(String... filePaths) throws HydraException {
        return recognize(2, filePaths);
    }

    private HydraResponse recognize(int retryCount, String... filePaths) throws HydraException {
        validateFiles(filePaths);
        ArrayList<FileContent> fileContents;
        try {
            fileContents = new ArrayList<>(toFileContents(filePaths));
        } catch (IOException e) {
            throw new HydraException(e.getMessage());
        }
        try {
            HydraRequest hydraRequest = new HydraRequest(fileContents);
            RequestBody body = RequestBody.create(objectMapper.writeValueAsBytes(hydraRequest), MediaType.parse("application/json"));
            String recognizePath = "/api/hydra/";
            Request request = new Request.Builder()
                    .url(getHost() + recognizePath + dataSourceId + "/")
                    .addHeader("Authorization", "Basic " + authorizationKey)
                    .post(body)
                    .build();
            Response response = httpClient.newCall(request).execute();
            int statusCode = response.code();
            if (statusCode == 200) {
                //System.out.println("response string = "+ response.body().string());
                return objectMapper.readValue(response.body().bytes(), HydraResponse.class);
            }
            if (statusCode >= 500 && statusCode <= 599 && retryCount > 0) {
                return recognize(retryCount - 1, filePaths);
            } else {
                throw new HydraException("Something went wrong.\n Status Code =  " + response.code());
            }
        } catch (Exception e) {
            throw new HydraException(e.getMessage());
        }
    }

    private ArrayList<FileContent> toFileContents(String... filePaths) throws IOException {
        ArrayList<FileContent> fileContents = new ArrayList<>();
        for (String filePath : filePaths) {
            String base64File = fileContentReader.fileToBase64(filePath);
            String extension = getExtension(filePath).toLowerCase();
            String mimeType = fileExtensionToMimeType.get(extension);
            fileContents.add(new FileContent(mimeType, base64File));
        }
        return fileContents;
    }

    private String getExtension(String filePath) {
        String[] elems = filePath.split("\\.");
        if (elems.length == 0) return null;
        else return elems[elems.length - 1];
    }

    private void validateFiles(String... filePaths) throws HydraException {
        if (filePaths == null || filePaths.length == 0) throw new HydraException("Files paths cannot be empty or null");
        else {
            for (String filePath : filePaths) {
                String t = getExtension(filePath);
                String extension = t == null ? "" : t;
                if (!validExtensions.contains(extension.toLowerCase()))
                    throw new HydraException("Only bmp, gif, png, jpeg and jpg files allowed");
            }
        }
    }

    private String getHost() {
        String env = System.getProperty("environment");
        return env != null && env.equals("test") ? "http://localhost:3059" : "https://siftrics.com";
    }
}
