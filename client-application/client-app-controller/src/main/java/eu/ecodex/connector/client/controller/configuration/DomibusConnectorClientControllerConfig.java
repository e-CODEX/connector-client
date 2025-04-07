/*
 * Copyright 2024 European Union Agency for the Operational Management of Large-Scale IT Systems
 * in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.connector.client.controller.configuration;

import eu.ecodex.connector.client.controller.database.DatabaseRestore;
import eu.ecodex.connector.client.controller.persistence.dao.PackageDomibusConnectorClientRepositories;
import eu.ecodex.connector.client.controller.persistence.model.PDomibusConnectorClientPersistenceModel;
import eu.ecodex.connector.client.controller.persistence.service.DomibusConnectorClientPersistenceService;
import eu.ecodex.connector.client.storage.DomibusConnectorClientStorage;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class is the configuration class for the Domibus Connector Client Controller. It provides
 * the necessary beans and configuration properties for the controller to function properly. The
 * configuration properties are loaded from the "connector-client-controller-default.properties"
 * file.tore the database from the storage.
 */
@Data
@Configuration
@EntityScan(basePackageClasses = {PDomibusConnectorClientPersistenceModel.class})
@EnableJpaRepositories(basePackageClasses = {PackageDomibusConnectorClientRepositories.class})
@NoArgsConstructor
@EnableTransactionManagement
@EnableConfigurationProperties({DefaultConfirmationAction.class})
@PropertySource("classpath:/connector-client-controller-default.properties")
@SuppressWarnings("squid:S1118")
public class DomibusConnectorClientControllerConfig {
    public static final String PREFIX = "connector-client.controller";
    public static final String RESTORE_DATABASE_PROPERTY = "restore-database-from-storage";
    private static final Logger LOGGER =
        LoggerFactory.getLogger(DomibusConnectorClientControllerConfig.class);

    /**
     * Configuration class responsible for restoring the database from storage.
     * This configuration is activated conditionally based on a specific property.
     *
     * <p>The `RestoreDatabaseFromStorageConfiguration` class defines a bean to handle
     * the restoration of database entities from a storage source when the required
     * property is enabled. It utilizes the `DomibusConnectorClientPersistenceService`
     * to perform database operations and the `DomibusConnectorClientStorage` to access
     * stored data.
     *
     * <p>The restoration process is automatically triggered after the bean has been
     * initialized, ensuring that necessary database restoration logic is executed
     * during the application startup phase.
     */
    @ConditionalOnProperty(
        prefix = DomibusConnectorClientControllerConfig.PREFIX, value = RESTORE_DATABASE_PROPERTY,
        havingValue = "true"
    )
    @Configuration
    public static class RestoreDatabaseFromStorageConfiguration {
        /**
         * Creates a bean responsible for restoring the database from a storage source.
         * The restoration process is initiated automatically after the bean's initialization.
         *
         * @param persistenceService the service used to handle database operations required during
         *                           the restoration process
         * @param storage the storage service from which the data for restoration is retrieved
         * @return an anonymous object containing the database restoration logic
         */
        @Bean
        public Object restoreDatabaseFromStorageBean(
                DomibusConnectorClientPersistenceService persistenceService,
                DomibusConnectorClientStorage storage) {
            return new Object() {
                @PostConstruct
                public void restoreDb() {
                    LOGGER.debug("#restoreDatabaseFromStorage: enter");
                    var restore = new DatabaseRestore();
                    restore.setPersistenceService(persistenceService);
                    restore.setStorage(storage);
                    restore.restoreDatabaseFromStorage();
                }
            };
        }
    }
}
