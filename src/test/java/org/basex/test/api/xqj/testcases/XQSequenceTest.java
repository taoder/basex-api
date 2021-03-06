// Copyright (c) 2003, 2006, 2007, 2008 Oracle. All rights reserved.
package org.basex.test.api.xqj.testcases;

import java.io.*;
import java.util.*;

import javax.xml.stream.*;
import javax.xml.transform.stream.*;
import javax.xml.xquery.*;

import org.basex.test.api.xqj.*;
import org.xml.sax.helpers.*;

@SuppressWarnings("all")
public class XQSequenceTest extends XQJTestCase {

  public void testAbsolute() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    boolean b = false;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.absolute(2);
      fail("A-XQS-1.1: SCROLLTYPE_FORWARD_ONLY sequence supports absolute()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.close();
    try {
      xqs.absolute(2);
      fail("A-XQS-1.2: closed sequence supports absolute()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.absolute(2);
    } catch (final XQException e) {
      fail("A-XQS-2.1: absolute() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-2.1: absolute(2) failed", b);
    assertEquals("A-XQS-2.1: absolute(2) failed", 2, xqs.getInt());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.absolute(5);
    } catch (final XQException e) {
      fail("A-XQS-2.4: absolute() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-2.4: absolute(5) failed", b);
    assertTrue("A-XQS-2.4: absolute(5) failed", xqs.isAfterLast());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.absolute(0);
    } catch (final XQException e) {
      fail("A-XQS-2.3: absolute() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-2.3: absolute(0) failed", b);
    assertTrue("A-XQS-2.3: absolute(0) failed", xqs.isBeforeFirst());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.absolute(-2);
    } catch (final XQException e) {
      fail("A-XQS-2.2: absolute() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-2.2: absolute(-2) failed", b);
    assertEquals("A-XQS-2.2: absolute(-2) failed", 3, xqs.getInt());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try{
      b = xqs.absolute(-5);
    } catch (final XQException e) {
      fail("A-XQS-2.4: absolute() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-2.4: absolute(-5) failed", b);
    assertTrue("A-XQS-2.4: absolute(-5) failed", xqs.isBeforeFirst());
    xqe.close();
  }

  public void testAfterLast() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.afterLast();
      fail("A-XQS-1.1: SCROLLTYPE_FORWARD_ONLY sequence supports afterLast()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.close();
    try {
      xqs.afterLast();
      fail("A-XQS-1.2: closed sequence supports afterLast()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.afterLast();
    } catch (final XQException e) {
      fail("A-XQS-3.1: afterLast() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-3.1: afterLast() failed", xqs.isAfterLast());
    xqe.close();
  }

  public void testBeforeFirst() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.beforeFirst();
      fail("A-XQS-1.1: SCROLLTYPE_FORWARD_ONLY sequence supports beforeFirst()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.close();
    try {
      xqs.beforeFirst();
      fail("A-XQS-1.2: closed sequence supports beforeFirst()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.beforeFirst();
    } catch (final XQException e) {
      fail("A-XQS-4.1: beforeFirst() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-4.1: beforeFirst() failed", xqs.isBeforeFirst());
    xqe.close();
  }

  public void testClose() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    XQItem xqi;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.next();
    xqi = xqs.getItem();

    try{
      xqs.close();
      xqs.close();
    } catch (final XQException e) {
      fail("A-XQS-5.1: close() failed with message: " + e.getMessage());
    }

    assertTrue("A-XQS-5.1: close() doesn't close sequence", xqs.isClosed());
    assertTrue("A-XQS-5.2: close() doesn't close items obtained from the sequence", xqi.isClosed());

    xqe.close();
  }

  public void testIsClosed() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");

    assertFalse("A-XQS-6.1: isClosed() on open sequence", xqs.isClosed());
    xqs.close();
    assertTrue("A-XQS-6.2: isClosed() on closed sequence", xqs.isClosed());

    xqe.close();
  }

  public void testCount() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.count();
      fail("A-XQS-7.1: SCROLLTYPE_FORWARD_ONLY sequence supports count()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.close();
    try {
      xqs.count();
      fail("A-XQS-1.2: closed sequence supports count()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      assertEquals("A-XQS-7.2: count() failed", xqs.count(), 4);
    } catch (final XQException e) {
      fail("A-XQS-7.2: count() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testGetPosition() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      xqs.getPosition();
      fail("A-XQS-8.1: SCROLLTYPE_FORWARD_ONLY sequence supports getPosition()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    xqs.close();
    try {
      xqs.getPosition();
      fail("A-XQS-1.2: closed sequence supports getPosition()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try{
      assertEquals("A-XQS-8.3: getPosition() failed when before first item", 0, xqs.getPosition());
    } catch (final XQException e) {
      fail("A-XQS-8.3: getPosition() failed with message: " + e.getMessage());
    }
    xqs.next();
    try{
      assertEquals("A-XQS-8.2: getPosition() failed when on item", 1, xqs.getPosition());
    } catch (final XQException e) {
      fail("A-XQS-8.2: getPosition() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertEquals("A-XQS-8.4: getPosition() failed when after last item", 2, xqs.getPosition());
    } catch (final XQException e) {
      fail("A-XQS-8.4: getPosition() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testIsOnItem() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    xqs.close();
    try {
      xqs.isOnItem();
      fail("A-XQS-1.2: closed sequence supports isOnItem()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      assertFalse("A-XQS-9.2: isOnItem() failed when before first item", xqs.isOnItem());
    } catch (final XQException e) {
      fail("A-XQS-9.2: isOnItem() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertTrue("A-XQS-9.1: isOnItem() failed when on item", xqs.isOnItem());
    } catch (final XQException e) {
      fail("A-XQS-9.1: isOnItem() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertFalse("A-XQS-9.2: isOnItem() failed when after last item", xqs.isOnItem());
    } catch (final XQException e) {
      fail("A-XQS-9.2: isOnItem() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testIsScrollable() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    xqs.close();
    try {
      xqs.isScrollable();
      fail("A-XQS-1.2: closed sequence supports isScrollable()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      assertFalse("A-XQS-10.2: isScrollable() failed for SCROLLTYPE_FORWARD_ONLY sequence", xqs.isScrollable());
    } catch (final XQException e) {
      fail("A-XQS-10.2: isScrollable() failed with message: " + e.getMessage());
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      assertTrue("A-XQS-10.1: isScrollable() failed for SCROLLTYPE_SCROLLABLE sequence", xqs.isScrollable());
    } catch (final XQException e) {
      fail("A-XQS-10.1: isScrollable() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testFirst() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    boolean b = false;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.first();
      fail("A-XQS-1.1: SCROLLTYPE_FORWARD_ONLY sequence supports first()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.close();
    try {
      xqs.first();
      fail("A-XQS-1.2: closed sequence supports first()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.first();
    } catch (final XQException e) {
      fail("A-XQS-11.1: first() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-11.1: first() failed", b);
    assertEquals("A-XQS-11.1: first() failed", 1, xqs.getInt());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("()");
    try {
      b = xqs.first();
    } catch (final XQException e) {
      fail("A-XQS-11.2: first() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-11.2: first() failed", b);
    xqe.close();
  }

  public void testGetItem() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.next();
    xqs.getInt();
    try {
      xqs.getItem();
      fail("A-XQS-12.1: SCROLLTYPE_FORWARD_ONLY sequence supports getting item twice()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.getItem();
      fail("A-XQS-12.2: sequence supports getItem() when not positioned on an item");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.next();
    xqs.close();
    try {
      xqs.getItem();
      fail("A-XQS-1.2: closed sequence supports getItem()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.next();
    try {
      xqs.getItem();
    } catch (final XQException e) {
      fail("A-XQS-12.3: getItem() failed with message: " + e.getMessage());
    }
    try {
      xqs.getItem();
    } catch (final XQException e) {
      fail("A-XQS-12.4: SCROLLTYPE_SCROLLABLE sequence doesn't supports getting item twice()");
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.next();
    assertTrue("A-XQS-12.5: Item must be XQResultItem", xqs.getItem() instanceof XQResultItem);
  }

  public void testGetSequenceAsStream() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    XMLStreamReader xmlReader = null;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.close();
    try {
      xqs.getSequenceAsStream();
      fail("A-XQS-1.2: closed sequence supports getSequenceAsStream()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
    try {
      xmlReader = xqs.getSequenceAsStream();
      while (xmlReader.hasNext())
        xmlReader.next();
      fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
    } catch (final XQException xq) {
      // Expect an XQException or XMLStreamException
    } catch (final XMLStreamException xml) {
      // Expect an XQException or XMLStreamException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.next();
    xqs.getItem();
    try {
      xqs.getSequenceAsStream();
      fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    try {
      xmlReader = xqs.getSequenceAsStream();
    } catch (final XQException e) {
      fail("A-XQS-22.1: getSequenceAsStream failed with message: " + e.getMessage());
    }
    try {
      assertNotNull("A-XQS-22.1: getSequenceAsStream returned a null XMLStreamReader", xmlReader);
      assertEquals("A-XQS-22.1: unexpected first event returned by XMLStreamReader", XMLStreamReader.START_DOCUMENT, xmlReader.getEventType());
      assertEquals("A-XQS-22.1: unexpected second event returned by XMLStreamReader", XMLStreamReader.START_ELEMENT, xmlReader.next());
      assertEquals("A-XQS-22.1: unexpected third event returned by XMLStreamReader", XMLStreamReader.CHARACTERS, xmlReader.next());
      assertEquals("A-XQS-22.1: unexpected fourth event returned by XMLStreamReader", XMLStreamReader.END_ELEMENT, xmlReader.next());
      assertEquals("A-XQS-22.1: unexpected fifth event returned by XMLStreamReader", XMLStreamReader.END_DOCUMENT, xmlReader.next());
    } catch (final XMLStreamException e) {
      fail("A-XQS-22.1: XMLStreamReader.next() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testGetSequenceAsString() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.close();
    try {
      xqs.getSequenceAsString(new Properties());
      fail("A-XQS-1.2: closed sequence supports getSequenceAsString()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
    try {
      xqs.getSequenceAsString(new Properties());
      fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
    } catch (final XQException xq) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.next();
    xqs.getItem();
    try {
      xqs.getSequenceAsString(new Properties());
      fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    String result = null;
    try {
      result = xqs.getSequenceAsString(new Properties());
    } catch (final XQException e) {
      fail("A-XQS-23.1: getSequenceAsString failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-23.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + '\'', result.indexOf("<e>Hello world!</e>") != -1);
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    try {
      assertEquals("A-XQS-23.2: null properties argument is equivalent to empty properties argument", result, xqs.getSequenceAsString(null));
    } catch (final XQException e) {
      fail("A-XQS-23.2: getSequenceAsString failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testIsAfterLast() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      xqs.isAfterLast();
      fail("A-XQS-13.1: SCROLLTYPE_FORWARD_ONLY sequence supports isAfterLast()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    xqs.close();
    try {
      xqs.isAfterLast();
      fail("A-XQS-1.2: closed sequence supports isAfterLast()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      assertFalse("A-XQS-13.3: isAfterLast() failed when before first item", xqs.isAfterLast());
    } catch (final XQException e) {
      fail("A-XQS-13.3: isAfterLast() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertFalse("A-XQS-13.3: isAfterLast() failed when on item", xqs.isAfterLast());
    } catch (final XQException e) {
      fail("A-XQS-13.3: isAfterLast() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertTrue("A-XQS-13.2: isAfterLast() failed when after last item", xqs.isAfterLast());
    } catch (final XQException e) {
      fail("A-XQS-13.2: isAfterLast() failed with message: " + e.getMessage());
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("()");
    try {
      assertFalse("A-XQS-13.3: isAfterLast() failed in case of empty sequence", xqs.isAfterLast());
    } catch (final XQException e) {
      fail("A-XQS-13.4: isAfterLast() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testIsBeforeFirst() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      xqs.isBeforeFirst();
      fail("A-XQS-14.1: SCROLLTYPE_FORWARD_ONLY sequence supports isBeforeFirst()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    xqs.close();
    try {
      xqs.isBeforeFirst();
      fail("A-XQS-1.2: closed sequence supports isBeforeFirst()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      assertTrue("A-XQS-14.2: isBeforeFirst() failed when before first item", xqs.isBeforeFirst());
    } catch (final XQException e) {
      fail("A-XQS-14.2: isBeforeFirst() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertFalse("A-XQS-14.3: isBeforeFirst() failed when on item", xqs.isBeforeFirst());
    } catch (final XQException e) {
      fail("A-XQS-14.3: isBeforeFirst() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertFalse("A-XQS-14.3: isBeforeFirst() failed when after last item", xqs.isBeforeFirst());
    } catch (final XQException e) {
      fail("A-XQS-14.3: isBeforeFirst() failed with message: " + e.getMessage());
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("()");
    try {
      assertFalse("A-XQS-14.2: isBeforeFirst() failed in case of empty sequence", xqs.isBeforeFirst());
    } catch (final XQException e) {
      fail("A-XQS-14.1: isBeforeFirst() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testIsFirst() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      xqs.isFirst();
      fail("A-XQS-15.1: SCROLLTYPE_FORWARD_ONLY sequence supports isFirst()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    xqs.close();
    try {
      xqs.isFirst();
      fail("A-XQS-1.2: closed sequence supports isFirst()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      assertFalse("A-XQS-15.3: isFirst() failed when before first item", xqs.isFirst());
    } catch (final XQException e) {
      fail("A-XQS-15.3: isFirst() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertTrue("A-XQS-15.2: isFirst() failed when on item", xqs.isFirst());
    } catch (final XQException e) {
      fail("A-XQS-15.2: isFirst() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertFalse("A-XQS-15.3: isFirst() failed when after last item", xqs.isFirst());
    } catch (final XQException e) {
      fail("A-XQS-15.3: isFirst() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testIsLast() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      xqs.isLast();
      fail("A-XQS-16.1: SCROLLTYPE_FORWARD_ONLY sequence supports isLast()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    xqs.close();
    try {
      xqs.isLast();
      fail("A-XQS-1.2: closed sequence supports isLast()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1");
    try {
      assertFalse("A-XQS-16.3: isLast() failed when before first item", xqs.isLast());
    } catch (final XQException e) {
      fail("A-XQS-16.3: isLast() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertTrue("A-XQS-16.2: isLast() failed when on item", xqs.isLast());
    } catch (final XQException e) {
      fail("A-XQS-16.2: isLast() failed with message: " + e.getMessage());
    }
    xqs.next();
    try {
      assertFalse("A-XQS-16.3: isLast() failed when after last item", xqs.isLast());
    } catch (final XQException e) {
      fail("A-XQS-16.3: isLast() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testLast() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    boolean b = false;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.last();
      fail("A-XQS-1.1: SCROLLTYPE_FORWARD_ONLY sequence supports last()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.close();
    try {
      xqs.last();
      fail("A-XQS-1.2: closed sequence supports last()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.last();
    } catch (final XQException e) {
      fail("A-XQS-17.1: last() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-17.1: last() failed", b);
    assertEquals("A-XQS-17.1: last() failed", 4, xqs.getInt());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("()");
    try {
      b = xqs.last();
    } catch (final XQException e) {
      fail("A-XQS-17.2: last() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-17.2: last() failed", b);
    xqe.close();
  }

  public void testNext() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    boolean b = false;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2");
    xqs.close();
    try {
      xqs.next();
      fail("A-XQS-1.2: closed sequence supports last()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2");
    try {
      b = xqs.next();
    } catch (final XQException e) {
      fail("A-XQS-18.1: next() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-18.1: next() failed", b);
    assertEquals("A-XQS-18.1: next() failed", 1, xqs.getInt());
    try {
      b = xqs.next();
    } catch (final XQException e) {
      fail("A-XQS-18.1: next() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-18.1: next() failed", b);
    assertEquals("A-XQS-18.1: next() failed", 2, xqs.getInt());
    try {
      b = xqs.next();
    } catch (final XQException e) {
      fail("A-XQS-18.2: next() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-18.2: next() failed", b);
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("()");
    b = xqs.next();
    assertFalse("next() failed", b);
    xqe.close();
  }

  public void testPrevious() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    boolean b = false;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2");
    try {
      xqs.previous();
      fail("A-XQS-1.1: SCROLLTYPE_FORWARD_ONLY sequence supports previous()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2");
    xqs.close();
    try {
      xqs.previous();
      fail("A-XQS-1.2: closed sequence supports previous()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2");
    xqs.afterLast();
    try {
      b = xqs.previous();
    } catch (final XQException e) {
      fail("A-XQS-19.1: previous() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-19.1: previous() failed", b);
    assertEquals("A-XQS-19.1: previous() failed", 2, xqs.getInt());
    try {
      b = xqs.previous();
    } catch (final XQException e) {
      fail("A-XQS-19.1: previous() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-19.1: previous() failed", b);
    assertEquals("A-XQS-19.1: previous() failed", 1, xqs.getInt());
    try {
      b = xqs.previous();
    } catch (final XQException e) {
      fail("A-XQS-19.2: previous() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-19.2: previous() failed", b);
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("()");
    xqs.afterLast();
    b = xqs.previous();
    assertFalse("previous() failed", b);
    xqe.close();
  }

  public void testRelative() throws XQException {
    XQExpression xqe;
    XQSequence xqs;
    boolean b = false;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      xqs.relative(2);
      fail("A-XQS-1.1: SCROLLTYPE_FORWARD_ONLY sequence supports relative()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    final XQStaticContext xqsc = xqc.getStaticContext();
    xqsc.setScrollability(XQConstants.SCROLLTYPE_SCROLLABLE);
    xqc.setStaticContext(xqsc);

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.close();
    try {
      xqs.relative(2);
      fail("A-XQS-1.2: closed sequence supports absolute()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.relative(2);
    } catch (final XQException e) {
      fail("A-XQS-20.1: relative() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-20.1: relative(2) failed", b);
    assertEquals("A-XQS-20.1: relative(2) failed", 2, xqs.getInt());
    try {
      b = xqs.relative(2);
    } catch (final XQException e) {
      fail("A-XQS-20.1: relative() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-20.1: relative(2) failed", b);
    assertEquals("A-XQS-20.1: relative(2) failed", 4, xqs.getInt());
    try {
      b = xqs.relative(2);
    } catch (final XQException e) {
      fail("A-XQS-20.4: relative() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-20.4: relative(2) failed", b);
    assertTrue("A-XQS-20.4: relative(2) failed", xqs.isAfterLast());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    try {
      b = xqs.relative(0);
    } catch (final XQException e) {
      fail("A-XQS-20.3: relative() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-20.3: relative(0) failed", b);
    assertTrue("A-XQS-20.3: relative(0) failed", xqs.isBeforeFirst());
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("1,2,3,4");
    xqs.afterLast();
    try {
      b = xqs.relative(-2);
    } catch (final XQException e) {
      fail("A-XQS-20.2: relative() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-20.2: relative(-2) failed", b);
    assertEquals("A-XQS-20.2: relative(-2) failed", 3, xqs.getInt());
    try {
      b = xqs.relative(-2);
    } catch (final XQException e) {
      fail("A-XQS-20.2: relative() failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-20.2: relative(-2) failed", b);
    assertEquals("A-XQS-20.2: relative(-2) failed", 1, xqs.getInt());
    try {
      b = xqs.relative(-2);
    } catch (final XQException e) {
      fail("A-XQS-20.4: relative() failed with message: " + e.getMessage());
    }
    assertFalse("A-XQS-20.4: relative(-2) failed", b);
    assertTrue("A-XQS-20.4: relative(-2) failed", xqs.isBeforeFirst());
    xqe.close();
  }

  public void testWriteSequence_OutputStream() throws XQException, UnsupportedEncodingException {

    // We don't expect this method ever to throw UnsupportedEncodingException, as we only request the "UTF-8" encoding.
    // However, in order to make the compiler happy, and to play it safe, add UnsupportedEncodingException to the throws clause.

    XQExpression xqe;
    XQSequence xqs;

    final Properties prop = new Properties();
    prop.setProperty("encoding", "UTF-8");

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.close();
    try {
      xqs.writeSequence(new ByteArrayOutputStream(), prop);
      fail("A-XQS-1.2: closed sequence supports writeSequence()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.next();
    xqs.getItem();
    try {
      xqs.writeSequence(new ByteArrayOutputStream(), prop);
      fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
    try {
      xqs.writeSequence(new ByteArrayOutputStream(), prop);
      fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
    } catch (final XQException xq) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    try {
      xqs.writeSequence((OutputStream)null, prop);
      fail("A-XQS-24.3: writeSequence accepts a null buffer as first argument");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    final ByteArrayOutputStream result = new ByteArrayOutputStream();
    try {
      xqs.writeSequence(result, prop);
    } catch (final XQException e) {
      fail("A-XQS-24.1: writeSequence failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-24.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result.toString("UTF-8") + '\'', result.toString("UTF-8").indexOf("<e>Hello world!</e>") != -1);
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    final ByteArrayOutputStream otherResult = new ByteArrayOutputStream();
    try {
      xqs.writeSequence(otherResult, prop);
    } catch (final XQException e) {
      fail("A-XQS-24.2: writeSequence failed with message: " + e.getMessage());
    }
    assertEquals("A-XQS-24.2: null properties argument is equivalent to empty properties argument", result.toString("UTF-8"), otherResult.toString("UTF-8"));
    xqe.close();
  }

  public void testWriteSequence_Writer() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    final Properties prop = new Properties();
    prop.setProperty("encoding", "UTF-8");

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.close();
    try {
      xqs.writeSequence(new StringWriter(), prop);
      fail("A-XQS-1.2: closed sequence supports writeSequence()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.next();
    xqs.getItem();
    try {
      xqs.writeSequence(new StringWriter(), prop);
      fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
    try {
      xqs.writeSequence(new StringWriter(), prop);
      fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
    } catch (final XQException xq) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    try {
      xqs.writeSequence((Writer)null, prop);
      fail("A-XQS-24.3: writeSequence accepts a null buffer as first argument");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    final StringWriter result = new StringWriter();
    try {
      xqs.writeSequence(result, prop);
    } catch (final XQException e) {
      fail("A-XQS-24.1: writeSequence failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-24.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result.toString() + '\'', result.toString().indexOf("<e>Hello world!</e>") != -1);
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    final StringWriter otherResult = new StringWriter();
    try {
      xqs.writeSequence(otherResult, prop);
    } catch (final XQException e) {
      fail("A-XQS-24.2: writeSequence failed with message: " + e.getMessage());
    }
    assertEquals("A-XQS-24.2: null properties argument is equivalent to empty properties argument", result.toString(), otherResult.toString());
    xqe.close();
  }

  public void testWriteSequenceToSAX() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.close();
    try {
      xqs.writeSequenceToSAX(new DefaultHandler());
      fail("A-XQS-1.2: closed sequence supports writeSequenceToSAX()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.next();
    xqs.getItem();
    try {
      xqs.writeSequenceToSAX(new DefaultHandler());
      fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
    try {
      xqs.writeSequenceToSAX(new DefaultHandler());
      fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
    } catch (final XQException xq) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    try {
      xqs.writeSequenceToSAX(null);
      fail("A-XQS-24.3: writeSequence accepts a null buffer as first argument");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    final TestContentHandler result = new TestContentHandler();
    try {
      xqs.writeSequenceToSAX(result);
    } catch (final XQException e) {
      fail("A-XQS-24.1: writeSequence failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-24.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result.buffer + '\'', result.buffer.toString().indexOf("<e>Hello world!</e>") != -1);
    xqe.close();
  }

  public void testWriteSequenceToResult() throws XQException {
    XQExpression xqe;
    XQSequence xqs;

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.close();
    try {
      xqs.writeSequenceToResult(new StreamResult(new StringWriter()));
      fail("A-XQS-1.2: closed sequence supports writeSequence()");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    xqs.next();
    xqs.getItem();
    try {
      xqs.writeSequenceToResult(new StreamResult(new StringWriter()));
      fail("A-XQS-21.2: SCROLLTYPE_FORWARD_ONLY sequence, getXXX() or write() method has been invoked already on the current item");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

 // The spec doesn't say serialization is performed according to XSLT 2.0 and XQuery 1.0 serialization.
 // As such we don't test here if the serialization process can fail.
 //
 //   xqe = xqc.createExpression();
 //   xqs = xqe.executeQuery("<e a='Hello world!'/>/@*");
 //   try {
 //     xqs.writeSequenceToResult(new StreamResult(new StringWriter()));
 //     fail("A-XQS-21.1: serialization process fails when sequence contains a top-level attribute");
 //   } catch (XQException xq) {
 //     // Expect an XQException
 //   }
 //   xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    try {
      xqs.writeSequenceToResult(null);
      fail("A-XQS-24.3: writeSequence accepts a null buffer as first argument");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqe.close();

    xqe = xqc.createExpression();
    xqs = xqe.executeQuery("<e>Hello world!</e>");
    final StringWriter result = new StringWriter();
    try {
      xqs.writeSequenceToResult(new StreamResult(result));
    } catch (final XQException e) {
      fail("A-XQS-24.1: writeSequence failed with message: " + e.getMessage());
    }
    assertTrue("A-XQS-24.1: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result.toString() + '\'', result.toString().indexOf("<e>Hello world!</e>") != -1);
    xqe.close();
  }
}
