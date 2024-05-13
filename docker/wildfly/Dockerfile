#FROM jboss/base-jdk:8
#
#WORKDIR /opt/jboss
#
#ENV WILDFLY_VERSION 26.0.1.Final
#ENV WILDFLY_SHA1 9b6d762aa4662045fc3e7329a1ed1c0d457daf6d
#ENV JBOSS_HOME /opt/jboss/wildfly
#
#USER root
#
## Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
## Make sure the distribution is available from a well-known place
#RUN cd $HOME \
#    && curl -L -O https://github.com/wildfly/wildfly/releases/download/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
#    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
#    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
#    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
#    && rm wildfly-$WILDFLY_VERSION.tar.gz \
#    && chown -R jboss:0 ${JBOSS_HOME} \
#    && chmod -R g+rw ${JBOSS_HOME}
#
#COPY jtask.war $JBOSS_HOME/standalone/deployments/
#COPY standalone.xml $JBOSS_HOME/standalone/configuration/
#
#ENV LAUNCH_JBOSS_IN_BACKGROUND true
#
#USER jboss
#
#EXPOSE 8080
#
#CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]

FROM quay.io/wildfly/wildfly:26.0.1.Final

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
ADD postgresql-42.7.1.jar /tmp/
COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/
COPY jtask.war /opt/jboss/wildfly/standalone/deployments/
ADD execute.sh /tmp/
ADD batch.cli /tmp/
RUN /tmp/execute.sh

#COPY postgres /opt/jboss/wildfly/modules/system/layers/base/org
#COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/
#COPY jtask.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080


