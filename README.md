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

### Build and integration test ###
To generate .editorconfig file which scaffold basic rule sets run, if it has been done previously you can skip this step
```shellscript
mvn org.apache.maven.plugins:maven-antrun-plugin:3.1.0:run@ktlint-generate-editor-config
```
Then copy the content from stdout to .editorconfig
cd to project directory
```shellscript
cd demo-set-intersection/
mvn verify
```
Ktlint report will be output to stdin
Surefire and Failsafe reports can be found in `target/site/jacoco/index.html` and `target/site/jacoco-it/index.html` respectively

### Build and run backend ###
cd to project directory
```shellscript
cd demo-set-intersection/
mvn spring-boot:run
```
If your machine has Apache Maven with other version than 3.9.5, there's no problem. You can overcome with Maven wrapper, the idea is borrowed from Gradle: detail in https://maven.apache.org/wrapper/

### OpenAPI documentation ###
OpenAPI definition can be found locally at http://localhost:8080/swagger-ui.html after backend has been built and started

