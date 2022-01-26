/**
 * 
 */
package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.util.ArrayList;

public interface CRUD<T> {
		   public ArrayList<T> getAllEntities();
		   public T getEntity ( int rollNo);
		   public void createEntity(T object);
		   public void updateEntity(T object);
		   public void deleteEntity(T object);

}
