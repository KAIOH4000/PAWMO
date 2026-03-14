@echo off
REM 收集所有Maven依赖并启动Spring Boot应用
setlocal enabledelayedexpansion

set "MAVEN_REPO=C:\Users\29303\.m2\repository"
set "CLASSES_DIR=d:\springboot\d1\code-scaffold\springboot\target\classes"
set "MAIN_CLASS=com.example.springboot.SpringbootApplication"

REM Initialize classpath with classes
set "CLASSPATH=!CLASSES_DIR!"

echo 正在收集Maven依赖...

REM 递归添加Maven仓库中的所有jar文件
for /r "!MAVEN_REPO!" %%F in (*.jar) do (
    set "CLASSPATH=!CLASSPATH!;%%F"
)

echo 依赖收集完成
echo.
echo 正在启动Spring Boot应用...
echo 主类: !MAIN_CLASS!
echo.

REM 启动应用
java -cp "!CLASSPATH!" "!MAIN_CLASS!"

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] 应用启动失败，错误代码: %errorlevel%
    echo 可能的原因：
    echo 1. Java未正确安装或不在PATH中
    echo 2. 项目未编译 - 请运行: mvn clean install
    echo 3. MySQL数据库服务未运行
    echo 4. 缺少必要的依赖JAR文件
    pause
    exit /b 1
) else (
    echo.
    echo [SUCCESS] 应用已启动
    echo 访问地址: http://localhost:9999
    pause
)
