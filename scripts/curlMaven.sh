mkdir /java
cd /java

curl -o maven.tar.gz https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz

tar -xvzf maven.tar.gz

rm maven.tar.gz

echo "export MAVEN_HOME=/java/apache-maven-3.9.9" >> /etc/profile
echo "PATH=\$PATH:\$MAVEN_HOME/bin" >> /etc/profile