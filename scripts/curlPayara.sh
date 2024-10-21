mkdir /java
cd /java

curl -o payara.zip https://nexus.payara.fish/repository/payara-community/fish/payara/distributions/payara/6.2024.10/payara-6.2024.10.zip

apt install -y unzip
unzip payara.zip

rm payara.zip

echo "export PAYARA_HOME=/java/payara6" >> /etc/profile
echo "PATH=\$PATH:\$PAYARA_HOME/bin" >> /etc/profile