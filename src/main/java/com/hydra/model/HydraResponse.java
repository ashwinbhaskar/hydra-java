package com.hydra.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HydraResponse {
    @JsonProperty("Rows")
    private List<Row> rows;

    public HydraResponse() {
    }

    public HydraResponse(List<Row> rows) {
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "HydraResponse{" +
                "rows=" + rows +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HydraResponse that = (HydraResponse) o;
        return Objects.equals(rows, that.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }
}
