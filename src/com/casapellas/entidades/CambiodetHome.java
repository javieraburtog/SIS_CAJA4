package com.casapellas.entidades;

// Generated 01-05-2010 02:37:36 PM by Hibernate Tools 3.2.0.b9

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Cambiodet.
 * @see com.casapellas.entidades.Cambiodet
 * @author Hibernate Tools
 */
public class CambiodetHome {

	private static final Log log = LogFactory.getLog(CambiodetHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Cambiodet transientInstance) {
		log.debug("persisting Cambiodet instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Cambiodet instance) {
		log.debug("attaching dirty Cambiodet instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Cambiodet instance) {
		log.debug("attaching clean Cambiodet instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Cambiodet persistentInstance) {
		log.debug("deleting Cambiodet instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Cambiodet merge(Cambiodet detachedInstance) {
		log.debug("merging Cambiodet instance");
		try {
			Cambiodet result = (Cambiodet) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Cambiodet findById(com.casapellas.entidades.CambiodetId id) {
		log.debug("getting Cambiodet instance with id: " + id);
		try {
			Cambiodet instance = (Cambiodet) sessionFactory.getCurrentSession()
					.get("com.casapellas.entidades.Cambiodet", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Cambiodet instance) {
		log.debug("finding Cambiodet instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.casapellas.entidades.Cambiodet").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
