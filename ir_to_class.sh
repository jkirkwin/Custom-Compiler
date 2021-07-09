# Helper script to generate class files from the .ir representation
# using the provided code generator and Jasmin

USAGE="Usage: ir_to_class <input ir file> <output dir>"

if [[ $# -ne 2 ]]; then
    echo $USAGE
    exit 1
fi

INPUT_FILE=$1
OUTPUT_DIR=$2

CODEGEN_DIR=/home/jcorless/csc435/codegen/
CODEGEN_EXE=${CODEGEN_DIR}codegen
source ${CODEGEN_DIR}setclasspath

echo "Creating temp directory and temporary ir file"
TEMP_DIR=tmpdir
rm -rf ${TEMP_DIR}
mkdir ${TEMP_DIR}

TEMP_FILE_NAME=$(mktemp --tmpdir=$TEMP_DIR)
JASMIN_INPUT_FILE=${TEMP_FILE_NAME}.j
mv ${TEMP_FILE_NAME} ${JASMIN_INPUT_FILE}

echo "Running codegen on input file: ${INPUT_FILE}"

$CODEGEN_EXE --file=$INPUT_FILE > ${JASMIN_INPUT_FILE}
if [[ $? -ne 0 ]]; then
    echo "codegen failed"
    cat ${JASMIN_INPUT_FILE}
    exit 1
fi

echo "Ran codegen and created ${JASMIN_INPUT_FILE}"

echo "Running jasmin on temp file ${JASMIN_INPUT_FILE}"

java jasmin.Main -d $OUTPUT_DIR $JASMIN_INPUT_FILE
if [[ $? -ne 0 ]]; then
    echo "jasmin class-file generation failed"
    exit 1
fi

rm -rf $TEMP_DIR
