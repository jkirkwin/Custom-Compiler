#!/bin/bash

USAGE="Usage: run_tests [-v]"

if [[ $# -ge 1 ]]; then
    echo $USAGE
    exit 1
fi

echo "Running Make"
make
if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi

# Set test folder names
TEST_CASE_FOLDER="test-cases"

VALID_DIR="${TEST_CASE_FOLDER}/valid" 

REJECT_DIR="${VALID_DIR}/reject"
VALID_REJECT_UL_FILES=$(find $REJECT_DIR -type f -name "*.ul")

ACCEPT_DIR="${VALID_DIR}/accept"
VALID_ACCEPT_UL_FILES=$(find $ACCEPT_DIR -type f -name "*.ul")

INVALID_DIR="${TEST_CASE_FOLDER}/invalid"
INVALID_UL_FILES=$(find $INVALID_DIR -type f -name "*.ul")

# Other constants
JAVA_COMMAND="java -ea Compiler"
JASMIN_COMMAND="java jasmin.Main"
JASMIN_INCLUDE=/home/jcorless/csc435/codegen/setclasspath

# Get the jasmin path so we can run the compiled files
source $JASMIN_INCLUDE

# Counters to be updated on each test
FAILED=0
PASSED=0
TOTAL=0

function increment_total() {
    TOTAL=$((TOTAL + 1))
}

function test_passed() {
    echo "Pass"
    PASSED=$((PASSED + 1))
    increment_total
}

function test_failed() {
    echo "FAIL (${file})"
    FAILED=$((FAILED + 1))
    increment_total
}


function compile() {
    FILE=$1
    $JAVA_COMMAND $FILE
    return $?
}

function get_class_name() {
    SOURCE_FILE=$1
    BASE=$(basename $SOURCE_FILE)
    CLASS_NAME="${BASE%.*}"
    echo $CLASS_NAME
}

function assemble() {
    CLASS_NAME=$1
    JASMIN_FILE=${CLASS_NAME}.j
    $JASMIN_COMMAND $JASMIN_FILE
    return $?
}

function run() {
    CLASS_NAME=$1
    java $CLASS_NAME
    return $?
}

function compile_and_run() {
    FILE=$1
    CLASS_NAME=$(get_class_name $FILE)
    
    compile $FILE && assemble $CLASS_NAME && run $CLASS_NAME
    return $?
}

function run_test_expect_failure() {
    FILE=$1
    
    compile_and_run $FILE
    if [ $? -ne 0 ]; then
        test_passed
    else
        test_failed
    fi
}

function run_test_expect_success() {
    FILE=$1
    
    compile_and_run $FILE
    if [ $? -eq 0 ]; then
        test_passed
    else
        test_failed
    fi
}


echo -e "\n\n==== Invalid files =====\n"
for f in $INVALID_UL_FILES; do
    run_test_expect_failure $f
    echo ""
done

echo -e "\n==== Valid files (Rejected) =====\n"
for f in $VALID_REJECT_UL_FILES; do
    run_test_expect_failure $f
    echo ""
done

echo -e "\n==== Valid files (Accepted) =====\n"
for f in $VALID_ACCEPT_UL_FILES; do
    run_test_expect_success $f
    echo ""
done

echo -e "\n"
echo "TOTAL: $TOTAL"
echo "PASSED: $PASSED"
echo "FAILED: $FAILED"
