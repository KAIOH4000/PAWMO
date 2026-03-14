#!/bin/bash
# Spring Boot Application Startup Script

# Project directory
PROJECT_DIR="d:\springboot\d1\code-scaffold\springboot"
CLASSES_DIR="$PROJECT_DIR\target\classes"
MAVEN_REPO="C:\Users\29303\.m2\repository"

# Spring Boot main class
MAIN_CLASS="com.example.springboot.SpringbootApplication"

# Build classpath with all dependencies
CLASSPATH="$CLASSES_DIR"

# Add dependencies from pom.xml to classpath
# Spring Boot Starter Web
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\boot\spring-boot-starter-web\3.5.7\spring-boot-starter-web-3.5.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\boot\spring-boot\3.5.7\spring-boot-3.5.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\boot\spring-boot-autoconfigure\3.5.7\spring-boot-autoconfigure-3.5.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\sprint-context\6.2.7\spring-context-6.2.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\spring-core\6.2.7\spring-core-6.2.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\spring-jcl\6.2.7\spring-jcl-6.2.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\spring-beans\6.2.7\spring-beans-6.2.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\spring-aop\6.2.7\spring-aop-6.2.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\spring-expression\6.2.7\spring-expression-6.2.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\spring-web\6.2.7\spring-web-6.2.7.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\springframework\spring-webmvc\6.2.7\spring-webmvc-6.2.7.jar"

# Tomcat
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\apache\tomcat\embed\tomcat-embed-core\10.1.33\tomcat-embed-core-10.1.33.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\apache\tomcat\embed\tomcat-embed-WebSocket\10.1.33\tomcat-embed-websocket-10.1.33.jar"

# MySQL
CLASSPATH="$CLASSPATH;$MAVEN_REPO\com\mysql\mysql-connector-j\9.2.0\mysql-connector-j-9.2.0.jar"

# MyBatis Plus
CLASSPATH="$CLASSPATH;$MAVEN_REPO\com\baomidou\mybatis-plus-spring-boot3-starter\3.5.14\mybatis-plus-spring-boot3-starter-3.5.14.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\com\baomidou\mybatis-plus-core\3.5.14\mybatis-plus-core-3.5.14.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\mybatis\mybatis\3.5.16\mybatis-3.5.16.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\mybatis\mybatis-spring\3.0.3\mybatis-spring-3.0.3.jar"

# Lombok  
CLASSPATH="$CLASSPATH;$MAVEN_REPO\org\projectlombok\lombok\1.18.30\lombok-1.18.30.jar"

# FastJSON
CLASSPATH="$CLASSPATH;$MAVEN_REPO\com\alibaba\fastjson\2.0.32\fastjson-2.0.32.jar"

# Hutool
CLASSPATH="$CLASSPATH;$MAVEN_REPO\cn\hutool\hutool-all\5.8.18\hutool-all-5.8.18.jar"

# JWT
CLASSPATH="$CLASSPATH;$MAVEN_REPO\com\auth0\java-jwt\4.3.0\java-jwt-4.3.0.jar"

# Alipay SDK
CLASSPATH="$CLASSPATH;$MAVEN_REPO\com\alipay\sdk\alipay-sdk-java\4.38.157.ALL\alipay-sdk-java-4.38.157.ALL.jar"

# Other dependencies as needed...
CLASSPATH="$CLASSPATH;$MAVEN_REPO\commons-codec\commons-codec\1.6\commons-codec-1.6.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\commons-logging\commons-logging\1.2\commons-logging-1.2.jar"
CLASSPATH="$CLASSPATH;$MAVEN_REPO\commons-io\commons-io\2.11.0\commons-io-2.11.0.jar"

echo "Starting Spring Boot Application..."
echo "Main Class: $MAIN_CLASS"
echo "Classes: $CLASSES_DIR"

# Run the application
java -cp "$CLASSPATH" "$MAIN_CLASS"
