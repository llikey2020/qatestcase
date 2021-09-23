package common.util;

import common.lib.metadata.FileMetadata;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataUtil {

    public static String getDatabaseName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase() + ".db";
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase() + ".tbl";
    }

    public static String getFilename(int index) {
        return "part-0000" + index
                + "-" + UUID.randomUUID()
                + ".c000.snappy.parquet";
    }

    public static FileMetadata getMockFileMetadata() {
        return null;
    }

    public static String getErrMsg(String input) {
        String str = getFirstLine(input);

        Pattern pattern = Pattern.compile("Metadata.API.*",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    private static String getFirstLine(String input) {
        if (null == input) return "";

        String[] lines = input.split("\n");
        return lines.length > 0 ?
                lines[0] : "";
    }

}
