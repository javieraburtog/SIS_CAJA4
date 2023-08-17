package com.casapellas.entidades;

// Generated 01-04-2010 05:28:50 PM by Hibernate Tools 3.2.0.b9

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Vrecibosxtipoegreso.
 * @see com.casapellas.entidades.Vrecibosxtipoegreso
 * @author Hibernate Tools
 */
public class VrecibosxtipoegresoHome {

	private static final Log log = LogFactory
			.getLog(VrecibosxtipoegresoHome.class);

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

	public void persist(Vrecibosxtipoegreso transientInstance) {
		log.debug("persisting Vrecibosxtipoegreso instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Vrecibosxtipoegreso instance) {
		log.debug("attaching dirty Vrecibosxtipoegreso instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Vrecibosxtipoegreso instance) {
		log.debug("attaching clean Vrecibosxtipoegreso instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Vrecibosxtipoegreso persistentInstance) {
		log.debug("deleting Vrecibosxtipoegreso instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Vrecibosxtipoegreso merge(Vrecibosxtipoegreso detachedInstance) {
		log.debug("merging Vrecibosxtipoegreso instance");
		try {
			Vrecibosxtipoegreso result = (Vrecibosxtipoegreso) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Vrecibosxtipoegreso findById(
			com.casapellas.entidades.VrecibosxtipoegresoId id) {
		log.debug("getting Vrecibosxtipoegreso instance with id: " + id);
		try {
			Vrecibosxtipoegreso instance = (Vrecibosxtipoegreso) sessionFactory
					.getCurrentSession().get(
							"com.casapellas.entidades.Vrecibosxtipoegreso", id);
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

	public List findByExample(Vrecibosxtipoegreso instance) {
		log.debug("finding Vrecibosxtipoegreso instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.casapellas.entidades.Vrecibosxtipoegreso").add(
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
