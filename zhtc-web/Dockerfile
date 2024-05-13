FROM openjdk:8-slim
COPY target/*.jar /app.jar
COPY wangyan.yml /wangyan.yml
ENTRYPOINT ["java", "-jar", "app.jar","--server.port=80","--spring.profiles.active=wangyan","--spring.config.additional-location=wangyan.yml"]
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
