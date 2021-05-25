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

import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import quickfix.DataDictionary;
import quickfix.logviewer.FieldFilter;
import quickfix.logviewer.LogFile;
import quickfix.logviewer.MessagesTableModel;
import quickfix.logviewer.ProgressBar;
import quickfix.logviewer.ProgressBarPanel;
import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;
import static quickfix.logviewer.Helper.getFileFromResourceAsStream;

public class MessagesTableModelTestCase  {
    private static Logger LOG = LogManager.getLogger();

    private DataDictionary dataDictionary = null;
    private ProgressBarPanel progressBar = null;
    LogFile logFile, b4b, testDel, testMultiDel;


    @Before
    public void setUp() throws Exception {

        dataDictionary = new DataDictionary("lib/FIX44.xml");
        progressBar = new ProgressBarPanel(new ProgressBar());
        logFile = new LogFile(getFileFromResourceAsStream(this,"test.log"), dataDictionary);

    }

    @Test
    public void testSetMessages() throws Exception {
        Date dt = null;
        ArrayList messages = logFile.parseMessages(progressBar, dt, dt);
        MessagesTableModel tableModel = new MessagesTableModel(dataDictionary);
        tableModel.setMessages(messages, progressBar);
        LOG.info("{}", tableModel);
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("index {}  = {}", i, tableModel.getColumnName(i));
        }
        assertEquals("MsgSeqNum(34)", tableModel.getColumnName(2));
        assertEquals("BodyLength(9)", tableModel.getColumnName(1));
        assertEquals("BeginString(8)", tableModel.getColumnName(0));
        assertEquals("MsgType(35)", tableModel.getColumnName(3));
        assertEquals("Symbol(55)", tableModel.getColumnName(22));
        assertEquals("CheckSum(10)", tableModel.getColumnName(34));
        LOG.info("{}", tableModel.getMessageAt(0));
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("{} = {}", i, tableModel.getValueAt(0, i));
        }

        assertEquals("1", tableModel.getValueAt(0, 2));
        assertEquals("100", tableModel.getValueAt(0, 1));
        assertEquals("FIX.4.2", tableModel.getValueAt(0, 0));
        assertEquals("", tableModel.getValueAt(0, 10));
        assertEquals("000", tableModel.getValueAt(0, 34));
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("{} = {}", i, tableModel.getValueAt(4, i));
        }
        assertEquals("5", tableModel.getValueAt(4, 2));
        assertEquals("100", tableModel.getValueAt(4, 1));
        assertEquals("FIX.4.2", tableModel.getValueAt(4, 0));
        assertEquals("DELL", tableModel.getValueAt(4, 22));
        assertEquals("000", tableModel.getValueAt(4, 34));
    }

    @Test
    public void testFilter() throws Exception {
        Date dt= null;
        ArrayList messages = logFile.parseMessages(progressBar, dt, dt);
        MessagesTableModel tableModel = new MessagesTableModel(dataDictionary);
        tableModel.setMessages(messages, progressBar);

        ArrayList filter = new ArrayList();
        filter.add(new FieldFilter(new quickfix.StringField(9, "52"), FieldFilter.EQUAL));
        tableModel.filter(filter);
        if (tableModel.getMessages().size() > 0) {
            assertEquals("52", tableModel.getValueAt(0, 1));
            assertEquals("52", tableModel.getValueAt(1, 1));
            assertEquals("52", tableModel.getValueAt(2, 1));
            assertEquals("52", tableModel.getValueAt(3, 1));
            assertEquals("52", tableModel.getValueAt(4, 1));
        }


        filter = new ArrayList();
        filter.add(new FieldFilter(new quickfix.StringField(55, "DELL"), FieldFilter.EQUAL));
        tableModel.filter(filter);
        LOG.info("{}", tableModel);
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("{} = {}", i, tableModel.getValueAt(0, i));
        }

        assertEquals("DELL", tableModel.getValueAt(0, 22));
        assertEquals("NewOrderSingle (D)", tableModel.getValueAt(0, 3));
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("{} = {}", i, tableModel.getValueAt(1, i));
        }
        assertEquals("DELL", tableModel.getValueAt(1, 22));
        assertEquals("NewOrderSingle (D)", tableModel.getValueAt(1, 3));
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("{} = {}", i, tableModel.getValueAt(2, i));
        }
        assertEquals("DELL", tableModel.getValueAt(2, 22));
        assertEquals("NewOrderSingle (D)", tableModel.getValueAt(2, 3));
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("{} = {}", i, tableModel.getValueAt(3, i));
        }
        assertEquals("DELL", tableModel.getValueAt(3, 22));
        assertEquals("OrderCancelRequest (F)", tableModel.getValueAt(3, 3));
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            LOG.info("{} = {}", i, tableModel.getValueAt(4, i));
        }
        assertEquals("DELL", tableModel.getValueAt(4, 22));
        assertEquals("ExecutionReport (8)", tableModel.getValueAt(4, 3));

        filter.add(new FieldFilter(new quickfix.StringField(35, "8"), FieldFilter.EQUAL));
        tableModel.filter(filter);
        LOG.info("{}", tableModel);
        assertEquals("DELL", tableModel.getValueAt(0, 22));
        assertEquals("ExecutionReport (8)", tableModel.getValueAt(0, 3));
        assertEquals("DELL", tableModel.getValueAt(1, 22));
        assertEquals("ExecutionReport (8)", tableModel.getValueAt(1, 3));
        assertEquals("DELL", tableModel.getValueAt(2, 22));
        assertEquals("ExecutionReport (8)", tableModel.getValueAt(2, 3));
        assertEquals("DELL", tableModel.getValueAt(3, 22));
        assertEquals("ExecutionReport (8)", tableModel.getValueAt(3, 3));
        assertEquals("DELL", tableModel.getValueAt(4, 22));
        assertEquals("ExecutionReport (8)", tableModel.getValueAt(4, 3));

//        filter.clear();
        filter.add(new FieldFilter(new quickfix.StringField(10, "252"), FieldFilter.EQUAL));
//        tableModel.setMessages();
        tableModel.filter(filter);
        if (tableModel.getMessages().size() > 0) {
            LOG.info("10=253 is {}", tableModel.getMessages());

            assertEquals("DELL", tableModel.getValueAt(0, 22));
            assertEquals("ExecutionReport (8)", tableModel.getValueAt(0, 3));
            assertEquals("148", tableModel.getValueAt(0, 1));
        }


    }
}
