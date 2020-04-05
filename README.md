# hydra-java

Java client for [Hydra](https://siftrics.com/docs/hydra.html) APIs

# Usage

```
import com.hydra.Factory;
import com.hydra.client.HydraClient;
import com.hydra.error.HydraException;
import com.hydra.model.HydraResponse;

HydraClient hydraClient = Factory.newHydraClient("api-key", "data-source-id");
String[] files = {"/users/johndoe/invoice.pdf"};
try {
      HydraResponse response = hydraClient.recognize(files);
      //do something
} catch (HydraException e) {
    //do something
}
```

