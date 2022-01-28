package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import java.util.Objects;
import java.util.UUID;

public class Squad {
	private UUID id;
	private String squadName;

	public Squad() {
		id = UUID.randomUUID();
		squadName = "";
	}

	public Squad(UUID id, String squadName) {
		super();
		this.id = id;
		this.squadName = squadName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSquadName() {
		return squadName;
	}

	public void setSquadName(String groupName) {
		this.squadName = groupName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(squadName, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Squad other = (Squad) obj;
		return Objects.equals(squadName, other.squadName) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Squad [id=" + id + ", squadName=" + squadName + "]";
	}

}