package com.casapellas.entidades;
// Generated Jul 15, 2010 10:12:55 AM by Hibernate Tools 3.2.0.b9


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Solecheque.
 * @see com.casapellas.entidades.Solecheque
 * @author Hibernate Tools
 */
public class SolechequeHome {

    private static final Log log = LogFactory.getLog(SolechequeHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();
    
    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        }
        catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }
    
    public void persist(Solecheque transientInstance) {
        log.debug("persisting Solecheque instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Solecheque instance) {
        log.debug("attaching dirty Solecheque instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Solecheque instance) {
        log.debug("attaching clean Solecheque instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Solecheque persistentInstance) {
        log.debug("deleting Solecheque instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Solecheque merge(Solecheque detachedInstance) {
        log.debug("merging Solecheque instance");
        try {
            Solecheque result = (Solecheque) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Solecheque findById( com.casapellas.entidades.SolechequeId id) {
        log.debug("getting Solecheque instance with id: " + id);
        try {
            Solecheque instance = (Solecheque) sessionFactory.getCurrentSession()
                    .get("com.casapellas.entidades.Solecheque", id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findByExample(Solecheque instance) {
        log.debug("finding Solecheque instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.casapellas.entidades.Solecheque")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
}

