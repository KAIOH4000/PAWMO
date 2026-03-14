@echo off
REM Spring Boot Application Startup Script for Windows
setlocal enabledelayedexpansion

set "PROJECT_DIR=d:\springboot\d1\code-scaffold\springboot"
set "CLASSES_DIR=!PROJECT_DIR!\target\classes"
set "MAVEN_REPO=C:\Users\29303\.m2\repository"
set "MAIN_CLASS=com.example.springboot.SpringbootApplication"

REM Check if necessary directories exist
if not exist "!CLASSES_DIR!" (
    echo Error: Classes directory not found: !CLASSES_DIR!
    echo Please compile the project first using: mvn clean install
    pause
    exit /b 1
)

REM Build classpath
set "CLASSPATH=!CLASSES_DIR!"

REM Add Spring Boot dependencies
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\org\springframework\boot\spring-boot-starter-web\3.5.7\spring-boot-starter-web-3.5.7.jar"

REM Add other critical dependencies
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\org\springframework\boot\spring-boot\3.5.7\spring-boot-3.5.7.jar"
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\org\springframework\boot\spring-boot-autoconfigure\3.5.7\spring-boot-autoconfigure-3.5.7.jar"
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar"
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\com\baomidou\mybatis-plus-spring-boot3-starter\3.5.14\mybatis-plus-spring-boot3-starter-3.5.14.jar"
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\org\projectlombok\lombok\1.18.30\lombok-1.18.30.jar"
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\cn\hutool\hutool-all\5.8.18\hutool-all-5.8.18.jar"
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\com\alibaba\fastjson\2.0.32\fastjson-2.0.32.jar"
set "CLASSPATH=!CLASSPATH!;!MAVEN_REPO!\com\auth0\java-jwt\4.3.0\java-jwt-4.3.0.jar"

echo Starting Spring Boot Application...
echo Classpath configured with !CLASSPATH!

REM Run the application
java -cp "!CLASSPATH!" "!MAIN_CLASS!"

if %errorlevel% neq 0 (
    echo.
    echo Error: Failed to start the application.
    echo Please make sure:
    echo 1. Java is installed and in your PATH
    echo 2. The project has been compiled (mvn clean install)
    echo 3. All dependencies are available in Maven local repository
    pause
)
