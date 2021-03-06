package notifier;

import lombok.Data;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Data
public class SRM {
	@PrimaryKey
	private String key;
	@Persistent
	private String name;
	@Persistent
	private Date registerTime;
	@Persistent
	private Date competitionTime;
	@Persistent
	private int count;

	public void update(SRM update) {
		this.name = update.name;
		this.registerTime = update.registerTime;
		this.competitionTime = update.competitionTime;
		this.count = update.count;
	}
}
