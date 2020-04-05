package com.hydra.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileContent {
    @JsonProperty("mimeType")
    private String mimeType;
    @JsonProperty("base64File")
    private String base64File;

    public FileContent(String mimeType, String base64File) {
        this.mimeType = mimeType;
        this.base64File = base64File;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getBase64File() {
        return base64File;
    }
}
