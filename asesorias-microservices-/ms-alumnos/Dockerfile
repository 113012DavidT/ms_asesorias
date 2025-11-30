FROM alpine/java:21-jdk

WORKDIR /app

# Copiar JAR del directorio target
COPY target/*.jar ./app.jar

EXPOSE 8086

# Comando para ejecutar
CMD ["java", "-jar", "app.jar"]
