package org.telap.dao.hibernate;

import java.util.List;

import javax.persistence.Table;

import org.hibernate.Query;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.telap.dao.UserDao;
import org.telap.model.User;


@Repository("userDao")
public class UserDaoHibernate extends GenericHibernateDao<User, Long> implements UserDao {

	
	public UserDaoHibernate() {
		super(User.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		Query qry = getSession().createQuery("from User u order by upper(u.username)");
		return qry.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public User saveUser(User user) {
		if (log.isDebugEnabled()) {
			log.debug("user's id: " + user.getId());
		}
		getSession().saveOrUpdate(user);
		getSession().flush();
		return user;
	}


	@Override
	public User save(User user) {
		return this.saveUser(user);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUserPassword(Long userId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
		Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
		return jdbcTemplate.queryForObject("select password from " + table.name() + " where id=?", String.class, userId);
	}
}
