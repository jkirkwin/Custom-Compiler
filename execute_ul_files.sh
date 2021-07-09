# Helper script to run a set of UL files specified by a pattern
# If no pattern is given

USAGE="Usage: run_test_group <pattern>"
echo "NOTE: This script doesn't run make. Ensure you've compiled the latest version of the code."

if [[ $# -eq 1 ]]; then
    PATTERN=$1
else 
    PATTERN=.ul
fi

FILES=$(find test-cases/valid/accept | grep $PATTERN)

for f in ${FILES}; do
    java -ea Compiler $f &>/dev/null
    if [[ $? -ne 0 ]]; then 
        echo "Compilation failed"
        exit 1
    fi

    BASE=$(basename $f)
    CLASS_NAME="${BASE%.*}"
    IR_FILE_NAME=${CLASS_NAME}.ir

    ./ir_to_class.sh $IR_FILE_NAME . &>/dev/null
    if [[ $? -ne 0 ]]; then
        echo "Code gen failed"
        exit 1
    fi

    echo -e '\n'
    echo "Running class $CLASS_NAME"
    java $CLASS_NAME
    echo -e '\n'
done
