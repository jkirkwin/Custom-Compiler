# Helper script to compile and run a .ul file

USAGE="Usage: compile_and_run <input ul file> <class_name>"

if [[ $# -ne 2 ]]; then
    echo $USAGE
    exit 1
fi

UL_FILE=$1
CLASS_NAME=$2

TEMP_DIR=tmp
rm -rf tmp
mkdir -p tmp

BASE_FILE_NAME=$(basename $UL_FILE .ul)
IR_FILE=${TEMP_DIR}/${BASE_FILE_NAME}.ir

java Compiler $UL_FILE 2>/dev/null >$IR_FILE

if [ $? -ne 0 ]; then
    echo "Compilation phase failed"
    exit 1;
fi

./ir_to_class.sh $IR_FILE $TEMP_DIR/

if [ $? -ne 0 ]; then
    echo "IR->class translation failed"
    exit 1;
fi

java -cp $TEMP_DIR $CLASS_NAME
