package org.telap.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:applicationContext-dao.xml" })
@RunWith(SpringJUnit4ClassRunner.class)  
public abstract class BaseDaoTestCase extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private SessionFactory sessionFactory;

	protected final Log log = LogFactory.getLog(getClass());

	protected ResourceBundle rb;

	public BaseDaoTestCase() {
		String className = this.getClass().getName();
		try {
			rb = ResourceBundle.getBundle(className);
		} catch (MissingResourceException mre) {
			log.trace("No resource bundle found for: " + className);
		}
	}

	protected Object populate(final Object obj) throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			map.put(key, rb.getString(key));
		}

		org.springframework.beans.BeanUtils.copyProperties(obj, map);

		return obj;
	}

	protected void flush() throws BeansException {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.flush();
	}

	public void flushSearchIndexes() {
		Session currentSession = sessionFactory.getCurrentSession();
		final FullTextSession fullTextSession = Search.getFullTextSession(currentSession);
		fullTextSession.flushToIndexes();
	}
}
