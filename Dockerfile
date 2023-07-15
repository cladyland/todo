FROM tomcat:9.0.76-jdk17-corretto

LABEL created="Viktoriia Kovalenko"
LABEL github.todo="https://github.com/cladyland/todo"

COPY init/ROOT.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
