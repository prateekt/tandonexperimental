package util.db;

public abstract class DBConverter {
	
	protected DBManager db1;
	protected DBManager db2;
	
	protected DBConverter(DBManager db1, DBManager db2) {
		this.db1 = db1;
		this.db2 = db2;
	}
	
	public abstract void handleConversion();
	
	/**
	 * @return the db1
	 */
	public DBManager getDb1() {
		return db1;
	}

	/**
	 * @param db1 the db1 to set
	 */
	public void setDb1(DBManager db1) {
		this.db1 = db1;
	}

	/**
	 * @return the db2
	 */
	public DBManager getDb2() {
		return db2;
	}

	/**
	 * @param db2 the db2 to set
	 */
	public void setDb2(DBManager db2) {
		this.db2 = db2;
	}
}
