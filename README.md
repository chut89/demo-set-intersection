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
### Build and run backend ###
cd to project directory
```shellscript
cd demo-set-intersection/
mvn spring-boot:run
```
If your machine has Apache Maven with other version than 3.9.5, there's no problem. You can overcome with Maven wrapper, the idea is borrowed from Gradle: detail in https://maven.apache.org/wrapper/

### OpenAPI documentation ###
OpenAPI definition can be found locally at http://localhost:8080/swagger-ui.html after backend has been built and started

