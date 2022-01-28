package ua.com.foxminded.lms.sqljdbcschool.entities;

import java.util.Objects;
import java.util.UUID;

public class Group {
	private UUID id;
	private String groupName;

	public Group() {
		id = UUID.randomUUID();
		groupName = "";
	}

	public Group(UUID id, String groupName) {
		super();
		this.id = id;
		this.groupName = groupName;
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
		return Objects.hash(groupName, id);
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

	
}