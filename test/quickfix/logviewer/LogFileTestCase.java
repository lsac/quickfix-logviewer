/****************************************************************************
 ** Copyright (c) quickfixengine.org  All rights reserved.
 **
 ** This file is part of the QuickFIX FIX Engine
 **
 ** This file may be distributed under the terms of the quickfixengine.org
 ** license as defined by quickfixengine.org and appearing in the file
 ** LICENSE included in the packaging of this file.
 **
 ** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 ** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 **
 ** See http://www.quickfixengine.org/LICENSE for licensing information.
 **
 ** Contact ask@quickfixengine.org if any conditions of this licensing are
 ** not clear to you.
 **
 ****************************************************************************/

package quickfix.logviewer;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import quickfix.DataDictionary;
import quickfix.logviewer.Helper;
import quickfix.logviewer.LogFile;
import junit.framework.TestCase;

import static org.junit.Assert.*;
import static quickfix.logviewer.Helper.getFileFromResourceAsStream;

public class LogFileTestCase {

    private static Logger LOG = LogManager.getLogger();
    private DataDictionary dataDictionary = null;
    LogFile logFile, b4b, testDel, testMultiDel;


    @Before
    public void setUp() throws Exception {
        logFile = new LogFile(getFileFromResourceAsStream(this,"test.log"), dataDictionary);
        dataDictionary = new DataDictionary("lib/FIX44.xml");
        b4b=new LogFile(getFileFromResourceAsStream(this,"testB4B.log"), dataDictionary);
        testDel = new LogFile(getFileFromResourceAsStream(this,"testDelimiter.log"), dataDictionary);
        testMultiDel = new LogFile(getFileFromResourceAsStream(this,"testMultiCharDelimiter.log"), dataDictionary);
    }


    @Test
    public void testLoadFile() throws Exception {
        Date dt=null;
        ArrayList messages = logFile.parseMessages(null, dt, dt);
        assertEquals(22, messages.size());
        assertEquals(0, logFile.getInvalidMessages().size());

        messages = logFile.parseMessages(null, dt, dt);
        assertEquals(22, messages.size());
        assertEquals(0, logFile.getInvalidMessages().size());
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.set(2004, 4, 11, 0, 0, 0);
        LocalDateTime startDate = Helper.getLocalDateTime(calendar);
        messages = logFile.parseMessages(null, startDate, null);
        assertEquals(22, messages.size());
        assertEquals(0, logFile.getInvalidMessages().size());

        calendar.set(2004, 4, 15, 0, 0, 0);
        LocalDateTime endDate = Helper.getLocalDateTime(calendar);
        messages = logFile.parseMessages(null, null, endDate);
        assertEquals(0, messages.size());
        assertEquals(0, logFile.getInvalidMessages().size());

        messages = logFile.parseMessages(null, startDate, endDate);
        assertEquals(0, messages.size());
        assertEquals(0, logFile.getInvalidMessages().size());
    }

    @Test
    public void testSkipLine() {
        assertFalse(LogFile.skipLine("#*@JDUJ@UI", LogFile.TYPE_PLAIN));
        assertFalse(LogFile.skipLine("8=FIX.4.1", LogFile.TYPE_PLAIN));

        assertFalse(LogFile.skipLine("20040206-14:47:33 Recv: 8=FIX.4.1", LogFile.TYPE_B4B));
        assertTrue(LogFile.skipLine("20040206-14:47:33 Driver Inbound: 8=FIX.4.1", LogFile.TYPE_B4B));
        assertTrue(LogFile.skipLine("20040206-14:47:33 Driver Outbound: 8=FIX.4.1", LogFile.TYPE_B4B));
        assertFalse(LogFile.skipLine("8=FIX.4.1", LogFile.TYPE_B4B));
    }
    @Test
    public void testGetDelimiter() {
        assertEquals('\001', LogFile.getDelimiter("8=FIX.4.2\001"));
        assertEquals('|', LogFile.getDelimiter("8=FIX.4.2|"));
        assertEquals(0, LogFile.getDelimiter("8=FIX.4.2"));
    }
    @Test
    public void testDetermineType() throws Exception {
        assertEquals(LogFile.TYPE_PLAIN, logFile.getType());

        assertEquals(LogFile.TYPE_B4B, b4b.getType());
    }

    @Test
    public void testDetermineDelimiter() throws Exception {
        Date dt=null;
        ArrayList messages = logFile.parseMessages(null, dt, dt);
        assertEquals(22, messages.size());
        assertEquals(0, logFile.getInvalidMessages().size());
        assertEquals('\001', logFile.getDelimiter());

        messages = testDel.parseMessages(null, dt, dt);
        assertEquals(20, messages.size());
        assertEquals(0, testDel.getInvalidMessages().size());
        assertEquals('|', testDel.getDelimiter());

        messages = testMultiDel.parseMessages(null, dt, dt);
        assertEquals(20, messages.size());
        assertEquals(0, testMultiDel.getInvalidMessages().size());
        assertEquals('^', testMultiDel.getDelimiter());
    }

    @Test
    public void testGetStartTime() throws Exception {
        Date time = logFile.getStartTime();
        assertEquals("Sat May 01 13:26:51 GMT 2004", time.toString());
    }

    @Test
    public void testGetEndTime() throws Exception {
        Date time = logFile.getEndTime();
        assertEquals("Wed May 19 14:27:52 GMT 2004", time.toString());
    }
}
