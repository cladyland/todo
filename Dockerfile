FROM tomcat:9.0.76-jdk17-corretto

COPY init/ROOT.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
