package fr.krachimmo.xmlcrc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmlDigesterTest {

	@Test
	public void given_equivalent_xml_should_return_same_hashstring() {
		XmlDigester digester = new XmlDigester("sha1");
		String hash1 = digester.digest(
				"<toto>Hello <titi b=\"2\" a=\"1\"></titi></toto>");
		String hash2 = digester.digest(
				"<?xml version='1.0' encoding='utf8'?>\n" +
				"<toto><![CDATA[Hello ]]><titi \n" +
				"  a=\"1\" b = \"2\" />\n" +
				"<!-- blah -->\n" +
				" </toto>\n");
		assertEquals(hash1, hash2);
	}
}
