mvn package -f isrp/WM-3.6/pom.xml
rm WM-settings.war
cp isrp/WM-3.6/target/WM-2-2-2 WM-settings.war
asadmin disable WM-settings
asadmin undeploy WM-settings
asadmin deploy WM-settings.war
