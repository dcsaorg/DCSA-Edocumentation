FROM eclipse-temurin:17-jre-alpine

EXPOSE 9090
ENV db_hostname dcsa_db
COPY run-in-container.sh /run.sh
RUN chmod +x /run.sh
COPY edocumentation-application/src/main/resources/application.yml .
COPY edocumentation-application/target/dcsa-edocumentation-application.jar .
CMD ["/run.sh"]
