# Helper script to generate class files using the compiler
# and jasmin. 

USAGE="Usage: ul_to_class <input ul file>"

if [[ $# -ne 1 ]]; then
    echo $USAGE
    exit 1
fi

echo ""
echo "This script does not build the compiler. Did you build the latest version?"
echo ""

INPUT_FILE=$1
OUTPUT_DIR=.

CODEGEN_DIR=/home/jcorless/csc435/codegen/
source ${CODEGEN_DIR}setclasspath


echo "Generating code for input file: ${INPUT_FILE}"

BASE=$(basename ${INPUT_FILE})
CLASS_NAME="${BASE%.*}"
echo "Class name: ${CLASS_NAME}"

java Compiler ${INPUT_FILE}

if [[ $? -ne 0 ]]; then
    echo "codegen failed"
    cat ${JASMIN_INPUT_FILE}
    exit 1
fi

JASMIN_INPUT_FILE=${CLASS_NAME}.j
echo "Ran codegen and created ${JASMIN_INPUT_FILE}"

java jasmin.Main -d $OUTPUT_DIR $JASMIN_INPUT_FILE

if [[ $? -ne 0 ]]; then
    echo "jasmin class-file generation failed"
    exit 1
fi

