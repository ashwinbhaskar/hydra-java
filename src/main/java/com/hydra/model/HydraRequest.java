package com.hydra.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HydraRequest {
    @JsonProperty("files")
    private ArrayList<FileContent> files;

    public HydraRequest(ArrayList<FileContent> files) {
        this.files = files;
    }

    public ArrayList<FileContent> getFiles() {
        return files;
    }
}

