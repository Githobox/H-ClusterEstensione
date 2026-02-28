@echo off
echo ====================================
echo Compilazione H-Cluster Project
echo ====================================

REM Pulisci tutto
echo Pulizia cartelle bin...
if exist bin rmdir /s /q bin
mkdir bin\client
mkdir bin\server

REM Compila SERVER
echo.
echo Compilazione SERVER...
cd server\src
javac -cp "..\lib\mysql-connector-java-8.0.17.jar" -d ..\..\bin\server *.java clustering\*.java data\*.java database\*.java distance\*.java
cd ..\..

REM Copia risorse SERVER
echo Copia risorse SERVER...
xcopy /E /I /Y server\DataStore bin\server\DataStore > nul
copy server\lib\mysql-connector-java-8.0.17.jar bin\server\ > nul

REM Compila CLIENT
echo.
echo Compilazione CLIENT...
cd client\src
javac --module-path "D:\Sviluppo\Languages\Java\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -d ..\..\bin\client Main.java gui\*.java connectionManager\*.java
cd ..\..

REM Copia risorse CLIENT
echo Copia risorse CLIENT...
xcopy /E /I /Y client\src\resources bin\client\resources > nul

echo.
echo ====================================
echo Compilazione completata!
echo ====================================
pause