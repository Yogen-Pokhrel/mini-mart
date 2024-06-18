FROM openjdk:21-jdk
WORKDIR /app
RUN ls
COPY Minimart-API-System-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD java --add-opens java.base/java.lang=ALL-UNNAMED -Dserver.port=$PORT -Xmx384m -jar app.jar
