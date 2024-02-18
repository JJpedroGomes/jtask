package com.jjpedrogomes.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

    /**
     * Creates a static instance of the entity manager factory.
     */
    private static final EntityManagerFactory FACTORY = Persistence
            .createEntityManagerFactory("default");

    /**
     * Returns a new EntityManager instance.
     *
     * @return EntityManager - A new EntityManager instance.
     */
    public static EntityManager getEntityManager() {
       return FACTORY.createEntityManager();
    }
}
