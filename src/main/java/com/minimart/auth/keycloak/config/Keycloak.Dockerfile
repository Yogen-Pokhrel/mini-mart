FROM quay.io/keycloak/keycloak:latest
USER keycloak
ENV KC_HOSTNAME="localhost"
ENV KC_DB="postgres"
ENV KC_DB_URL="jdbc:postgresql://keycloak_db:5432/keycloak"
ENV KC_DB_USERNAME="keycloak"
ENV KC_DB_PASSWORD="password"
ENV KC_HEALTH_ENABLED="true"
ENV KC_FEATURES="scripts"
ENV KEYCLOAK_ADMIN="admin"
ENV KEYCLOAK_ADMIN_PASSWORD="admin"
#COPY ./export.json /opt/keycloak/data/import/export.json
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
