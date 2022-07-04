package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="group_1")
public class Group implements Comparable<Group> {
	@Column(name="uuid")
	private String uuid;
	@Column(name="group_name")
	private String groupName;

	public Group() {
		uuid = UUID.randomUUID().toString();
		groupName = "";
	}

	public Group(String id, String squadName) {
		super();
		this.uuid = id;
		this.groupName = squadName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String id) {
		this.uuid = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		return Objects.equals(groupName, other.groupName) && Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		return "Group [uuid=" + uuid + ", groupName=" + groupName + "]";
	}

	@Override
	public int compareTo(Group o) {
		return uuid.compareTo(o.getUuid().toString());
	}

}