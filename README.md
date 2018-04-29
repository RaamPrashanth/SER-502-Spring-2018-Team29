# SER-502 - EZ Language

## Team 29
**Team Members**
1. Raam Prashanth Namakkal Sudhakar
2. Hari Siddarth Velaudampalayam Kesavan
3. Mohan Vasantrao Yadav
4. Pratik Suryawanshi

A demo of the project can be found [here](https://youtu.be/x1RiYHtVbXc)
  
**Platform**
```
MacOS
Linux
Windows
```

**Tools**<br>
`ANTLR` (ANother Tool for Language Recognition) is a powerful parser generator for reading, processing, executing, or translating structured text or binary files. It's widely used to build languages, tools, and frameworks. From a grammar, ANTLR generates a parser that can build and walk parse trees.

**Install Instructions**
```
git clone https://github.com/pratik8064/SER-502-Spring-2018-Team29.git
```

**Run Instructions**
```
cd SER-502-Spring-2018-Team29/
java -jar compiler.jar <file_name>.ez 
java -jar runtime.jar <file_name>.ezi

Example
java -jar compiler.jar resources/sample/factorial.ez
java -jar compiler.jar resources/sample/factorial.ezi
```
```
If the jars don't run in your system, you can build using ./build.sh command.
```

# Highlights
The EZ language supports the use of `functions` with `multiple parameters` and `recursive calls`. Parameters can be expressions. Function calls are also handled inside expressions. <br>
The scope of variables in different blocks is handled. <br>
We have handled basic exceptions. <br>
We have supported nested if’s and nested loops. <br>
We have also tried to decrease the runtime of the code to an extent. <br>
We allow users to input values using read command and also write to the console. <br>

**Grammar Changes** <br>
GRAMMAR IS STILL THE SAME. <br>
We have made some structural changes to the grammar to make it compatible with ANTLR. However, the logic remains the same. <br>
We have also added some functionalities like supporting function blocks, function call, return statements, write strings to the grammar.

**Future Scope** <br>
We are planning to add:
1. Lists.
2. Multiple Datatypes. We will handle integer, float and other types of numbers using the "variable" itself and use some other keyword like "String" to support strings.
3. Datastructures.
