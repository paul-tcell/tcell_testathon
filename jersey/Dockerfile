FROM tomcat:8.5

WORKDIR /app

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64     \
    MAVEN_HOME=/app/apache-maven-3.3.9      \
    PATH=$PATH:/app/apache-maven-3.3.9/bin  \
    APP_SRC=/app/appsrc

VOLUME  ["/app/appsrc", "/app/tcell", "/root/.m2"]

RUN apt -qq update \
    && apt -q -y upgrade \
    && apt -qq -y install openjdk-8-jdk netcat net-tools \
    && wget -qO- http://www-us.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz | tar xz -C /app \
    && echo 'CATALINA_OPTS="$CATALINA_OPTS -javaagent:/app/tcell/tcellagent.jar"' > $CATALINA_HOME/bin/setenv.sh


COPY jersey/src/main/sh/run.sh /app/
#COPY jersey/src/main/resources/tcell_agent.config /app/tcell/

CMD /app/run.sh

