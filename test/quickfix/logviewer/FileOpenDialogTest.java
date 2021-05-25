package quickfix.logviewer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class FileOpenDialogTest {
    private static Logger LOG = LogManager.getLogger();

    @Test
    public void roundDate() throws ParseException {
        Date date2;

        SimpleDateFormat format = new SimpleDateFormat( "yyyyMMdd-HH:mm:ss.SSS, z");
        //52=20040502-13:27:00.000
        String tm = "20040502-13:27:01.666, GMT";
        date2 = format.parse(tm);
        LOG.info("{}", date2);

        Date date;
        date = FileOpenDialog.roundDate(date2, false);
        LOG.info("{} rounds up to {}", tm, date);
        date2 = format.parse(tm);
        LOG.info("{}", date2);
        assertEquals("Sun May 02 14:00:00 GMT 2004", date.toString());

        date = FileOpenDialog.roundDate(date2, true);
        LOG.info("{} rounds down to {}", tm, date);
        assertEquals("Sun May 02 13:00:00 GMT 2004", date.toString());
    }
}