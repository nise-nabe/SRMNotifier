package notifier;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SRM {
	public boolean equals(Object obj) {
		if (!(obj instanceof SRM)) {
			return false;
		}
		SRM srm = (SRM) obj;
		if (!(this.getName().equals(srm.getName()))) {
			return false;
		}
		if (!(this.getUrl().equals(srm.getUrl()))) {
			return false;
		}
		if (!(this.getRegisterTime().equals(srm.getRegisterTime()))) {
			return false;
		}
		if (!(this.getCompetisionTime().equals(srm.getCompetisionTime()))) {
			return false;
		}

		return true;
	}

	public String toString() {
		return this.getClass().getName() + "[name=" + name + ",url=" + url
				+ ",competisionTime=" + competisionTime + ",count=" + count
				+ "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCompetisionTime() {
		return competisionTime;
	}

	public void setCompetisionTime(Date competisionTime) {
		this.competisionTime = competisionTime;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

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
		this.competisionTime = update.competisionTime;
		this.count = update.count;
	}
}
