package com.ez.compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * 
 * @author Team 29
 *
 * This class compiles(Calls lexical analyzer, parser and intermediate code generation) the EZ file
 */
public class EZCompiler {
	
	static EZParser parser = null;
	public static void main(String[] args) {
		
		String filename = "resources/sample/sum.ez";
		if (args.length > 0){
    			filename = args[0];
		}
		
		CharStream charStream = null;
		try {
			charStream = CharStreams.fromFileName(filename);
		} catch (IOException e) {
			System.out.println("ex");
			e.printStackTrace();
		}
		
		
		EZLexer lexer = new EZLexer(charStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		parser = new EZParser(tokenStream);
		ParseTreeWalker.DEFAULT.walk(EZIntermediateCodeGenarator.getInstance(), parser.program());
		ArrayList<String> intermediateCode =  EZIntermediateCodeGenarator.getInstance().getintermediateCode();
		writeIntermediateFile(filename, intermediateCode);

		System.out.println("\n parse tree " + parser.program().toStringTree(parser));
		
		//TODO GUI to display parse tree
	}
	
	public static EZParser getParserInstance() {
		return parser;
	}
	
	public static void writeIntermediateFile(String fileName, ArrayList<String> intermediateCode) {
		try {
			PrintWriter writer = new PrintWriter(fileName + "i", "UTF-8");
			for (String i:intermediateCode){
				System.out.println(i);
				writer.println(i);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
