FROM java:7
VOLUME /tmp
ADD serviceorder-0.0.1-SNAPSHOT.war	app.war
RUN bash -c 'touch /app.war'
ENTRYPOINT ["java","-jar","/app.war"]
