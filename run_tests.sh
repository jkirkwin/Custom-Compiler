#!/bin/bash

echo "Running Make"
make

TEST_CASE_FOLDER="test-cases"

VALID_DIR="${TEST_CASE_FOLDER}/valid" 
VALID_UL_FILES=$(find $VALID_DIR -type f -name "*.ul")

INVALID_DIR="${TEST_CASE_FOLDER}/invalid"
INVALID_UL_FILES=$(find $INVALID_DIR -type f -name "*.ul")

FAILED=0
PASSED=0
TOTAL=0

echo -e "\n\n==== Invalid files =====\n"

for i in $INVALID_UL_FILES; do
    echo "Test file: $i"
    java Compiler $i

    if [ $? -eq 0 ]; then
        echo -e "Test failed\n"
        FAILED=$((FAILED + 1))
    else
        PASSED=$((PASSED + 1))
    fi;
    
    TOTAL=$((TOTAL + 1))

    echo -e "\n"
done

echo -e "\n==== Valid files =====\n"

for i in $VALID_UL_FILES; do
    echo "Test file: $i"
    java Compiler $i

    if [ $? -neq 0 ]; then
        echo -e "Test failed\n"
        FAILED=$((FAILED + 1))
    else
        PASSED=$((PASSED + 1))
    fi;
    
    TOTAL=$((TOTAL + 1))

    echo -e "\n"
done

echo -e "\n"
echo "TOTAL: $TOTAL"
echo "PASSED: $PASSED"
echo "FAILED: $FAILED"