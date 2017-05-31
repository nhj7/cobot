package nhj.db.mybatis;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable{
	private Integer id;
	private String title;
	private Date releaseDate;
	private Integer artistId;
	private Integer labelId;
	private Date created;
	private Date modified;

	public Record()  {
	}

	//
	// Define the getters and setters for our pojo.
	//
	// ...
	// ...
	// ...

	@Override
	public String toString() {
		return "Record{" + "id=" + id + ", title='" + title + "'" + ", releaseDate=" + releaseDate + ", artistId="
				+ artistId + ", labelId=" + labelId + "}";
	}

}
