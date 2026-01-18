# 第一阶段：提取 Jar 包层
# 使用 Eclipse Temurin (原 AdoptOpenJDK)，它是目前最推荐的生产级 JDK 镜像
FROM eclipse-temurin:21-jre-alpine as builder
WORKDIR /application
# 假设 Maven 打包后的 jar 名字叫 app.jar，你需要根据 pom.xml 调整或者重命名
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
# 这一步利用 Spring Boot 的工具把 Jar 包拆解成四个文件夹
RUN java -Djarmode=layertools -jar application.jar extract

# 第二阶段：构建运行镜像
FROM eclipse-temurin:21-jre-alpine
WORKDIR /application

# 按照依赖变更频率从低到高复制，利用 Docker 缓存
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

# 暴露端口 (Gateway 默认通常是 8080 或 9000，根据你 yml 配置调整)
EXPOSE 8080

# 使用 Launcher 启动，而不是直接 java -jar
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]