@echo off
REM Maven installation script for Windows
setlocal enabledelayedexpansion

set "TEMP_DIR=%TEMP%"
set "MAVEN_ZIP=%TEMP_DIR%\maven.zip"
set "MAVEN_HOME=D:\maven-3.9.11"
set "MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip"

echo Downloading Maven from %MAVEN_URL%...
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = 'Tls12'; (New-Object System.Net.WebClient).DownloadFile('%MAVEN_URL%', '%MAVEN_ZIP%')"

if exist "%MAVEN_ZIP%" (
    echo Maven downloaded to %MAVEN_ZIP%
    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%TEMP_DIR%' -Force"
    
    if exist "%TEMP_DIR%\apache-maven-3.9.11" (
        move "%TEMP_DIR%\apache-maven-3.9.11" "%MAVEN_HOME%"
        echo Maven installed to %MAVEN_HOME%
        set "PATH=%MAVEN_HOME%\bin;!PATH!"
        mvn -v
    )
) else (
    echo Failed to download Maven
)
