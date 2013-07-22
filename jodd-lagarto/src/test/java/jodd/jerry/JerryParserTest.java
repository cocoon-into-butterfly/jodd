// Copyright (c) 2003-2013, Jodd Team (jodd.org). All Rights Reserved.

package jodd.jerry;

import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JerryParserTest {

	@Test
	public void testJerryParserCreation() {
		Jerry.JerryParser jerryParser = Jerry.jerry();

		jerryParser.enableXmlMode();

		Jerry doc = jerryParser.parse("<xml>   <book isbn='123'> <name>Foo<br></name>   </book></xml>");

		Jerry name = doc.$("book name");

		assertEquals("Foo", name.text());

		assertEquals("<xml><book isbn=\"123\"><name>Foo<br></br></name></book></xml>", doc.html());
	}

	@Test
	public void testAppendContent() {
		Jerry.JerryParser jerryParser = Jerry.jerry();

		jerryParser.enableHtmlMode();

		Jerry doc = jerryParser.parse("<xml><book isbn='123'><name>Foo</name></book></xml>");

		Jerry book = doc.$("book");

		book.append("<br>");

		assertEquals("<xml><book isbn=\"123\"><name>Foo</name><br></book></xml>", doc.html());
	}

	@Test
	public void testAppendContent2() {
		Jerry.JerryParser jerryParser = Jerry.jerry();

		jerryParser.enableXmlMode();

		Jerry doc = jerryParser.parse("<xml><book isbn='123'><name>Foo</name></book></xml>");

		Jerry book = doc.$("book");

		book.append("<br>");

		assertEquals("<xml><book isbn=\"123\"><name>Foo</name><br></br></book></xml>", doc.html());
	}

	@Test
	public void testAppendContent3() {
		Jerry.JerryParser jerryParser = Jerry.jerry();

		jerryParser.enableXhtmlMode();

		Jerry doc = jerryParser.parse("<xml><book isbn='123'><name>Foo</name></book></xml>");

		Jerry book = doc.$("book");

		book.append("<br>");

		assertEquals("<xml><book isbn=\"123\"><name>Foo</name><br/></book></xml>", doc.html());
	}

	@Test
	public void testAttributeCaseSensitive() {
		String str = "<dIV id='one' myAttr='aaa'>xxx</div>";

		Jerry.JerryParser jerryParser = Jerry.jerry();
		jerryParser.enableHtmlMode();

		// default

		Jerry doc = jerryParser.parse(str);
		Document document = (Document) doc.get(0);
		Element divNode = (Element) document.getChild(0);
		assertEquals("div", divNode.getNodeName());
		assertNotNull(divNode.getAttribute("myattr"));
		assertNotNull(divNode.getAttribute("myAttr"));

		Element divNode2 = (Element) doc.$("div[myattr=aaa]").nodes[0];
		assertSame(divNode, divNode2);

		assertEquals("<div id=\"one\" myattr=\"aaa\">xxx</div>", doc.html());

		// case sensitive

		jerryParser.getDOMBuilder().setCaseSensitive(true);

		doc = jerryParser.parse(str);
		document = (Document) doc.get(0);
		divNode = (Element) document.getChild(0);
		assertEquals("dIV", divNode.getNodeName());
		assertNull(divNode.getAttribute("myattr"));

		assertEquals("<dIV id=\"one\" myAttr=\"aaa\">xxx</dIV>", doc.html());

		divNode2 = (Element) doc.$("div[myattr=aaa]").nodes[0];
		assertSame(divNode, divNode2);


	}
}
