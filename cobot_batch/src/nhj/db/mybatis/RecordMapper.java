package nhj.db.mybatis;

public interface RecordMapper {
	/**
	 * Get a single Record from the database based on the record identified.
	 * 
	 * @param id
	 *            record identifier.
	 * @return a record object.
	 */
	Record getRecord(int id);
}
