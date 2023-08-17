package com.casapellas.entidades;

// Generated Jul 12, 2010 2:07:26 PM by Hibernate Tools 3.2.0.b9

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Vsolecheque.
 * @see com.casapellas.entidades.Vsolecheque
 * @author Hibernate Tools
 */
public class VsolechequeHome {

	private static final Log log = LogFactory.getLog(VsolechequeHome.class);

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

	public void persist(Vsolecheque transientInstance) {
		log.debug("persisting Vsolecheque instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Vsolecheque instance) {
		log.debug("attaching dirty Vsolecheque instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Vsolecheque instance) {
		log.debug("attaching clean Vsolecheque instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Vsolecheque persistentInstance) {
		log.debug("deleting Vsolecheque instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Vsolecheque merge(Vsolecheque detachedInstance) {
		log.debug("merging Vsolecheque instance");
		try {
			Vsolecheque result = (Vsolecheque) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Vsolecheque findById(com.casapellas.entidades.VsolechequeId id) {
		log.debug("getting Vsolecheque instance with id: " + id);
		try {
			Vsolecheque instance = (Vsolecheque) sessionFactory
					.getCurrentSession().get(
							"com.casapellas.entidades.Vsolecheque", id);
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

	public List findByExample(Vsolecheque instance) {
		log.debug("finding Vsolecheque instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.casapellas.entidades.Vsolecheque").add(
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
