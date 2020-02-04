## Ubuntu Java Install
https://www.javahelps.com/2015/03/install-oracle-jdk-in-ubuntu.html  
sudo tar -xvzf ~/Downloads/jdk-8u231-linux-x64.tar.gz  
sudo gedit /etc/environment  
PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/usr/lib/jvm/jdk1.8.0_231/bin:/usr/lib/jvm/jdk1.8.0_231/db/bin:/usr/lib/jvm/jdk1.8.0_231/jre/bin"  
J2SDKDIR="/usr/lib/jvm/jdk1.8.0_231"  
J2REDIR="/usr/lib/jvm/jdk1.8.0_231/jre"  
JAVA_HOME="/usr/lib/jvm/jdk1.8.0_231"  
DERBY_HOME="/usr/lib/jvm/jdk1.8.0_231/db"  

sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.8.0_231/bin/java" 0  
sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk1.8.0_231/bin/javac" 0  
sudo update-alternatives --set java /usr/lib/jvm/jdk1.8.0_231/bin/java  
sudo update-alternatives --set javac /usr/lib/jvm/jdk1.8.0_231/bin/javac  
update-alternatives --list java  
update-alternatives --list javac  

## lib install
All the lib inside the folder [opt](opt) shold be placed inside the opt folder in ubuntu.  

## Tomcat 8 setup
The environment variable CATALINA_HOME must be set to the Tomcat 8 installation directory.    
Add the following code to the Tomcat configuration file "tomcat-user.xml"  
```
  <role rolename="manager"/>  
  <role rolename="manager-gui"/>  
  <role rolename="manager-script"/>  
  <user username="root" password="root" roles="manager,manager-gui,manager-script"/>  
```
In order for Tomcat to be able to use the JAX-RS jar files and their dependencies, edit the Topmcat 8 catalina.properties configuration file and make sure shared.loader is defined as follows:  
shared.loader=/opt/dp2/shared/lib/*.jar

## NEO4J
neo4j.conf -> dbms.security.auth_enabled=false  
match (n) return n

## Intellij Idea project setup
- File -> Project structure... -> Project -> set the sdk accordingly  
- File -> Project structure... -> Project -> set the language level to "sdk default"  
- File -> Project structure... -> Libraries -> add all the libraries contained in /lib as Java libraries  
- File -> Project structure... -> Libraries -> add to the single library the source contained il lib-src
- gen-src -> Right click -> Mark Directory as -> Source root  
- src -> Right click -> Mark Directory as -> Source root  
- Ant -> add (+ icon) -> select the xml

