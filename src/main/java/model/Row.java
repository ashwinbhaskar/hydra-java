package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Row {
    @JsonProperty("Error")
    private String error;
    @JsonProperty("FileIndex")
    private int fileIndex;
    @JsonProperty("RecognizedText")
    private Map<String, String> recognizedText;

    public Row() {
    }

    public Row(String error, int fileIndex, Map<String, String> recognizedText) {
        this.error = error;
        this.fileIndex = fileIndex;
        this.recognizedText = recognizedText;
    }

    public String getError() {
        return error;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public Map<String ,String> getRecognizedText() {
        return recognizedText;
    }

    @Override
    public String toString() {
        return "Row{" +
                "error='" + error + '\'' +
                ", fileIndex=" + fileIndex +
                ", recognizedText=" + recognizedText +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return fileIndex == row.fileIndex &&
                Objects.equals(error, row.error) &&
                Objects.equals(recognizedText, row.recognizedText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, fileIndex, recognizedText);
    }
}
