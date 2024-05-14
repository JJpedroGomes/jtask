FROM quay.io/wildfly/wildfly:26.0.1.Final

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
ADD docker/postgres/main/postgresql-42.7.1.jar /tmp/
COPY docker/standalone.xml /opt/jboss/wildfly/standalone/configuration/
COPY target/jtask.war /opt/jboss/wildfly/standalone/deployments/
ADD docker/config.sh /tmp/
ADD docker/batch.cli /tmp/
RUN /tmp/config.sh

EXPOSE 8080 9990 8787
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b=0.0.0.0", "-c", "standalone.xml", "-bmanagement=0.0.0.0"]