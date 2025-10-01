@echo off
echo ==========================================
echo    IPL Match Scheduling System
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
echo Compilation successful! 
echo.
echo Choose an option:
echo 1. Run Main Application
echo 2. Run Test Suite
echo 3. Exit
echo.
set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" (
    echo.
    echo Running IPL Scheduling System...
    java -cp bin com.iplscheduler.Main
) else if "%choice%"=="2" (
    echo.
    echo Running Test Suite...
    java -cp bin com.iplscheduler.SchedulingServiceTest
) else if "%choice%"=="3" (
    echo Goodbye!
    exit /b 0
) else (
    echo Invalid choice!
)

echo.
pause 