FROM maven:3.9-eclipse-temurin-21 AS build
# Build the app
WORKDIR /app
COPY . .
RUN mvn clean install -Pproduction -DskipTests

# Prepare the image
FROM eclipse-temurin:21

LABEL maintainer="e-codex@eulisa.europa.eu"
LABEL description="e-CODEX connector"

ARG USERNAME=connector-client
ARG USER_GROUP=${USERNAME}
ARG BASE_PATH=/app
ARG BUILD_OUTPUT_FOLDER=/app/client-distribution/target/connector-client/standalone

WORKDIR ${BASE_PATH}

COPY --from=build ${BUILD_OUTPUT_FOLDER}/bin/ ${BASE_PATH}/bin/
COPY --from=build ${BUILD_OUTPUT_FOLDER}/lib/ ${BASE_PATH}/lib/
COPY --from=build ${BUILD_OUTPUT_FOLDER}/start.sh ${BASE_PATH}

RUN groupadd --system ${USER_GROUP} \
    && useradd  --system -s /usr/sbin/nologin -g ${USER_GROUP} ${USERNAME} \
    && mkdir -p database messages logs config \
    && chown -R ${USERNAME}:${USER_GROUP} ${BASE_PATH} \
    && chmod +x ${BASE_PATH}/start.sh

USER ${USERNAME}

EXPOSE 8081

ENTRYPOINT ["/app/start.sh"]
