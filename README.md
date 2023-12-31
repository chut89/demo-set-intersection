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

### (Optional) Install and generate kotlin linter rule sets ###
- Download ktlint at https://pinterest.github.io/ktlint/dev-snapshot/install/cli/#download-and-verification
- Generate .editorconfig file which scaffold basic rule sets
```shellscript
ktlint generateEditorConfig --code-style=ktlint_official >> ./.editorconfig
```
- Details on how to add ktlint-maven-plugin to your POM can be found at https://pinterest.github.io/ktlint/dev-snapshot/install/integrations/

### (Optional) Install and generate detekt ###
```shellscript
curl -sSLO https://github.com/detekt/detekt/releases/download/v1.23.3/detekt-cli-1.23.3.zip
unzip detekt-cli-1.23.3.zip
./detekt-cli-1.23.3/bin/detekt-cli --generate-config
```

### Build and integration test ###
- (Optional) To generate .editorconfig file which scaffold basic rule sets run and detekt which detects potential code smells, if it has been done previously you can skip this step
```shellscript
mvn antrun:run@ktlint-generate-editor-config
```
Then copy the content from `stdout` to `.editorconfig`
```shellscript
mvn antrun:run@detekt-generate-config
```
Then `detekt.yml` is ready
- (Mandatory) Validate, compile, test and integration test
cd to project directory
```shellscript
cd demo-set-intersection/
mvn verify
```
Ktlint report will be output to `stdin`
<br/>
Surefire and Failsafe reports can be found in `target/site/jacoco/index.html` and `target/site/jacoco-it/index.html` respectively
- At the moment securing our Rest apis with ssl is not tested

### (Optional) Create self-signed certificates ###
To secure communication between server and client using one-way ssl we generate a key store for each of them
```shellscript
cd {projectdir.basedir}/src/main/resource
keytool -genkeypair -alias servercert -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore server.p12 -validity 3650 -storepass changeit
```

### Build and run backend ###
cd to project directory
```shellscript
cd demo-set-intersection/
mvn spring-boot:run -Dspring-boot.run.profiles=[active_profile]
```
where `active_profile` can be `local` or `ssl`, then an http (8080) or https (8443) default port will be loaded respectively.
<br/>
If your machine has Apache Maven with other version than 3.9.5, there's no problem. You can overcome with Maven wrapper, the idea is borrowed from Gradle, see detail in https://maven.apache.org/wrapper/

### (Optional) Import client certificate into browser ###
If we have generated key stores for server above and built the application with ssl activated then to make ssl work, we will want to import server certificate into browser. This will be done after the warning about unsecured connection pops up and we make an exception of localhost (server certificate will be imported)

### OpenAPI documentation ###
OpenAPI definition can be found locally at http://localhost:8080/swagger-ui.html (with local profile) or https://localhost:8443/swagger-ui.html (with ssl profile) after backend has been built and started

