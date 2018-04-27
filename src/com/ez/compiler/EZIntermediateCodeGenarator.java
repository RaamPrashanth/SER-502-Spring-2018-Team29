package com.ez.compiler;

import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.ez.common.EZConstants;

public class EZIntermediateCodeGenarator extends EZBaseListener  {
	
	private static EZIntermediateCodeGenarator INSTANCE = null;
	private static ArrayList<String> intermediateCode;
	private static int nestCount = 1;
	private static Stack<Integer> nestedStack = new Stack<Integer>(); 
	
	public static EZIntermediateCodeGenarator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EZIntermediateCodeGenarator();
			intermediateCode = new ArrayList<String>();
		}
		
		return INSTANCE;
	}
	

	@Override 
	public void enterProgram(EZParser.ProgramContext ctx) { 
	}

	@Override 
	public void exitProgram(EZParser.ProgramContext ctx) {		
	}

	@Override 
	public void enterStatement_list(EZParser.Statement_listContext ctx) {
		
	}

	@Override 
	public void exitStatement_list(EZParser.Statement_listContext ctx) { 
		
	}

	@Override 
	public void enterStatement(EZParser.StatementContext ctx) {
		
	}

	@Override
	public void exitStatement(EZParser.StatementContext ctx) {
		
	}

	@Override 
	public void enterDecl_statement(EZParser.Decl_statementContext ctx) { 
		intermediateCode.add(EZConstants.DECLARE + ctx.identifier().getText());
	}

	@Override 
	public void exitDecl_statement(EZParser.Decl_statementContext ctx) {
		
	}

	@Override 
	public void enterAssign_statement(EZParser.Assign_statementContext ctx) { 
	}

	@Override 
	public void exitAssign_statement(EZParser.Assign_statementContext ctx) {
		intermediateCode.add(EZConstants.ASSIGN + ctx.identifier().getText());
	}

	@Override 
	public void enterRead_statement(EZParser.Read_statementContext ctx) {
		intermediateCode.add(EZConstants.READ + ctx.identifier().getText());
	}

	@Override
	public void exitRead_statement(EZParser.Read_statementContext ctx) {
		
	}
	
	@Override 
	public void enterWrite_statement(EZParser.Write_statementContext ctx) { 
	}
	
	@Override 
	public void exitWrite_statement(EZParser.Write_statementContext ctx) { 
		intermediateCode.add(EZConstants.WRITE);
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

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLoop_statement(EZParser.Loop_statementContext ctx) {
		intermediateCode.add(EZConstants.LOOP.trim() + "_" + nestedStack.push(nestCount));
		nestCount++;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLoop_statement(EZParser.Loop_statementContext ctx) {
		intermediateCode.add(EZConstants.END_LOOP.trim() + "_" + nestedStack.pop());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExpression(EZParser.ExpressionContext ctx) { 

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExpression(EZParser.ExpressionContext ctx) { 
		if (ctx.getText().contains("+")) {
			intermediateCode.add(EZConstants.ADD);
		} else if (ctx.getText().contains("-")) {
			intermediateCode.add(EZConstants.SUB);
		} 
	}

	@Override public void enterExp1(EZParser.Exp1Context ctx) {
		if (ctx.number() != null) {
			intermediateCode.add(EZConstants.PUSH + ctx.number().getText());
		} else if (ctx.identifier() != null) {
			intermediateCode.add(EZConstants.LOAD + ctx.identifier().getText());
		}
	}

	@Override public void exitExp1(EZParser.Exp1Context ctx) { 
		//if (ctx.exp1() == null) {
			if (ctx.getText().contains("*")) {
				intermediateCode.add(EZConstants.MUL);
			} else if (ctx.getText().contains("%")) {
				intermediateCode.add(EZConstants.REM);
			} else if (ctx.getText().contains("/")) {
				intermediateCode.add(EZConstants.DIV);
			}
		//}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCond_expression(EZParser.Cond_expressionContext ctx) {
		
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
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCond_operators(EZParser.Cond_operatorsContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCond_operators(EZParser.Cond_operatorsContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIdentifier(EZParser.IdentifierContext ctx) { 
		System.out.println("\n EZCompiler" + ctx.getText() + " " + ctx.getParent().toStringTree(EZCompiler.getParserInstance()) + " "
				+ ctx.getParent().getParent().toStringTree(EZCompiler.getParserInstance()));
		//intermediateCode.add(EZConstants.LOAD + EZConstants.SPACE + ctx.getText());
		/*
		String temp = intermediateCode.get(intermediateCode.size()-1);
		intermediateCode.remove(intermediateCode.size()-1);
		temp = temp + ctx.getText();
		intermediateCode.add(temp);
		*/
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIdentifier(EZParser.IdentifierContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLetters(EZParser.LettersContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLetters(EZParser.LettersContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterUnderscore(EZParser.UnderscoreContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitUnderscore(EZParser.UnderscoreContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBool_val(EZParser.Bool_valContext ctx) {
		intermediateCode.add(EZConstants.BOOL + ctx.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBool_val(EZParser.Bool_valContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNumber(EZParser.NumberContext ctx) { 
		/*String temp = intermediateCode.get(intermediateCode.size()-1);
		intermediateCode.remove(intermediateCode.size()-1);
		temp = temp + ctx.getText();
		intermediateCode.add(temp);*/
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNumber(EZParser.NumberContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDigit(EZParser.DigitContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDigit(EZParser.DigitContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLowerChar(EZParser.LowerCharContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLowerChar(EZParser.LowerCharContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterUpperChar(EZParser.UpperCharContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitUpperChar(EZParser.UpperCharContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }

	@Override public void enterFunction_statement(EZParser.Function_statementContext ctx) { 
		intermediateCode.add(EZConstants.FUNC_DECL.trim() + "_" + ctx.identifier().getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitFunction_statement(EZParser.Function_statementContext ctx) { 
		intermediateCode.add(EZConstants.FUNC_END.trim() + "_" + ctx.identifier().getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFunction_call_statement(EZParser.Function_call_statementContext ctx) { }

	@Override public void exitFunction_call_statement(EZParser.Function_call_statementContext ctx) { 
		intermediateCode.add(EZConstants.FUNC_CALL.trim() + "_" + ctx.identifier(0).getText());
	}
	
	/**
	 * returns the intermediate code generated till now 
	 */
	@Override public void visitErrorNode(ErrorNode node) { }
	public ArrayList<String> getintermediateCode() {
		return intermediateCode;
	}
}
