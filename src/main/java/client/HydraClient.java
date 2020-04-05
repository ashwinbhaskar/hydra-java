package client;

import error.HydraException;
import model.HydraResponse;

public interface HydraClient {
    HydraResponse recognize(String ... filePaths) throws HydraException;
}

