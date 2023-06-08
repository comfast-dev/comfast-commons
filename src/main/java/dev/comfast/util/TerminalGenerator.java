package dev.comfast.util;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dev.comfast.util.Utils.fixedLength;
import static dev.comfast.util.Utils.transposeMatrix;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Utility class that generates ASCII tables
 */
@RequiredArgsConstructor
public class TerminalGenerator {
    private final String separator, linePrefix, lineSuffix;

    public TerminalGenerator() {
        this(" ", "", "");
    }

    public TerminalGenerator(String separator) {
        this(separator, "", "");
    }

    /**
     * Generates table like: <pre>{@code
     * H1    H2     H3
     * data1 data2 data3
     * data4 data5 data6
     * data7 data9 data9
     * ...
     * }
     */
    public String table(List<String> columnHeaders, List<List<String>> dataRows) {
        if(dataRows.size() == 0) throw new IllegalArgumentException("Empty data passed");
        if(dataRows.get(0).size() != columnHeaders.size())
            throw new IllegalArgumentException("Data rows count should match headers count");

        var allRows = new ArrayList<>(dataRows);
        allRows.add(0, columnHeaders);
        return printColumns(transposeMatrix(allRows));
    }

    /**
     * Generates table like:<pre>{@code
     * H1 data1 data2 data3 ...
     * H2 data4 data5 data6 ...
     * H3 data7 data9 data9 ...
     * }
     */
    public String horizontalTable(List<String> rowHeaders, List<List<String>> dataRows) {
        if(rowHeaders.size() != dataRows.size())
            throw new IllegalArgumentException("Headers count should match data count.");
        if(dataRows.size() == 0) throw new IllegalArgumentException("Empty data passed");

        var allColumns = new ArrayList<>(transposeMatrix(dataRows));
        allColumns.add(0, rowHeaders.stream().map(h -> h + ":").collect(toList()));

        return printColumns(allColumns);
    }

    private String printColumns(List<List<String>> columns) {
        var alignedColumns = columns.stream().map(this::alignSpaces).collect(toList());

        return transposeMatrix(alignedColumns).stream()
            .map(this::joinLine)
            .collect(joining("\n"));
    }

    @NotNull private String joinLine(List<String> line) {
        return (linePrefix + join(separator, line) + lineSuffix).trim();
    }

    /**
     * Adds spaces to the end of each string to keep all to have same length.
     */
    private List<String> alignSpaces(List<String> strings) {
        final int MAX_LENGTH = strings.stream().mapToInt(String::length).max().orElseThrow();
        return strings.stream()
            .map(s -> fixedLength(s, MAX_LENGTH))
            .collect(toList());
    }
}