package com.cfe.auction.web.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(DatabaseUtil.class);

	public static void closeResources(Connection conn, Statement stmt,
			ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
				logger.warn("Exception while closing database resultset", e);
				;
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (Exception e) {
				logger.warn("Exception while closing database statement", e);
				;
			}
		if (conn != null)
			try {
				conn.close();
			} catch (Exception e) {
				logger.warn("Exception while closing database connection", e);
			}
	}
}
