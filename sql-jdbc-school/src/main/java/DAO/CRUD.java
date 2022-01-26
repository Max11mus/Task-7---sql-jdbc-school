/**
 * 
 */
package DAO;

import java.util.List;

/**
 * @author Max11mus
 *
 */
public interface CRUD <T> {
		   public List<T> getAllEntities();
		   public <T> getEntity(int rollNo);
		   public void createEntity(<T> object);
		   public void updateEntity(<T> object);
		   public void deleteEntity(<T> object);

}
