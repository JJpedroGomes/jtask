#!/bin/bash
mvn clean package -DskipTests war:exploded
WILDFLY_PATH=/opt/wildfly-26.0.1.Final
$WILDFLY_PATH/bin/standalone.sh
ARTIFACT=$(ls target/*.war | sed "s/target\///g")
$WILDFLY_PATH/bin/jboss-cli.sh -c --controller=localhost:9990 --command="undeploy $ARTIFACT"
rm $WILDFLY_PATH/standalone/deployments/$ARTIFACT*
cp target/$ARTIFACT $WILDFLY_PATH/standalone/deployments/
$WILDFLY_PATH/bin/jboss-cli.sh -c --controller=localhost:9990 --command="deploy --force $WILDFLY_PATH/standalone/deployments/$ARTIFACT"
echo "Deploy it's done: $ARTIFACT"