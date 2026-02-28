@echo off
title H-Cluster Server
echo ====================================
echo Avvio H-Cluster Server
echo ====================================
echo.
cd /d "%~dp0"
set CLASSPATH=bin\server;bin\server\mysql-connector-java-8.0.17.jar
java MultiServer
pause