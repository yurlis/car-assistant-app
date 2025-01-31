package yurlis.carassistantapp.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public interface CommonTimeMapper {
    default String formatPurchaseDate(Timestamp timestamp) {
        if (timestamp != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.format(timestamp);
        }
        return null;
    }
}
