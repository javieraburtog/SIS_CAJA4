package com.casapellas.util;
import java.sql.Connection;
import java.sql.DriverManager;

public class AllConectionMngt {
	
	private  Connection simpleConnectionDriver = null;

	private  Connection Create() {
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");

			return DriverManager.getConnection("jdbc:as400://" + PropertiesSystem.IPSERVERDB2 + "/QS36F;prompt=false",
					PropertiesSystem.CN_USRNAME, PropertiesSystem.CN_USRPWD);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private  Connection Create(String user,String pass) throws Exception {
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");

			return DriverManager.getConnection("jdbc:as400://" + PropertiesSystem.IPSERVERDB2 + "/QS36F;prompt=false",
					user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public  Connection getSimpleDriverConnection() {
		try {
			if (simpleConnectionDriver != null && !simpleConnectionDriver.isClosed()) {
				return simpleConnectionDriver;
			}

			return Create();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		public  Connection getCustomDriverConnection(String user,String pass) throws Exception {
			try {
				if (simpleConnectionDriver != null && !simpleConnectionDriver.isClosed()) {
					return simpleConnectionDriver;
				}

				return Create(user,pass);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
	}

	
	
}
