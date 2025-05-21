FROM maven:3.9-eclipse-temurin-21 AS build
# Build the app
WORKDIR /app
COPY . .
RUN mvn clean install -Pproduction -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

LABEL maintainer="e-codex@eulisa.europa.eu"
LABEL description="e-CODEX connector"

ARG USERNAME=connector-client
ARG BASE_PATH=/app
ARG BUILD_OUTPUT_FOLDER=/app/client-distribution/target/connector-client-distribution/standalone

WORKDIR ${BASE_PATH}

RUN groupadd --system ${USERNAME} \
    && useradd  --system -s /usr/sbin/nologin -g ${USERNAME} ${USERNAME} \
    && mkdir -p database messages logs config \
    && chown -R ${USERNAME}:${USERNAME} ${BASE_PATH}

COPY --from=build --chown=${USERNAME}:${USERNAME} ${BUILD_OUTPUT_FOLDER}/bin/ ${BASE_PATH}/bin/
COPY --from=build --chown=${USERNAME}:${USERNAME} ${BUILD_OUTPUT_FOLDER}/lib/ ${BASE_PATH}/lib/
COPY --from=build --chown=${USERNAME}:${USERNAME} ${BUILD_OUTPUT_FOLDER}/start.sh ${BASE_PATH}

RUN chmod +x ${BASE_PATH}/start.sh

USER ${USERNAME}

EXPOSE 9082

ENTRYPOINT ["/app/start.sh"]
