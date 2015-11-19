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
	private String name;
	@Persistent
	private String url;
	@Persistent
	private Date registerTime;
	@Persistent
	private Date competisionTime;
	@Persistent
	private int count;

	public void update(SRM update) {
		this.name = update.name;
		this.url = update.url;
		this.registerTime = update.registerTime;
		this.competisionTime = update.competisionTime;
		this.count = update.count;
	}
}
