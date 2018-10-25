package com.topdesk.si2011.dbgenerator.dbentry;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

/**
 * Constainer class for exchanging information about the column entry
 * 
 * @author G.D.Eigenraam
 *
 */
public interface IDbColumnEntry {
	DbLocation getColumnLocation();
	String getValue();
}
