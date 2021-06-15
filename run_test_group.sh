# Handy command to run all tests matching a specific pattern. 
# 
# Use the larger test script to run large groups of tests and 
# to see which ones are actually running.

USAGE="Usage: run_test_group <pattern>"

if [[ $# -ne 1 ]]; then
    echo ${USAGE}
    exit 1;
fi

PATTERN=$1

find test-cases/ | grep $PATTERN | xargs -n 1 java -ea Compiler
