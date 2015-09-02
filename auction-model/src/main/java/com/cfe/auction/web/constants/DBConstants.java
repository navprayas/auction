package com.cfe.auction.web.constants;

public interface DBConstants {
	String MYSQL_LIST_TABLES_QUERY = "show tables";
	String MYSQL_DESC_TABLE_QUERY = "desc ";
	String MYSQL_CHECK_TABLE_COL_QUERY = "SELECT column_name FROM information_schema.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ? ";
	String MYSQLDB_DRIVER_CLASS_NAME = "Mysql Class";
	String REFERENCE_DATA="REFERENCE_DATA";
	String LIVE_DATA="LIVE_DATA";
}
