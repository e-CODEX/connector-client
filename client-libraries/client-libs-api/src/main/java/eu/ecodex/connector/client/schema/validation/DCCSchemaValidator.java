/*
 * Copyright 2024 European Union Agency for the Operational Management of Large-Scale IT Systems
 * in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.connector.client.schema.validation;

import eu.ecodex.connector.domain.transition.DomibusConnectorMessageType;

/**
 * Interface that may be implemented to validate a messages' business content against a schema. Is
 * extended by {@link DCCInternationalSchemaValidator}
 * and {@link DCCLocalSchemaValidator}
 *
 * @author riederb
 */
public interface DCCSchemaValidator {
    /**
     * Method to validate the business content XML.
     *
     * @param message - The message object holding the business content XML at
     *                message/MessageContent/contentXML
     * @return a ValidationResult holding single results.
     */
    ValidationResult validateBusinessContentXML(DomibusConnectorMessageType message);
}
