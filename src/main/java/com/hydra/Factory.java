package com.hydra;

import com.hydra.client.FileContentReaderImpl;
import com.hydra.client.HydraClient;
import com.hydra.client.HydraClientImpl;

public class Factory {
    public static HydraClient newHydraClient(String apiKey, String dataSourceId) {
        return new HydraClientImpl(apiKey, dataSourceId, new FileContentReaderImpl());
    }
}
