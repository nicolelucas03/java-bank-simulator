#!/bin/bash

mkdir -p bin

javac -d bin src/bankapp/*.java src/exceptions/*.java

java -cp bin bankapp.Main