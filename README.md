# Demo Set Intersection #
Demo project implemented using Spring-boot, Kotlin as backend and React, Typescript as frontend showing how to use compute the intersection of two sets

### Prerequisite ###
- openjdk 21
	- How to install on Linux: https://ubuntuhandbook.org/index.php/2022/03/install-jdk-18-ubuntu/
- Apache Maven latest version
	- Download link: https://maven.apache.org/download.cgi
	- How to install on Linux: https://www.digitalocean.com/community/tutorials/install-maven-linux-ubuntu or https://www.howtoforge.com/tutorial/ubuntu-apache-maven-installation/
- Kotlin latest version
	- How to install on Linux: https://idroot.us/install-kotlin-ubuntu-22-04/
- Git
```shellscript
sudo apt-get -y install git
```	
### Clone backend repository
```
git clone https://github.com/chut89/demo-set-intersection
```
### (Optional) Create self-signed certificates ###
To secure communication between server and client using two-way (mutual) ssl we generate a key store for each of them
```shellscript
cd {projectdir.basedir}/src/main/resource
mkdir -p certs
keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore springboot.p12 -validity 3650 -storepass password
keytool -genkeypair -alias servercert -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore client.p12 -validity 3650 -storepass changeit
```
Then export `client.p12` to a client certificate as follows
```shellscript
keytool -export -keystore client.p12 -alias clientcert -file client.crt
```
Finally import newly generated client certificate to a trust store
```shellscript
keytool -importcert -file client.crt -alias servercert -keystore myTrustStore
```

### Build and run backend ###
cd to project directory
```shellscript
cd demo-set-intersection/
mvn spring-boot:run
```
If your machine has Apache Maven with other version than 3.9.5, there's no problem. You can overcome with Maven wrapper, the idea is borrowed from Gradle: detail in https://maven.apache.org/wrapper/

### (Optional) Import client certificate into browser ###
If we have generated key stores for both client and server above and built the application with ssl activated then to make ssl work, we will want to import client certificate into browser. Otherwise the client cannot be authenticated. Server authentication will be done after the warning about unsecured connection pops up and we make an exception of localhost (server certificate will be imported)
- In Firefox: Key in address bar `about:preferences#privacy`, click `Show certificates`. When the popup windows is loaded, navigate to `Your certificate` Tab and select Import, then point it to `client.p12`
- In Chrome: Key in address bar `chrome://settings/certificates`, navigate to `My certificates` tab then select Import. Finally point it to `client.p12`

### OpenAPI documentation ###
OpenAPI definition can be found locally at http://localhost:8080/swagger-ui.html after backend has been built and started

