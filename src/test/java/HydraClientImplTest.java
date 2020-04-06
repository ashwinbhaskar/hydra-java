import com.hydra.client.FileContentReader;
import com.hydra.client.HydraClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydra.error.HydraException;
import io.javalin.Javalin;
import com.hydra.model.HydraResponse;
import com.hydra.model.Row;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class HydraClientImplTest {
    private String validAuthorizationKey = "12345678-1234-1234-1234-123412341234";
    private String validDataSourceId = "my_data_source";
    private ObjectMapper objectMapper = new ObjectMapper();
    private Javalin app = Javalin.create();

    @BeforeClass
    public static void beforeAll() {
        System.setProperty("environment","test");
    }

    @AfterClass
    public static void afterAll() {
        System.setProperty("environment","");
    }

    @Before
    public void beforeTest() {
        app.start(3059);
    }

    @After
    public void afterText() {
       app.stop();
    }

    @Test
    public void filePathsEmptyOrNullTest() throws Exception {
        String[] files = {};
        FileContentReader fileContentReader = Mockito.mock(FileContentReader.class);
        when(fileContentReader.fileToBase64(Mockito.any())).thenReturn("boooozz");
        HydraClientImpl hydraClient = new HydraClientImpl(validAuthorizationKey, validDataSourceId, fileContentReader);

        //Empty files
        try {
            hydraClient.recognize(files);
            fail("Should not pass");
        } catch (HydraException e) {
            assertEquals("Files paths cannot be empty or null", e.getMessage());
        }
        verify(fileContentReader, times(0)).fileToBase64(Mockito.any());

        //Null files
        try {
            hydraClient.recognize();
            fail("Should not pass");
        } catch (HydraException e) {
            assertEquals("Files paths cannot be empty or null", e.getMessage());
        }
        verify(fileContentReader, times(0)).fileToBase64(Mockito.any());

    }

    @Test
    public void filePathsUnsupportedExtensionsTest() throws Exception {
        String[] files = {"users/foo.bmp", "users/bax.exe"};
        FileContentReader fileContentReader = Mockito.mock(FileContentReader.class);
        when(fileContentReader.fileToBase64(Mockito.any())).thenReturn("boooozz");
        HydraClientImpl hydraClient = new HydraClientImpl(validAuthorizationKey, validDataSourceId, fileContentReader);

        try {
            hydraClient.recognize(files);
            fail("Should not reach here");
        } catch (HydraException e) {
            assertEquals("Only bmp, gif, png, jpeg and jpg files allowed", e.getMessage());
        }
        verify(fileContentReader, times(0)).fileToBase64(Mockito.any());
    }

    @Test
    public void shouldCallTheCorrectAPIAndGetResponseHappyPath() throws IOException, HydraException {
        Map<String, Object> expectedRecognizedText = new HashMap<String, Object>() {
            {
                put("field1", "value1");
                put("field2", "value2");
            }
        };
        Row expectedRow = new Row("", 0, expectedRecognizedText);
        List<Row> expectedRows = Collections.singletonList(expectedRow);
        HydraResponse expectedHydraResponse = new HydraResponse(expectedRows);

        FileContentReader fileContentReader = Mockito.mock(FileContentReader.class);
        when(fileContentReader.fileToBase64(Mockito.any())).thenReturn("qweretosdfsdf==");
        HydraClientImpl hydraClient = new HydraClientImpl(validAuthorizationKey, validDataSourceId, fileContentReader);

        app.post("/api/hydra/" + validDataSourceId, ctx -> {
            String auth = ctx.req.getHeader("Authorization");
            assertEquals("Basic "+validAuthorizationKey, auth);
            ctx.result(objectMapper.writeValueAsString(expectedHydraResponse));
        });
        String[] files = {"users/foo.bmp", "users/bax.pdf"};
        HydraResponse actualResponse = hydraClient.recognize(files);

        assertEquals(expectedHydraResponse, actualResponse);
        verify(fileContentReader, times(2)).fileToBase64(any());
    }

    @Test
    public void shouldThrowAHydraExceptionWhenServerReturns4XXErrorCode() throws IOException, HydraException {

        FileContentReader fileContentReader = Mockito.mock(FileContentReader.class);
        when(fileContentReader.fileToBase64(Mockito.any())).thenReturn("qweretosdfsdf==");
        HydraClientImpl hydraClient = new HydraClientImpl(validAuthorizationKey, validDataSourceId, fileContentReader);

        app.post("/api/hydra/" + validDataSourceId, ctx -> {
            String auth = ctx.req.getHeader("Authorization");
            assertEquals("Basic "+validAuthorizationKey, auth);
            ctx.status(401).result("Forbidden");
        });
        String[] files = {"users/foo.bmp", "users/bax.pdf"};
        try {
            HydraResponse actualResponse = hydraClient.recognize(files);
            fail("Should not happen");
        }catch (HydraException e) {
            assertEquals("Something went wrong.\n Status Code =  401", e.getMessage());
        }

        verify(fileContentReader, times(2)).fileToBase64(any());
    }

}
