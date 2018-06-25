import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Handles the writing of the csv file
 */
public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';

    /**
     * Writes a line of the resulting csv
     * @param w the <code>Writer</code> instance
     * @param values the values to write
     * @throws IOException
     */
    public static void writeLine(Writer w, List<Object> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    /**
     * Writes a line of the resulting csv with a custom separator
     * @param w the <code>Writer</code> instance
     * @param values the values to write
     * @param separators the separator to use
     * @throws IOException
     */
    public static void writeLine(Writer w, List<Object> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    private static String followCVSformat(Object value) {
        String result;
        if (value instanceof Integer)
            result = Integer.toString((Integer) value);
        else if (value instanceof Double)
            result = Double.toString((Double) value);
        else
            result = (String) value;

        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    /**
     * Writes a line of the resulting csv with a custom separator a custom quote
     * @param w the <code>Writer</code> instance
     * @param values the values to write
     * @param separators the separator to use
     * @param customQuote the custom quote
     * @throws IOException
     */
    public static void writeLine(Writer w, List<Object> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (Object value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }
}