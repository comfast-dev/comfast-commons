package dev.comfast.util;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TerminalGeneratorTest {
    final TerminalGenerator gen1 = new TerminalGenerator();
    final TerminalGenerator gen2 = new TerminalGenerator(" | ");
    final TerminalGenerator gen3 = new TerminalGenerator(" | ", "| ", " |");
    final List<String> HEADERS = List.of("H1", "H2", "H3");
    final List<List<String>> DATA = List.of(
        List.of("data1", "data2", "data3"),
        List.of("data4", "data5", "data6"),
        List.of("data7", "data9", "data9")
    );

    @Test void tableTest() {
        assertThat(gen1.table(HEADERS, DATA))
            .isEqualTo("H1    H2    H3\n" +
                       "data1 data2 data3\n" +
                       "data4 data5 data6\n" +
                       "data7 data9 data9");

        assertThat(gen2.table(HEADERS, DATA))
            .isEqualTo("H1    | H2    | H3\n" +
                       "data1 | data2 | data3\n" +
                       "data4 | data5 | data6\n" +
                       "data7 | data9 | data9");

        assertThat(gen3.table(HEADERS, DATA))
            .isEqualTo("| H1    | H2    | H3    |\n" +
                       "| data1 | data2 | data3 |\n" +
                       "| data4 | data5 | data6 |\n" +
                       "| data7 | data9 | data9 |");
    }


    @Test void horizontalTableTest() {
        assertThat(gen1.horizontalTable(HEADERS, DATA))
            .isEqualTo("H1: data1 data2 data3\n" +
                       "H2: data4 data5 data6\n" +
                       "H3: data7 data9 data9");

        assertThat(gen2.horizontalTable(HEADERS, DATA))
            .isEqualTo("H1: | data1 | data2 | data3\n" +
                       "H2: | data4 | data5 | data6\n" +
                       "H3: | data7 | data9 | data9");


        assertThat(gen3.horizontalTable(HEADERS, DATA))
            .isEqualTo("| H1: | data1 | data2 | data3 |\n" +
                       "| H2: | data4 | data5 | data6 |\n" +
                       "| H3: | data7 | data9 | data9 |");

    }
}