package org.telap.dao;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telap.model.User;

public interface UserDao extends GenericDao<User, Long> {

	List<User> getUsers();

	User saveUser(User user);

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	String getUserPassword(Long userId);

}
