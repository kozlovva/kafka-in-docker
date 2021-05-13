FROM bellsoft/liberica-openjdk-alpine-musl:latest
MAINTAINER Kozlov Vladimir <kozlovvladimir6@gmail.com>

ADD ./@project.build.finalName@.jar /app/
CMD ["java", "-Xmx1024m", "-jar", "/app/@project.build.finalName@.jar"]

EXPOSE 8080
