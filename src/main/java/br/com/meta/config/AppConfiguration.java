package br.com.meta.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AppConfiguration {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("metaPU");

    @Produces
    public  EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void  close(@Disposes EntityManager manager) {
        if (manager.isOpen()) {
            manager.close();
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
