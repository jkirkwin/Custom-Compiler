#!/bin/bash

USAGE="Usage: run_tests [-v]"

# Check for -v option
if [[ $# -eq 1 ]]; then

    if [ $1 == "-v" ]; then
        echo "Verbosity enabled."
        VERBOSE=1
    else
        echo $USAGE
        exit 0
    fi
elif [[ $# -gt 1 ]]; then
    echo $USAGE
    exit 0
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

# Counters to be updated on each test
FAILED=0
PASSED=0
TOTAL=0

function run_test() {
    file=$1
    should_accept=$2

    # Run the compiler
    if [ -z ${VERBOSE+x} ]; then
         $JAVA_COMMAND $file 2>/dev/null
    else 
        echo $file
        $JAVA_COMMAND $file
    fi

    # Update the running counts and report the result
    if [ $? -eq $should_accept ]; then
        echo "Pass"
        PASSED=$((PASSED + 1))
    else
        echo "FAIL (${file})"
        FAILED=$((FAILED + 1))
    fi;
    
    # Need an extra line separator when we're printing antlr output
    if [ ! -z ${VERBOSE+x} ]; then
        echo ""
    fi

    TOTAL=$((TOTAL + 1))
}

echo -e "\n\n==== Invalid files =====\n"
for i in $INVALID_UL_FILES; do
    run_test $i 1
done

echo -e "\n==== Valid files (Rejected) =====\n"
for i in $VALID_REJECT_UL_FILES; do
    run_test $i 1
done

echo -e "\n==== Valid files (Accepted) =====\n"
for i in $VALID_ACCEPT_UL_FILES; do
    run_test $i 0
done

echo -e "\n"
echo "TOTAL: $TOTAL"
echo "PASSED: $PASSED"
echo "FAILED: $FAILED"
