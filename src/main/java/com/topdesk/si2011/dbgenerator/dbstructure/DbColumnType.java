package com.topdesk.si2011.dbgenerator.dbstructure;

import org.apache.commons.lang.StringUtils;

public class DbColumnType {
	
	private final int parameter;
	private final DbColumnTypeName type;

	public DbColumnType(DbColumnTypeName type) {
		this.type = type;
		parameter = -1;
	}
	
	public DbColumnType(DbColumnTypeName type, int parameter) {
		this.type = type;
		this.parameter = parameter;
	}
	
	public boolean hasParameter() {
		return getType().hasParameter();
	}
	
	/**
	 * @return the parameter
	 */
	public int getParameter() {
		return parameter;
	}
	
	public static DbColumnType getByName(String name) {
		if (name == null) {
			return null;
		}
		
		for (DbColumnTypeName value : DbColumnTypeName.values()) {
			if (value.hasParameter()) {
				if (name.toLowerCase().startsWith(value.name().toLowerCase())) {
					return new DbColumnType(value, Integer.parseInt(StringUtils.substringBetween(name,"(", ")")));
				}
			}
			else if (value.name().equalsIgnoreCase(name)) {
				return new DbColumnType(value);
			}
		}
		return null;
	}

	/**
	 * @return the type
	 */
	public DbColumnTypeName getType() {
		return type;
	}
	
	@Override
	public String toString() {
		if(type == DbColumnTypeName.TEXT) {
			return type + "(" + parameter + ")";
		}
		
		return type.toString();
	}


}
