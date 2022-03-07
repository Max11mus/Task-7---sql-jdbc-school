package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import java.util.Objects;
import java.util.UUID;

public class Group implements Comparable<Group>{
	private UUID id;
	private String groupName;

	public Group() {
		id = UUID.randomUUID();
		groupName = "";
	}

	public Group(UUID id, String squadName) {
		super();
		this.id = id;
		this.groupName = squadName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return Objects.equals(groupName, other.groupName) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", groupName=" + groupName + "]";
	}

	@Override
	public int compareTo(Group o) {
		return id.compareTo(o.getId());
	}

}