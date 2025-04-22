#!/bin/bash

# Clean previous compilation
rm -rf out/
mkdir -p out/

# Compile
javac -cp "lib/*" -d out/ src/main/**/*.java

# Run
java -cp "lib/*:out" main.view.MainWindow 