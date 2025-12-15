@echo off
setlocal enabledelayedexpansion

echo ==========================================
echo  Packaging Jinpo with jpackage
echo ==========================================

rem 1. Build the project
echo [1/2] Building project with Maven...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo Error: Maven build failed.
    exit /b 1
)

rem 2. Create executable with jpackage
echo [2/2] Creating executable with jpackage...
if not exist "%JAVA_HOME%\jre" (
    echo Error: Could not found 'jre' on JAVA_HOME.
    exit /b 1
)

rem Use jpackage to create an application image
rem Output to a temp folder first to avoid input/output conflicts
if exist "package_temp" rmdir /s /q "package_temp"
"%JAVA_HOME%\bin\jpackage" --type app-image --name Jinpo --input dist --main-jar jinpo.jar --runtime-image "%JAVA_HOME%\jre" --icon "res\img\logo.ico" --dest package_temp

if %ERRORLEVEL% NEQ 0 (
    echo Error: jpackage failed.
    exit /b 1
)

rem Move output to dist
if exist "dist\Jinpo" rmdir /s /q "dist\Jinpo"
move "package_temp\Jinpo" "dist\package" >nul
rmdir "package_temp"

echo.
echo Success! Executable created in 'dist\package' folder.
echo You can run the application using: dist\package\Jinpo.exe
endlocal
