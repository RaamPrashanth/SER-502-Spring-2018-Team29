package com.ez.compiler;

import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ErrorNode;

import com.ez.common.EZConstants;

public class EZIntermediateCodeGenarator extends EZBaseListener  {
	
	private static EZIntermediateCodeGenarator INSTANCE = null;
	private static ArrayList<String> intermediateCode;
	private static int nestCount = 1;
	private static Stack<Integer> nestedStack = new Stack<Integer>(); 
	private static Stack<String> function = new Stack<String>();
	
	public static EZIntermediateCodeGenarator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EZIntermediateCodeGenarator();
			intermediateCode = new ArrayList<String>();
		}
		
		return INSTANCE;
	}
	
	@Override 
	public void enterDecl_statement(EZParser.Decl_statementContext ctx) { 
		if (function.isEmpty()) {
			intermediateCode.add(EZConstants.DECLARE + ctx.identifier().getText());
		} else {
			String accm = function.pop();
			function.push(accm);
			intermediateCode.add(EZConstants.DECLARE + accm + ctx.identifier().getText());
		}
	}
	
	@Override 
	public void exitAssign_statement(EZParser.Assign_statementContext ctx) {
		if (function.isEmpty()) {
			intermediateCode.add(EZConstants.ASSIGN + ctx.identifier().getText());
		} else {
			String accm = function.pop();
			function.push(accm);
			intermediateCode.add(EZConstants.ASSIGN + accm + ctx.identifier().getText());
		}
	}

	@Override 
	public void enterRead_statement(EZParser.Read_statementContext ctx) {
		if (function.isEmpty()) {
			intermediateCode.add(EZConstants.READ + ctx.identifier().getText());
		} else {
			String accm = function.pop();
			function.push(accm);
			intermediateCode.add(EZConstants.READ + accm + ctx.identifier().getText());
		}
	}
	
	@Override 
	public void exitWrite_statement(EZParser.Write_statementContext ctx) {
		if (ctx.expression() != null) {
			intermediateCode.add(EZConstants.WRITE);
		} else {
			intermediateCode.add(EZConstants.WRITE_STRING  + ctx.getText().substring(6, ctx.getText().trim().length()-2));
		}
	}

	@Override public void enterIf_statement(EZParser.If_statementContext ctx) {
		intermediateCode.add(EZConstants.IF.trim() +"_" + nestCount);
		nestedStack.push(nestCount);
		nestCount++;
	}

	@Override public void exitIf_statement(EZParser.If_statementContext ctx) {
		intermediateCode.add(EZConstants.END_IF.trim() + "_" + nestedStack.pop());
	}
	
	@Override public void enterElse_statement(EZParser.Else_statementContext ctx) {
		int accm = nestedStack.pop();
		nestedStack.push(accm);
		intermediateCode.add(EZConstants.ELSE.trim() + "_" + accm);
	}

	@Override public void enterLoop_statement(EZParser.Loop_statementContext ctx) {
		intermediateCode.add(EZConstants.LOOP.trim() + "_" + nestedStack.push(nestCount));
		nestCount++;
	}

	@Override public void exitLoop_statement(EZParser.Loop_statementContext ctx) {
		intermediateCode.add(EZConstants.END_LOOP.trim() + "_" + nestedStack.pop());
	}

	@Override public void exitAddition(EZParser.AdditionContext ctx) { 
		intermediateCode.add(EZConstants.ADD);
	}

	@Override public void exitSubtraction(EZParser.SubtractionContext ctx) { 
		intermediateCode.add(EZConstants.SUB);
	}

	@Override public void exitMultiplication(EZParser.MultiplicationContext ctx) {
		intermediateCode.add(EZConstants.MUL);
	}

	@Override public void exitDivision(EZParser.DivisionContext ctx) { 
		intermediateCode.add(EZConstants.DIV);
	}

	@Override public void exitMod(EZParser.ModContext ctx) { 
		intermediateCode.add(EZConstants.REM);
	}

	@Override public void exitFactor(EZParser.FactorContext ctx) { 
		if (ctx.function_call_statement()  == null) {
			if (ctx.number() != null) {
				intermediateCode.add(EZConstants.PUSH + ctx.number().getText());
			} else if (ctx.identifier() != null ) {
				if (function.isEmpty()) {
					intermediateCode.add(EZConstants.LOAD + ctx.identifier().getText());
				} else {
					String accm = function.pop();
					function.push(accm);
					intermediateCode.add(EZConstants.LOAD + accm + ctx.identifier().getText());
				}
			}
		}
	}
	
	@Override public void exitCond_expression(EZParser.Cond_expressionContext ctx) {
		if (ctx.getText().contains("==")) {
			intermediateCode.add(EZConstants.EQUAL);
		} else if (ctx.getText().contains("<")) {
			intermediateCode.add(EZConstants.LESS_THAN);
		} else if (ctx.getText().contains(">")) {
			intermediateCode.add(EZConstants.GREATER_THAN);
		} else if (ctx.getText().contains("<=")) {
			intermediateCode.add(EZConstants.LESS_EQUAL);
		} else if (ctx.getText().contains(">=")) {
			intermediateCode.add(EZConstants.GREAT_EQUAL);
		} else if (ctx.getText().contains("!=")) {
			intermediateCode.add(EZConstants.NOT_EQUAL);
		}
		intermediateCode.add(EZConstants.COND_END);
	}

	@Override public void enterBool_val(EZParser.Bool_valContext ctx) {
		intermediateCode.add(EZConstants.BOOL + ctx.getText());
	}

	@Override public void enterFunction_statement(EZParser.Function_statementContext ctx) { 
		if (ctx.identifier() != null) {
			function.push("#"+ctx.identifier(0).getText());
			intermediateCode.add(EZConstants.FUNC_DECL.trim() + "_" + ctx.identifier(0).getText());
			String func = "";
			func = EZConstants.FUNC_PARAM.trim();
			for (int i = 1; i < ctx.identifier().size(); i++) {
				func = func + " #" +ctx.identifier(0).getText() + ctx.identifier(i).getText();
			}
			intermediateCode.add(func);
		}
	}

	@Override public void exitFunction_statement(EZParser.Function_statementContext ctx) {
		if (ctx.identifier(0) != null) {
			intermediateCode.add(EZConstants.FUNC_END.trim() + "_" + ctx.identifier(0).getText());
			function.pop();
		}
	}

	@Override public void exitFunction_call_statement(EZParser.Function_call_statementContext ctx) { 
		intermediateCode.add(EZConstants.FUNC_CALL.trim() + "_" + ctx.identifier().getText());
	}
	
	@Override public void exitReturn_statement(EZParser.Return_statementContext ctx) { 
		intermediateCode.add(EZConstants.FUNC_RETURN);
	}

	@Override public void visitErrorNode(ErrorNode node) { }
	public ArrayList<String> getintermediateCode() {
		return intermediateCode;
	}
}
