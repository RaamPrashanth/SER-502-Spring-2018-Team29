package com.ez.compiler;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/*import com.ez.compiler.EZLexer;
import com.ez.compiler.EZParser;*/

/**
 * 
 * @author Team 29
 *
 * This class compiles(Calls lexical analyzer, parser and Intermidiate code generation) the EZ file
 */
public class EZCompiler {

	public static void main(String[] args) {
		
		//TODO
		//String fileName = args[0];
		//Boolean displayParseTree = "-ignoreParseTree".equals(args[1])? false : true;
		
		CharStream charStream = null;
		try {
			charStream = CharStreams.fromFileName("resources/sample/sum.ez");
		} catch (IOException e) {
			System.out.println("ex");
			e.printStackTrace();
		}
		
		
		EZLexer lexer = new EZLexer(charStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		EZParser parser = new EZParser(tokenStream);

		System.out.println("\n parse tree " + parser.program().toStringTree(parser));
		//TODO GUI to display parse tree
	}
}
