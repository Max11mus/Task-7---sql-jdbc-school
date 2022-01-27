/**
 * 
 */
package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.util.ArrayList;

public interface CRUD<Entity> {
		   public ArrayList<Entity> getAllEntities();
		   public Entity getEntity ( int recordNumber);
		   public void createEntity(Entity object);
		   public void updateEntity(Entity object);
		   public void deleteEntity(Entity object);

}
