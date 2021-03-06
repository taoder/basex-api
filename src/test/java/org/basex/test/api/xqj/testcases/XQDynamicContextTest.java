// Copyright (c) 2003, 2006, 2007, 2008 Oracle. All rights reserved.
package org.basex.test.api.xqj.testcases;

import java.io.*;
import java.math.*;
import java.util.*;

import javax.xml.namespace.*;
import javax.xml.parsers.*;
import javax.xml.transform.stream.*;
import javax.xml.xquery.*;

import org.w3c.dom.*;
import org.xml.sax.*;

@SuppressWarnings("all")
public class XQDynamicContextTest extends XQJTestCase {

  public void testGetImplicitTimeZone() throws XQException {
    XQExpression xqe;

    xqe = xqc.createExpression();
    xqe.close();
    try {
      xqe.getImplicitTimeZone();
      fail("A-XQDC-1.1: getImplicitTimeZone() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqe = xqc.createExpression();
    try {
      // implementation dependent value is returned, don't test it
      xqe.getImplicitTimeZone();
    } catch (final XQException e) {
      fail("A-XQDC-2.1: getImplicitTimeZone() failed with message: " + e.getMessage());
    }
    xqe.close();
  }

  public void testBindAtomicValue() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindAtomicValue(new QName("v"), "Hello world!", null);
      fail("A-XQDC-1.1: bindAtomicValue() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindAtomicValue(null, "Hello world!", null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindAtomicValue(new QName("v"), "Hello world!", xqc.createCommentType());
      fail("A-XQDC-1.3: An invalid type of the value to be bound must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindAtomicValue(new QName("v"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_DECIMAL));
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindAtomicValue(new QName("foo"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindAtomicValue(new QName("v"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindAtomicValue(new QName("v"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindAtomicValue() failed with message: " + e.getMessage());
    }
    final XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "Hello world!", xqs.getAtomicValue());
    xqpe.close();
  }

  public void testBindString() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindString(new QName("v"), "Hello world!", null);
      fail("A-XQDC-1.1: bindString() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindString(null, "Hello world!", null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindString(new QName("v"), "Hello world!", xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
     xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindString(new QName("v"), "123", xqc.createAtomicType(XQItemType.XQBASETYPE_NCNAME));
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindString(new QName("foo"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindString(new QName("v"), "Hello world!", null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindString(new QName("v"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindString() failed with message: " + e.getMessage());
    }
    XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "Hello world!", xqs.getAtomicValue());
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindString(new QName("v"), "Hello", xqc.createAtomicType(XQItemType.XQBASETYPE_NCNAME));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindString() failed with message: " + e.getMessage());
    }
    xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_NCNAME, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "Hello", xqs.getObject());
    xqpe.close();
  }

  public void testBindDocument_String() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindDocument(new QName("v"), "<e>Hello world!</e>", null, null);
      fail("A-XQDC-1.1: bindDocument() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(null, "<e>Hello world!</e>", null, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), "<e>Hello world!</e>", null, xqc.createAtomicType(XQItemType.XQBASETYPE_BOOLEAN));
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_ATOMIC)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
      if (xqs.getItemType().getBaseType() != XQItemType.XQBASETYPE_BOOLEAN)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), "<e>", null, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("foo"), "<e>Hello world!</e>", null, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindDocument(new QName("v"), "<e>Hello world!</e>", null, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), "<e>Hello world!</e>", null, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindDocument() failed with message: " + e.getMessage());
    }
    final String result = xqpe.executeQuery().getSequenceAsString(null);
    assertTrue("A-XQDC-1.7: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + '\'', result.contains("<e>Hello world!</e>"));
    xqpe.close();
  }

  public void testBindDocument_Reader() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindDocument(new QName("v"), new StringReader("<e>Hello world!</e>"), null, null);
      fail("A-XQDC-1.1: bindDocument() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(null, new StringReader("<e>Hello world!</e>"), null, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StringReader("<e>Hello world!</e>"), null, xqc.createAtomicType(XQItemType.XQBASETYPE_BOOLEAN));
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_ATOMIC)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
      if (xqs.getItemType().getBaseType() != XQItemType.XQBASETYPE_BOOLEAN)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StringReader("<e>"), null, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("foo"), new StringReader("<e>Hello world!</e>"), null, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StringReader("<e>Hello world!</e>"), null, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StringReader("<e>Hello world!</e>"), null, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindDocument() failed with message: " + e.getMessage());
    }
    final String result = xqpe.executeQuery().getSequenceAsString(null);
    assertTrue("A-XQDC-1.7: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + '\'', result.contains("<e>Hello world!</e>"));
    xqpe.close();
  }

  public void testBindDocument_InputStream() throws XQException, UnsupportedEncodingException {

    // We don't expect this method ever to throw UnsupportedEncodingException, as we only request the "UTF-8" encoding.
    // However, in order to make the compiler happy, and to play it safe, add UnsupportedEncodingException to the throws clause.

    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindDocument(new QName("v"), new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?><e>Hello world!</e>".getBytes("UTF-8")), null, null);
      fail("A-XQDC-1.1: bindDocument() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(null, new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?><e>Hello world!</e>".getBytes("UTF-8")), null, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?><e>Hello world!</e>".getBytes("UTF-8")), null, xqc.createAtomicType(XQItemType.XQBASETYPE_BOOLEAN));
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_ATOMIC)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
      if (xqs.getItemType().getBaseType() != XQItemType.XQBASETYPE_BOOLEAN)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?><e>".getBytes("UTF-8")), null, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("foo"), new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?><e>Hello world!</e>".getBytes("UTF-8")), null, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?><e>Hello world!</e>".getBytes("UTF-8")), null, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?><e>Hello world!</e>".getBytes("UTF-8")), null, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindDocument() failed with message: " + e.getMessage());
    }
    final String result = xqpe.executeQuery().getSequenceAsString(null);
    assertTrue("A-XQDC-1.7: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + '\'', result.contains("<e>Hello world!</e>"));
    xqpe.close();
  }

  public void testBindDocument_XMLStreamReader() throws XQException {
    XQPreparedExpression xqpe;

    // expression used to create the input XMLStreamReader objects
    final XQExpression xqe = xqc.createExpression();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindDocument(new QName("v"), xqe.executeQuery("<e>Hello world!</e>").getSequenceAsStream(), null);
      fail("A-XQDC-1.1: bindDocument() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(null, xqe.executeQuery("<e>Hello world!</e>").getSequenceAsStream(), null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), xqe.executeQuery("<e>Hello world!</e>").getSequenceAsStream(), xqc.createAtomicType(XQItemType.XQBASETYPE_BOOLEAN));
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_ATOMIC)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
      if (xqs.getItemType().getBaseType() != XQItemType.XQBASETYPE_BOOLEAN)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("foo"), xqe.executeQuery("<e>Hello world!</e>").getSequenceAsStream(), null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindDocument(new QName("v"), xqe.executeQuery("<e>Hello world!</e>").getSequenceAsStream(), null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), xqe.executeQuery("<e>Hello world!</e>").getSequenceAsStream(), null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindDocument() failed with message: " + e.getMessage());
    }
    final String result = xqpe.executeQuery().getSequenceAsString(null);
    assertTrue("A-XQDC-1.7: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + '\'', result.contains("<e>Hello world!</e>"));
    xqpe.close();

    xqe.close();
  }

  public void testBindDocument_Source() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindDocument(new QName("v"), new StreamSource(new StringReader("<e>Hello world!</e>")), null);
      fail("A-XQDC-1.1: bindDocument() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(null, new StreamSource(new StringReader("<e>Hello world!</e>")), null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StreamSource(new StringReader("<e>Hello world!</e>")), xqc.createAtomicType(XQItemType.XQBASETYPE_BOOLEAN));
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_ATOMIC)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
      if (xqs.getItemType().getBaseType() != XQItemType.XQBASETYPE_BOOLEAN)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StreamSource(new StringReader("<e>")), null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("foo"), new StreamSource(new StringReader("<e>Hello world!</e>")), null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StreamSource(new StringReader("<e>Hello world!</e>")), null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDocument(new QName("v"), new StreamSource(new StringReader("<e>Hello world!</e>")), null);
    } catch (final XQException e) {
      e.printStackTrace();
      fail("A-XQDC-1.7: bindDocument() failed with message: " + e.getMessage());
    }
    final String result = xqpe.executeQuery().getSequenceAsString(null);
    assertTrue("A-XQDC-1.7: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + '\'', result.contains("<e>Hello world!</e>"));
    xqpe.close();
  }

  public void testSetImplicitTimeZone() throws XQException {
    XQExpression xqe;

    xqe = xqc.createExpression();
    xqe.close();
    try {
      xqe.setImplicitTimeZone(new SimpleTimeZone(-28800000,"America/Los_Angeles"));
      fail("A-XQDC-1.1: setImplicitTimeZone() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqe = xqc.createExpression();
    xqe.close();
    try {
      xqe.setImplicitTimeZone(null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqe = xqc.createExpression();
    try {
      xqe.setImplicitTimeZone(new SimpleTimeZone(-28800000,"America/Los_Angeles"));
    } catch (final XQException e) {
      fail("A-XQDC-2.1: setImplicitTimeZone() failed with message: " + e.getMessage());
    }
    assertEquals("A-XQDC-2.1: setImplicitTimeZone() sets the timezone for this dynamic context.", -28800000, xqe.getImplicitTimeZone().getRawOffset());
    xqe.close();
  }

  public void testBindItem() throws XQException {
    XQPreparedExpression xqpe;

    // Create an XQItem, which we will use subsequently to test bindItem()
    final XQItem xqi = xqc.createItemFromString("Hello world!", null);

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindItem(new QName("v"), xqi);
      fail("A-XQDC-1.1: bindItem() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindItem(null, xqi);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindItem(new QName("foo"), xqi);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindItem(new QName("v"), xqi);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindItem(new QName("v"),xqi);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindItem() failed with message: " + e.getMessage());
    }
    final XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "Hello world!", xqs.getAtomicValue());
    xqpe.close();

    xqi.close();
    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindItem(new QName("v"), xqi);
      fail("A-XQDC-1.8: Passing a closed XQItem or XQSequence object as argument must result in an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();
  }

  public void testBindSequence() throws XQException {
    XQPreparedExpression xqpe;

    // Create an XQSequence, which we will use subsequently to test bindSequence()
    final XQExpression xqe = xqc.createExpression();
    final XQSequence xqs = xqc.createSequence(xqe.executeQuery("'Hello world!'"));
    xqe.close();

    xqs.beforeFirst();
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindSequence(new QName("v"), xqs);
      fail("A-XQDC-1.1: bindSequence() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqs.beforeFirst();
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindSequence(null, xqs);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqs.beforeFirst();
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindSequence(new QName("foo"), xqs);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqs.beforeFirst();
    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindSequence(new QName("v"), xqs);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqs.beforeFirst();
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindSequence(new QName("v"),xqs);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindSequence() failed with message: " + e.getMessage());
    }
    final XQSequence xqs2 = xqpe.executeQuery();
    xqs2.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "Hello world!", xqs2.getAtomicValue());
    xqpe.close();

    xqs.close();
    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindSequence(new QName("v"), xqs);
      fail("A-XQDC-1.8: Passing a closed XQItem or XQSequence object as argument must result in an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();
  }

  public void testBindObject() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindObject(new QName("v"), "Hello world!", null);
      fail("A-XQDC-1.1: bindObject() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindObject(null, "Hello world!", null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindObject(new QName("v"), "Hello world!", xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
     xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindObject(new QName("v"), "123", xqc.createAtomicType(XQItemType.XQBASETYPE_NCNAME));
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindObject(new QName("foo"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindObject(new QName("v"), "Hello world!", null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindObject(new QName("v"), "Hello world!", xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindObject() failed with message: " + e.getMessage());
    }
    XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "Hello world!", xqs.getAtomicValue());
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindObject(new QName("v"), "Hello", xqc.createAtomicType(XQItemType.XQBASETYPE_NCNAME));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindObject() failed with message: " + e.getMessage());
    }
    xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_NCNAME, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "Hello", xqs.getObject());
    xqpe.close();
  }

  public void testBindObject_AllTypes() throws Exception {
    XQExpression xqe;
    XQSequence xqs;

    final String msg = "A-XQDC-4.1: bindObject implements casting rules of '14.2 Mapping a Java Data Type to an XQuery Data Type'";

    boolean jdk14;
    try {
      Class.forName("javax.xml.datatype.XMLGregorianCalendar");
      jdk14 = false;
    } catch (final Exception e) {
      // assume JDK 1.4
      jdk14 = true;
    }

    xqe = xqc.createExpression();

    xqe.bindObject(new QName("v"), true, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:boolean");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), (byte) 1, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:byte");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), (float) 1, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:float");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), (double) 1, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:double");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), 1, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:int");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), (long) 1, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:long");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), (short) 1, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:short");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), "Hello world!", null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:string");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), new BigDecimal("1"), null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:decimal");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), new BigInteger("1"), null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:integer");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    if (!jdk14) {
      XQExpression xqe_temp;
      XQSequence xqs_temp;

      xqe_temp = xqc.createExpression();
      xqs_temp = xqe_temp.executeQuery("xs:dayTimeDuration('PT5H'), " +
                                       "xs:yearMonthDuration('P1M'), " +
                                       "xs:date('2000-12-31')," +
                                       "xs:dateTime('2000-12-31T12:00:00')," +
                                       "xs:gDay('---11')," +
                                       "xs:gMonth('--11')," +
                                       "xs:gMonthDay('--01-01')," +
                                       "xs:gYear('2000')," +
                                       "xs:gYearMonth('2000-01')," +
                                       "xs:time('12:12:12')");

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:dayTimeDuration");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:yearMonthDuration");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:date");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:dateTime");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:gDay");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:gMonth");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:gMonthDay");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:gYear");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:gYearMonth");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqs_temp.next();
      xqe.bindObject(new QName("v"), xqs_temp.getObject(), null);
      xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:time");
      xqs.next();
      assertTrue(msg, xqs.getBoolean());

      xqe_temp.close();
    }

    xqe.bindObject(new QName("v"), new QName("abc"), null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of xs:QName");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final Document document = builder.newDocument();
    final Element element = document.createElement("e");
    document.appendChild(element);
    final DocumentFragment documentFragment = document.createDocumentFragment();
    final Attr attribute = document.createAttribute("a");
    final Comment comment = document.createComment("comment");
    final ProcessingInstruction pi = document.createProcessingInstruction("target", "data");
    final Text text = document.createTextNode("text");

    xqe.bindObject(new QName("v"), document, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of document-node()");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), documentFragment, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of document-node()");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), element, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of element()");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), attribute, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of attribute()");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), comment, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of comment()");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), pi, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of processing-instruction()");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.bindObject(new QName("v"), text, null);
    xqs = xqe.executeQuery("declare variable $v external; $v instance of text()");
    xqs.next();
    assertTrue(msg, xqs.getBoolean());

    xqe.close();
  }

  public void testBindBoolean() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindBoolean(new QName("v"), true, null);
      fail("A-XQDC-1.1: bindBoolean() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindBoolean(null, true, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindBoolean(new QName("v"), true, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindBoolean(new QName("foo"), true, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindBoolean(new QName("v"), true, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindBoolean(new QName("v"), true, xqc.createAtomicType(XQItemType.XQBASETYPE_BOOLEAN));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindBoolean() failed with message: " + e.getMessage());
    }
    final XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "true", xqs.getAtomicValue());
    xqpe.close();
    xqpe.close();
  }

  public void testBindByte() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindByte(new QName("v"), (byte)1, null);
      fail("A-XQDC-1.1: bindByte() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindByte(null, (byte)1, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindByte(new QName("v"), (byte)1, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    // Can't think of a way to verify A-XQDC-1.4 with the bindByte() method

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindByte(new QName("foo"), (byte)1, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:string external; $v");
    try {
      xqpe.bindByte(new QName("v"), (byte)1, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindByte(new QName("v"), (byte)1, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindByte() failed with message: " + e.getMessage());
    }
    XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindByte(new QName("v"), (byte)1, xqc.createAtomicType(XQItemType.XQBASETYPE_INTEGER));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindByte() failed with message: " + e.getMessage());
    }
    xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_INTEGER, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();
  }

  public void testBindDouble() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindDouble(new QName("v"), 1d, null);
      fail("A-XQDC-1.1: bindDouble() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDouble(null, 1d, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDouble(new QName("v"), 1d, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    // Can't think of a way to verify A-XQDC-1.4 with the bindDouble() method

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDouble(new QName("foo"), 1d, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:string external; $v");
    try {
      xqpe.bindDouble(new QName("v"), 1d, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindDouble(new QName("v"), 1d, xqc.createAtomicType(XQItemType.XQBASETYPE_DOUBLE));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindDouble() failed with message: " + e.getMessage());
    }
    final XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_DOUBLE, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", 1d, xqs.getDouble(), 0.0);
    xqpe.close();
  }

  public void testBindFloat() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindFloat(new QName("v"), 1f, null);
      fail("A-XQDC-1.1: bindFloat() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindFloat(null, 1f, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindFloat(new QName("v"), 1f, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    // Can't think of a way to verify A-XQDC-1.4 with the bindFloat() method

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindFloat(new QName("foo"), 1f, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:string external; $v");
    try {
      xqpe.bindFloat(new QName("v"), 1f, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindFloat(new QName("v"), 1f, xqc.createAtomicType(XQItemType.XQBASETYPE_FLOAT));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindFloat() failed with message: " + e.getMessage());
    }
    final XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_FLOAT, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", 1f, xqs.getFloat(), 0.0);
    xqpe.close();
  }

  public void testBindInt() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindInt(new QName("v"), 1, null);
      fail("A-XQDC-1.1: bindInt() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindInt(null, 1, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindInt(new QName("v"), 1, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindInt(new QName("v"), 128, xqc.createAtomicType(XQItemType.XQBASETYPE_BYTE));
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindInt(new QName("foo"), 1, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:string external; $v");
    try {
      xqpe.bindInt(new QName("v"), 1, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindInt(new QName("v"), 1, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindInt() failed with message: " + e.getMessage());
    }
    XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindInt(new QName("v"), 1, xqc.createAtomicType(XQItemType.XQBASETYPE_INTEGER));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindInt() failed with message: " + e.getMessage());
    }
    xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_INTEGER, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();
  }

  public void testBindLong() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindLong(new QName("v"), 1, null);
      fail("A-XQDC-1.1: bindLong() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindLong(null, 1, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindLong(new QName("v"), 1, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindLong(new QName("v"), 128, xqc.createAtomicType(XQItemType.XQBASETYPE_BYTE));
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindLong(new QName("foo"), 1, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:string external; $v");
    try {
      xqpe.bindLong(new QName("v"), 1, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindLong(new QName("v"), 1, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindLong() failed with message: " + e.getMessage());
    }
    XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindLong(new QName("v"), 1, xqc.createAtomicType(XQItemType.XQBASETYPE_INTEGER));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindLong() failed with message: " + e.getMessage());
    }
    xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_INTEGER, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();
  }

  public void testBindNode() throws XQException, IOException, SAXException, ParserConfigurationException {
    XQPreparedExpression xqpe;

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder parser = factory.newDocumentBuilder();
    final Document document = parser.parse(new InputSource(new StringReader("<e>Hello world!</e>")));

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindNode(new QName("v"), document, null);
      fail("A-XQDC-1.1: bindNode() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindNode(null, document, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindNode(new QName("v"), document, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindNode(new QName("foo"), document, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:decimal external; $v");
    try {
      xqpe.bindNode(new QName("v"), document, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindNode(new QName("v"), document, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindNode() failed with message: " + e.getMessage());
    }
    final String result = xqpe.executeQuery().getSequenceAsString(null);
    assertTrue("A-XQDC-1.7: Expects serialized result contains '<e>Hello world!</e>', but it is '" + result + '\'', result.contains("<e>Hello world!</e>"));
    xqpe.close();
  }

  public void testBindShort() throws XQException {
    XQPreparedExpression xqpe;

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    xqpe.close();
    try {
      xqpe.bindShort(new QName("v"), (short)1, null);
      fail("A-XQDC-1.1: bindShort() throws an XQException when the dynamic context is in closed state.");
    } catch (final XQException e) {
      // Expect an XQException
    }

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindShort(null, (short)1, null);
      fail("A-XQDC-1.2: null argument is invalid and throws an XQException.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    boolean bindFailed = false;
    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindShort(new QName("v"), (short)1, xqc.createCommentType());
    } catch (final XQException e) {
      bindFailed = true;
      // Expect an XQException
    }
    if (!bindFailed) {
      final XQSequence xqs = xqpe.executeQuery();
      xqs.next();
      if (xqs.getItemType().getItemKind() != XQItemType.XQITEMKIND_COMMENT)
        fail("A-XQDC-1.3: The conversion is subject to the following constraints. Either it fails with an XQException, either it is successful in which case it must result in an instance of XDT.");
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindShort(new QName("v"), (short)128, xqc.createAtomicType(XQItemType.XQBASETYPE_BYTE));
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.4: The conversion of the value to an XDM instance must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindShort(new QName("foo"), (short)1, null);
      fail("A-XQDC-1.5: The bound variable must be declared external in the prepared expression.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v as xs:string external; $v");
    try {
      xqpe.bindShort(new QName("v"), (short)1, null);
      xqpe.executeQuery().getSequenceAsString(null);
      fail("A-XQDC-1.6: The dynamic type of the bound value is not compatible with the static type of the variable and must fail.");
    } catch (final XQException e) {
      // Expect an XQException
    }
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindShort(new QName("v"), (short)1, null);
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindShort() failed with message: " + e.getMessage());
    }
    XQSequence xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();

    xqpe = xqc.prepareExpression("declare variable $v external; $v");
    try {
      xqpe.bindShort(new QName("v"), (short)1, xqc.createAtomicType(XQItemType.XQBASETYPE_INTEGER));
    } catch (final XQException e) {
      fail("A-XQDC-1.7: bindShort() failed with message: " + e.getMessage());
    }
    xqs = xqpe.executeQuery();
    xqs.next();
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQITEMKIND_ATOMIC, xqs.getItemType().getItemKind());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", XQItemType.XQBASETYPE_INTEGER, xqs.getItemType().getBaseType());
    assertEquals("A-XQDC-1.7: Successful bindXXX.", "1", xqs.getAtomicValue());
    xqpe.close();
  }
}
