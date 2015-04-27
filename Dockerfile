FROM maven:3.2-jdk-8-onbuild

RUN /usr/sbin/useradd --home-dir /usr/src/app --shell /bin/bash spud
EXPOSE 8080

WORKDIR /usr/src/app
USER spud
ENTRYPOINT ["/usr/bin/java", "-jar", "target/SpigotVersionCheck.jar"]
