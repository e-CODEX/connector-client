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
ARG USER_UID=1000
ARG USER_GID=${USER_UID}
ARG APP_FOLDER=/app
ARG BUILD_OUTPUT_FOLDER=/app/client-distribution/target/connector-client-distribution/standalone

WORKDIR ${APP_FOLDER}

RUN apt-get update -y \
    && apt-get upgrade -y \
    && groupadd -g ${USER_GID} ${USERNAME} \
    && useradd -u ${USER_UID} -g ${USER_GID} -m ${USERNAME} \
    && mkdir -p database messages logs config \
    && chown -R ${USERNAME}:${USERNAME} ${APP_FOLDER}

COPY --from=build --chown=${USERNAME}:${USERNAME} ${BUILD_OUTPUT_FOLDER}/bin/ ${APP_FOLDER}/bin/
COPY --from=build --chown=${USERNAME}:${USERNAME} ${BUILD_OUTPUT_FOLDER}/lib/ ${APP_FOLDER}/lib/
COPY --from=build --chown=${USERNAME}:${USERNAME} ${BUILD_OUTPUT_FOLDER}/start.sh ${APP_FOLDER}

RUN chmod +x ${APP_FOLDER}/start.sh

USER ${USERNAME}

EXPOSE 9082

ENTRYPOINT ["/app/start.sh"]
