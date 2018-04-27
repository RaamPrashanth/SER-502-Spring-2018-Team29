@echo off
set ROOT_PATH=%~dp0

set LIB_PATH=%ROOT_PATH%lib

cd %ROOT_PATH%

echo Removing compiler.jar and runtime.jar

del /f /Q compiler.jar
del /f /Q runtime.jar

echo Starting build...

javac -d . -cp "%ROOT_PATH%src;%LIB_PATH%\antlr-4.7.1-complete.jar" %ROOT_PATH%src\com\ez\compiler\EZCompiler.java

jar cfm compiler.jar %ROOT_PATH%\manifest\compiler_manifest.txt lib com

rmdir /s /q com

javac -d . -cp "%ROOT_PATH%src;%LIB_PATH%\antlr-4.7.1-complete.jar" %ROOT_PATH%src\com\ez\runtime\EZExecuter.java

jar cfm runtime.jar %ROOT_PATH%\manifest\runtime_manifest.txt lib com

rmdir /s /q com

echo Build finished!