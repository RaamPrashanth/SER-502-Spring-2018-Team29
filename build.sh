#!/bin/sh

SCRIPT_PATH=$(readlink"$0")
ROOT_PATH=$(dirname "$SCRIPT_PATH")

LIB_PATH="$ROOT_PATH/lib"

cd $ROOT_PATH

echo
echo $SCRIPT_PATH
echo
echo "Removing compiler.jar and runtime.jar"
echo

rm compiler.jar
rm runtime.jar

javac -d . -cp ".:$ROOT_PATH/src:$LIB_PATH/antlr-4.7.1-complete.jar" $ROOT_PATH/src/com/ez/compiler/EZCompiler.java

jar cfm compiler.jar ./manifest/compiler_manifest.txt lib com

rm -rf com

javac -d . -cp ".:$ROOT_PATH/src:$LIB_PATH/antlr-4.7.1-complete.jar" $ROOT_PATH/src/com/ez/runtime/EZExecuter.java

jar cfm runtime.jar ./manifest/runtime_manifest.txt lib com

echo
echo "Build Complete."
echo
