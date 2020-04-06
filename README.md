# hydra-java ![Java CI with Gradle](https://github.com/ashwinbhaskar/hydra-java/workflows/Java%20CI%20with%20Gradle/badge.svg)  [![codecov](https://codecov.io/gh/ashwinbhaskar/hydra-java/branch/master/graph/badge.svg)](https://codecov.io/gh/ashwinbhaskar/hydra-java)

Java client for [Hydra](https://siftrics.com/docs/hydra.html) APIs

# Usage

#### Java
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

#### Scala

```
import com.hydra.Factory
import com.hydra.model.HydraResponse
import scala.util.{Failure, Success, Try}

val client = Factory.newHydraClient("api-key", "data-source-id")
val files = Seq("/users/johndoe/invoice.pdf")

val response: Try[HydraResponse] = Try(client.recognize(files:_*))
response match {
  case Success(response) => //do something
  case Failure(exception) => //do something
}
```

#### Clojure

```
(defn recognize []
  (let [api-key "api-key"
        data-source "data-source-id"
        client (Factory/newHydraClient api-key data-source)
        files ["/Users/johndor/invoice.pdf"]]
    (try
      (.recognize client (into-array files))
      (catch HydraException e
        e))))
```
