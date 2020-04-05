package com.hydra.client;

import com.hydra.error.HydraException;
import com.hydra.model.HydraResponse;

public interface HydraClient {
    HydraResponse recognize(String ... filePaths) throws HydraException;
}

