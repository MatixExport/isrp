mkdir /java
cd /java
curl -o jdk.tar.gz https://download.java.net/java/GA/jdk21.0.1/415e3f918a1f4062a0074a2794853d0d/12/GPL/openjdk-21.0.1_linux-x64_bin.tar.gz
curl -o maven.tar.gz https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
curl -o payara.zip https://nexus.payara.fish/repository/payara-community/fish/payara/distributions/payara/6.2024.10/payara-6.2024.10.zip

mkdir javadb
tar -xvzf jdk.tar.gz -C javadb
mkdir maven
tar -xvzf maven.tar.gz -C maven
mkdir payara
unzip payara.zip payara


# echo "sdfsd" >> /etc/profile