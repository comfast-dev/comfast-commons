package dev.comfast.experimental.config;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapFlatter {
    /**
     * @param map multi-level map
     * @return single level map, where:
     * - keys are dot.notated.strings
     * - values are strings
     * - lists are represented by [i] notation eg. obj.list[2].nested
     * eg. convert this:
     * {obj:{a: 1, b: {c: "xd"}, list: [7, 8, 9]}, other: "lol" }
     * into this: {
     * obj.a: "1",
     * obj.b.c: "xd",
     * obj.list[0]: "7",
     * obj.list[1]: "8",
     * obj.list[2]: "9",
     * other: "lol"
     * }
     */
    public Map<String, String> flat(Map<?, ?> map) {
        var result = map.entrySet().stream()
            .flatMap(e -> flatten(e, ""))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().toString()
            ));
        return new TreeMap<>(result); // TreeMap to keep sorted
    }

    private Stream<Map.Entry<String, ?>> flatten(Map.Entry<?, ?> e, String parentKey) {
        final String key = parentKey.isEmpty()
                           ? e.getKey().toString()
                           : parentKey + "." + e.getKey();
        if(e.getValue() instanceof Map<?, ?>) {
            var m = (Map<?, ?>)e.getValue();
            return m.entrySet().stream().flatMap(en -> flatten(en, key));
        }
        else if(e.getValue() instanceof List<?>) {
            var l = (List<?>)e.getValue();
            return IntStream.range(0, l.size())
                .mapToObj(i -> new AbstractMap.SimpleEntry<>(key + "[" + i + "]", l.get(i)))
                .flatMap(en -> flatten(en, ""));
        }

        return Stream.of(new AbstractMap.SimpleEntry<>(key, e.getValue()));
    }
}
