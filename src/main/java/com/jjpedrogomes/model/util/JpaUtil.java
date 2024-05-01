package com.jjpedrogomes.model.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory FACTORY;

    static {
        String persistenceUnitName = determinePersistenceUnitName();
        FACTORY = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    private static String determinePersistenceUnitName() {
        String envPersistenceUnit = System.getProperty("env_persistence_unit");
        if (envPersistenceUnit != null && !envPersistenceUnit.isEmpty()) {
            return envPersistenceUnit;
        } else {
            // Default persistence unit name for WildFly
            return "default";
        }
    }

    /**
     * Returns a new EntityManager instance.
     *
     * @return EntityManager - A new EntityManager instance.
     */
    public static EntityManager getEntityManager() {
       return FACTORY.createEntityManager();
    }

    public static void closeFactory() {
        FACTORY.close();
    }
}
