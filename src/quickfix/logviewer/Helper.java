package quickfix.logviewer;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class Helper {
    private Helper(){}
    public static File getFileFromResourceAsStream(Object obj, String fileName) throws URISyntaxException {

        ClassLoader classLoader = obj.getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }

        //File file = new File(resource.getFile());
        File file = new File(resource.toURI());
        return file;
    }

    public static Date getDateTime(LocalDateTime localDateTime) {

        return localDateTime==null? null:Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDateTime getLocalDateTime(Calendar calendar){

        return  LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
    }

    public static LocalDateTime getUtcDateTime(long ms) {
        Instant dt = Instant.ofEpochMilli(ms);
        return LocalDateTime.ofInstant(dt, ZoneOffset.UTC);
    }
    public static LocalDateTime getUtcDateTime(Date dt) {
        return getUtcDateTime(dt.getTime());
    }

    public static LocalDate getLocalDate(Calendar calendar){

        return  LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
    }
}
