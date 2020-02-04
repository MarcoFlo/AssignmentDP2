# AssignmentDP2

## Intellij Idea install instructions
- File -> Project structure... -> Project -> set the sdk accordingly  
- File -> Project structure... -> Project -> set the language level to "sdk default"  
- File -> Project structure... -> Libraries -> add all the libraries contained in /lib as Java libraries  
- File -> Project structure... -> Libraries -> add to the single library the source contained il lib-src
- gen-src -> Right click -> Mark Directory as -> Source root  
- src -> Right click -> Mark Directory as -> Source root  
- Ant -> add (+ icon) -> select the xml

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
