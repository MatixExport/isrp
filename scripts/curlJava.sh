cd /opt

curl -o jdk.tar.gz https://download.java.net/java/GA/jdk21.0.1/415e3f918a1f4062a0074a2794853d0d/12/GPL/openjdk-21.0.1_linux-x64_bin.tar.gz

tar -xvzf jdk.tar.gz

rm jdk.tar.gz

echo "export JAVA_HOME=/opt/jdk-21.0.1" >> /etc/profile
echo "PATH=\$PATH:\$JAVA_HOME/bin" >> /etc/profile