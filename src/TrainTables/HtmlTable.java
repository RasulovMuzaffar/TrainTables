package TrainTables;

import java.util.LinkedList;
import java.util.List;

public class HtmlTable {

    public void advanceToNextRow() {
        currentRow = new TableRow();
        tableData.add(currentRow);
    }

    public void markCurrentRowAsHeader() {
        currentRow.type = RowType.Header;
    }

    public void markCurrentRowAsDocHeader() {
        currentRow.type = RowType.DocHeader;
    }

    public void addCell(String value) {
        if (currentRow == null) {
            advanceToNextRow();
        }

        currentRow.cells.add(value);
    }

    public String generateHtmlTable() {
        String result = "";//HTML_OPEN + BODY_OPEN;
        result += TABLE_OPEN;
        for (TableRow row : tableData) {
            if (!row.cells.isEmpty() && !row.type.name().equals("DocHeader")) {
                result += ROW_OPEN;
                for (String value : row.cells) {
                    if (row.type == RowType.Regular) {
                        result += CELL_OPEN;
                        result += value;
                        result += CELL_CLOSE;
                    } else if (row.type == RowType.Header) {
                        result += HEADER_CELL_OPEN;
                        result += value;
                        result += HEADER_CELL_CLOSE;
                    }
                }
                result += ROW_CLOSE;
            }
        }
        result += TABLE_CLOSE;
//        result += (TABLE_CLOSE + BODY_CLOSE + HTML_CLOSE);
        return result;
    }

    public String generateHtml() {
        String result = HTML_OPEN + BODY_OPEN;
        for (TableRow row : tableData) {
            for (String value : row.cells) {
                if (row.type == RowType.DocHeader) {
                    result += value + " ";
                }
            }
        }
        result += "\n";
        result += generateHtmlTable();
        result += (BODY_CLOSE + HTML_CLOSE);
        return result;
    }

    private List<TableRow> tableData = new LinkedList<>();
    private TableRow currentRow = null;
    private static final String HTML_OPEN = "<html>\n";
    private static final String HTML_CLOSE = "</html>";
    private static final String BODY_OPEN = "<body>\n";
    private static final String BODY_CLOSE = "</body>\n";
    private static final String TABLE_OPEN = "<table>\n";
    private static final String TABLE_CLOSE = "</table>\n";
    private static final String HEADER_CELL_OPEN = "<th>";
    private static final String HEADER_CELL_CLOSE = "</th>";
    private static final String ROW_OPEN = "<tr>";
    private static final String ROW_CLOSE = "</tr>\n";
    private static final String CELL_OPEN = "<td>";
    private static final String CELL_CLOSE = "</td>";
}

enum RowType {
    DocHeader,
    Header,
    Regular
}

class TableRow {

    public List<String> cells = new LinkedList<>();
    public RowType type = RowType.Regular;
}
