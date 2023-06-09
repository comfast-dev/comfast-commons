package dev.comfast.config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.comfast.experimental.config.MapFlatter;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MapFlatterTest {
    final MapFlatter flatter = new MapFlatter();
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    final String testYaml =
       " orderNo: A001\n" +
       " date: 2019-04-17\n" +
       " customerName: Customer, Joe\n" +
       " nested:\n" +
       "   object:\n" +
       "     dot.notated:\n" +
       "       list: [1, 2, 3]\n" +
       "       value: value\n" +
       " orderLines:\n" +
       "     - item: No. 9 Sprockets\n" +
       "       quantity: 12\n" +
       "       unitPrice: 1.23\n" +
       "     - item: Widget (10mm)\n" +
       "       quantity: 4\n" +
       "       unitPrice: 3.45\n";

    @Test void testFlat() throws JsonProcessingException {
        var inputMap = mapper.readValue(testYaml, Map.class);
        var result = flatter.flat(inputMap);

        assertEquals(
            "customerName = Customer, Joe\n" +
            "date = 2019-04-17\n" +
            "nested.object.dot.notated.list[0] = 1\n" +
            "nested.object.dot.notated.list[1] = 2\n" +
            "nested.object.dot.notated.list[2] = 3\n" +
            "nested.object.dot.notated.value = value\n" +
            "orderLines[0].item = No. 9 Sprockets\n" +
            "orderLines[0].quantity = 12\n" +
            "orderLines[0].unitPrice = 1.23\n" +
            "orderLines[1].item = Widget (10mm)\n" +
            "orderLines[1].quantity = 4\n" +
            "orderLines[1].unitPrice = 3.45\n" +
            "orderNo = A001"
            , mapToString(result));
    }

    private static String mapToString(Map<String, String> flatMap) {
        return flatMap.entrySet().stream().map(e -> e.getKey() + " = " + e.getValue())
            .collect(Collectors.joining("\n"));
    }
}