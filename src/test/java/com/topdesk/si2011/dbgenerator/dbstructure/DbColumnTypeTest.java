package com.topdesk.si2011.dbgenerator.dbstructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DbColumnTypeTest {

	@Test
	public void testGetParameter() {
		assertEquals(6, DbColumnType.getByName("TEXT(6)").getParameter());
	}

	@Test
	public void testGetByName() {
		assertEquals(DbColumnTypeName.MEMO, DbColumnType.getByName("memo").getType());
	}

	@Test
	public void testToString() {
		System.out.println(DbColumnType.getByName("text(8)").toString());
		assertEquals("TEXT(8)", DbColumnType.getByName("text(8)").toString());
	}

}
