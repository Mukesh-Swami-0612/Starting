#!/bin/bash

echo "=========================================="
echo "    IPL Match Scheduling System"
echo "=========================================="
echo

echo "Creating bin directory..."
mkdir -p bin

echo "Compiling Java files..."
javac -d bin -cp "src/main/java" src/main/java/com/iplscheduler/*.java src/main/java/com/iplscheduler/*/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo
echo "Compilation successful!"
echo
echo "Choose an option:"
echo "1. Run Main Application"
echo "2. Run Test Suite"
echo "3. Exit"
echo
read -p "Enter your choice (1-3): " choice

case $choice in
    1)
        echo
        echo "Running IPL Scheduling System..."
        java -cp bin com.iplscheduler.Main
        ;;
    2)
        echo
        echo "Running Test Suite..."
        java -cp bin com.iplscheduler.SchedulingServiceTest
        ;;
    3)
        echo "Goodbye!"
        exit 0
        ;;
    *)
        echo "Invalid choice!"
        ;;
esac

echo
read -p "Press Enter to continue..." 