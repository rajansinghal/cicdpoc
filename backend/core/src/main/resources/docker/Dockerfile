FROM openjdk:8-jre
RUN apt-get install -y libfontconfig
ENV APP_DIR /app
VOLUME /tmp
ARG JAR_FILE
ADD wait-for-it.sh $APP_DIR/
ADD ${JAR_FILE} $APP_DIR/app.jar
WORKDIR $APP_DIR
EXPOSE 8080
CMD ["java","-Xms1024m","-Xmx2048m", "-Djava.security.egd=file:/dev/./urandom", "-jar","app.jar"]