import client.FileContentReaderImpl;
import client.HydraClient;
import client.HydraClientImpl;

public class Factory {
    public static HydraClient newHydraClient(String apiKey, String dataSourceId) {
        return new HydraClientImpl(apiKey, dataSourceId, new FileContentReaderImpl());
    }
}
