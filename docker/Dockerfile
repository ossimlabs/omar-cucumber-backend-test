FROM Centos:7
ENV HOME "/home/omar"

RUN yum -y update \
    yum -y install java-1.8.0-openjdk \
    useradd -u 1001 -r -g 0 --create-home -d /home/omar -s /sbin/nologin -c \'Default Application User\' omar \
    mkdir /home/omar/build \
    mkdir /home/omar/build/classes \
    mkdir /home/omar/src \
    mkdir /home/omar/src/main
COPY main/ /home/omar/src/mainc\
    groovy /home/omar/build/classes \
    omar-cucumber-backend-test-latest.jar home/omar/ \
    chown 1001:0 -R /home/omar \
    chmod -R 777 /home/omar/
EXPOSE 8080
USER 1001
VOLUME /conf
WORKDIR /home/omar
CMD ['java', '-server', '-Xms256m', '-Xmx1024m', '-Djava.awt.headless=true', '-XX:+CMSClassUnloadingEnabled', '-XX:+UseGCOverheadLimit', '-Djava.security.egd=file:/dev/./urandom', '-jar', "omar-cucumber-backend-test-${version}.jar"]