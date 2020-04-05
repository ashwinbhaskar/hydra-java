import client.HydraClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import error.HydraException;
import model.HydraResponse;
import model.Row;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class IntegrationTest {
    private static String apiKey = System.getenv("API_KEY");
    private static String dataSourceId = System.getenv("DATA_SOURCE_ID");

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void invoicePDF1Test() throws URISyntaxException, JsonProcessingException {
        HydraClient client = Factory.newHydraClient(apiKey, dataSourceId);
        URL resourceUrl = getClass().getClassLoader().getResource("Helm_Avionics_Invoice_1.pdf");
        File file = Paths.get(resourceUrl.toURI()).toFile();
        String[] filePaths = {file.getAbsolutePath()};

        try {
            HydraResponse hydraResponse = client.recognize(filePaths);
            assertEquals(1, hydraResponse.getRows().size());
            Row row = hydraResponse.getRows().get(0);
            assertEquals("", row.getError());
            assertEquals(0, row.getFileIndex());
            Map<String ,Object> recognizedText = row.getRecognizedText();

            collector.checkThat("Erikson Airline Corporation", equalTo(recognizedText.get("Customer")));
            collector.checkThat("777 Erikson Way\nNorway, IA 52318", equalTo(recognizedText.get("Address")));
            collector.checkThat("2020/04/20", equalTo(recognizedText.get("Expiry Date")));
            collector.checkThat("2020/03/25", equalTo(recognizedText.get("Date Issued")));

            List<Map<String, String>> items = (List<Map<String ,String>>) recognizedText.get("Purchased Items");

            collector.checkThat(3, equalTo(items.size()));
            Map<String, String> item1 = new HashMap<String, String>() {{
                put("Item", "In-flight entertainment system");
                put("Price", "$500");
                put("Quantity", "250");
                put("Total", "$125,000");
            }};
            Map<String, String> item2 = new HashMap<String, String>(){{
                put("Item", "Cockpit Windshield HUD");
                put("Price", "$5,000");
                put("Quantity", "10");
                put("Total", "$50,000");
            }};
            Map<String, String> item3 = new HashMap<String, String>(){{
                put("Item", "Cockpit Displays");
                put("Price", "$2,000");
                put("Quantity", "15");
                put("Total", "$30,000");
            }};
            collector.checkThat(true, equalTo(itemsContains(items, item1)));
            collector.checkThat(true, equalTo(itemsContains(items, item2)));
            collector.checkThat(true, equalTo(itemsContains(items, item3)));

            collector.checkThat("$205,000", equalTo(recognizedText.get("Total Amount Owed")));
        } catch(HydraException e) {
            fail("Failed with exception " + e.getMessage());
        }
    }

    @Test
    public void invoiceJPEG1Test() throws URISyntaxException, JsonProcessingException {
        HydraClient client = Factory.newHydraClient(apiKey, dataSourceId);
        URL resourceUrl = getClass().getClassLoader().getResource("helm-1.jpg");
        File file = Paths.get(resourceUrl.toURI()).toFile();
        String[] filePaths = {file.getAbsolutePath()};

        try {
            HydraResponse hydraResponse = client.recognize(filePaths);
            assertEquals(1, hydraResponse.getRows().size());
            Row row = hydraResponse.getRows().get(0);
            assertEquals("", row.getError());
            assertEquals(0, row.getFileIndex());
            Map<String ,Object> recognizedText = row.getRecognizedText();

            collector.checkThat("Erikson Airline Corporation", equalTo(recognizedText.get("Customer")));
            collector.checkThat("777 Erikson Way\nNorway, IA 52318", equalTo(recognizedText.get("Address")));
            collector.checkThat("2020/04/20", equalTo(recognizedText.get("Expiry Date")));
            collector.checkThat("2020/03/25", equalTo(recognizedText.get("Date Issued")));

            List<Map<String, String>> items = (List<Map<String ,String>>) recognizedText.get("Purchased Items");

            collector.checkThat(3, equalTo(items.size()));
            Map<String, String> item1 = new HashMap<String, String>() {{
                put("Item", "In-flight entertainment system");
                put("Price", "$500");
                put("Quantity", "250");
                put("Total", "$125,000");
            }};
            Map<String, String> item2 = new HashMap<String, String>(){{
                put("Item", "Cockpit Windshield HUD");
                put("Price", "$5,000");
                put("Quantity", "10");
                put("Total", "$50,000");
            }};
            Map<String, String> item3 = new HashMap<String, String>(){{
                put("Item", "Cockpit Displays");
                put("Price", "$2,000");
                put("Quantity", "15");
                put("Total", "$30,000");
            }};
            collector.checkThat(true, equalTo(itemsContains(items, item1)));
            collector.checkThat(true, equalTo(itemsContains(items, item2)));
            collector.checkThat(true, equalTo(itemsContains(items, item3)));

            collector.checkThat("$205,000", equalTo(recognizedText.get("Total Amount Owed")));
        } catch(HydraException e) {
            fail("Failed with exception " + e.getMessage());
        }
    }

    @Test
    public void upsideDown1Test() throws URISyntaxException, JsonProcessingException {
        HydraClient client = Factory.newHydraClient(apiKey, dataSourceId);
        URL resourceUrl = getClass().getClassLoader().getResource("upside-down-1.png");
        File file = Paths.get(resourceUrl.toURI()).toFile();
        String[] filePaths = {file.getAbsolutePath()};

        try {
            HydraResponse hydraResponse = client.recognize(filePaths);
            assertEquals(1, hydraResponse.getRows().size());
            Row row = hydraResponse.getRows().get(0);
            assertEquals("", row.getError());
            assertEquals(0, row.getFileIndex());
            Map<String ,Object> recognizedText = row.getRecognizedText();

            collector.checkThat("Erikson Airline Corporation", equalTo(recognizedText.get("Customer")));
            collector.checkThat("777 Erikson Way\nNorway, IA 52318", equalTo(recognizedText.get("Address")));
            collector.checkThat("2020/04/20", equalTo(recognizedText.get("Expiry Date")));
            collector.checkThat("2020/03/25", equalTo(recognizedText.get("Date Issued")));

            List<Map<String, String>> items = (List<Map<String ,String>>) recognizedText.get("Purchased Items");

            collector.checkThat(3, equalTo(items.size()));
            Map<String, String> item1 = new HashMap<String, String>() {{
                put("Item", "In-flight entertainment system");
                put("Price", "$500");
                put("Quantity", "250");
                put("Total", "$125,000");
            }};
            Map<String, String> item2 = new HashMap<String, String>(){{
                put("Item", "Cockpit Windshield HUD");
                put("Price", "$5,000");
                put("Quantity", "10");
                put("Total", "$50,000");
            }};
            Map<String, String> item3 = new HashMap<String, String>(){{
                put("Item", "Cockpit Displays");
                put("Price", "$2,000");
                put("Quantity", "15");
                put("Total", "$30,000");
            }};
            collector.checkThat(true, equalTo(itemsContains(items, item1)));
            collector.checkThat(true, equalTo(itemsContains(items, item2)));
            collector.checkThat(true, equalTo(itemsContains(items, item3)));

            collector.checkThat("$205,000", equalTo(recognizedText.get("Total Amount Owed")));
        } catch(HydraException e) {
            fail("Failed with exception " + e.getMessage());
        }
    }

    private boolean itemsContains(List<Map<String, String>> items, Map<String, String> item) {
        return items.stream().anyMatch(it -> it.equals(item));
    }
}
