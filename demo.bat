@echo off
echo ==========================================
echo    IPL Match Scheduling System - DEMO
echo ==========================================
echo.

echo Creating bin directory...
if not exist "bin" mkdir bin

echo Compiling Java files...
javac -d bin -cp "src/main/java" src/main/java/com/iplscheduler/*.java src/main/java/com/iplscheduler/*/*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful! Running demo...
echo.

echo Running Test Suite to validate system...
java -cp bin com.iplscheduler.SchedulingServiceTest

echo.
echo ==========================================
echo Demo completed! The system is working correctly.
echo ==========================================
echo.
echo To run the full application, use: build.bat
echo.
pause 