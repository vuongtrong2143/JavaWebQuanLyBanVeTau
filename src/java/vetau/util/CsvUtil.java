package vetau.util;

import java.util.List;

public final class CsvUtil {

    private CsvUtil() {
    }

    public static String toCsv(String[] headers, List<String[]> rows) {
        StringBuilder sb = new StringBuilder();

        /*
         * BOM UTF-8 để Excel đọc tiếng Việt không lỗi font.
         */
        sb.append('\uFEFF');

        appendRow(sb, headers);

        if (rows != null) {
            for (String[] row : rows) {
                appendRow(sb, row);
            }
        }

        return sb.toString();
    }

    private static void appendRow(StringBuilder sb, String[] values) {
        if (values == null) {
            sb.append("\r\n");
            return;
        }

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(",");
            }

            sb.append(escape(values[i]));
        }

        sb.append("\r\n");
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }

        String safe = value.replace("\"", "\"\"");

        return "\"" + safe + "\"";
    }

    public static String safeFileName(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "export.csv";
        }

        return raw.replaceAll("[^a-zA-Z0-9_\\-.]", "_");
    }
}