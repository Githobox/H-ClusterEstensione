@echo off
title H-Cluster Client
echo ====================================
echo Avvio H-Cluster Client
echo ====================================
echo.
cd /d "%~dp0"
java --module-path "D:\Sviluppo\Languages\Java\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -cp bin\client Main
pause