package com.ez.runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

import com.ez.common.EZConstants;

/**
 * 
 * @author Team 29
 *
 * This class executes the intermediate code
 */
public class EZExecuter {
	
	static HashMap<String,Integer> variables = new HashMap<String,Integer>();
	static Stack<Integer> intStack = new Stack<Integer>();
	static Stack<Boolean> boolStack = new Stack<Boolean>();
	static ArrayList<String> codeList = new ArrayList<String>();
	static int iteratorIndex = 0;
	static Stack<Integer> nestedStack = new Stack<Integer>();
	static Stack<Boolean> isLoop = new Stack<Boolean>();
	static Stack<Integer> stackTrace = new Stack<Integer>();
	static Stack<Integer> scope = new Stack<Integer>();
	static int scopeCount = 1;
	
	public static void main(String[] args) {
		String filename = "resources/sample/sum.ezi";
		if (args.length > 0){
	    		filename = args[0];
	    	}
		
		try {
			for (String line : Files.readAllLines(Paths.get(filename))) {
				codeList.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (iteratorIndex=0; iteratorIndex<codeList.size(); iteratorIndex++) {
			execute();
		}
	}
	
	static public void throwException(String ex) {
		System.out.println("\n" + ex);
		iteratorIndex = codeList.size();
	}
	
	static public void execute(){
		String code = codeList.get(iteratorIndex);
		String[] split = code.split(" ");
		int tempNest = 0;
		if (code.contains(EZConstants.IF.trim()) || code.contains(EZConstants.ELSE.trim())
				|| code.contains(EZConstants.END_IF.trim()) || code.contains(EZConstants.LOOP.trim())
				|| code.contains(EZConstants.END_LOOP.trim())) {
			split = code.split("_");
			tempNest = Integer.parseInt(split[1]);
			if (code.contains(EZConstants.IF.trim()) ||code.contains(EZConstants.LOOP.trim())) {
				nestedStack.push(tempNest);
			}
		} else if (code.contains(EZConstants.FUNC_CALL.trim())
				|| code.contains(EZConstants.FUNC_DECL.trim()) || code.contains(EZConstants.FUNC_END.trim())) {
			split = code.split("_");
		}
		int accm = 0;
		switch(split[0] + " ") {
			case EZConstants.ADD :
				accm = intStack.pop() + intStack.pop();
				intStack.push(accm);
				break;
			case EZConstants.ASSIGN :
				if (scope.isEmpty()) {
					variables.put(split[1], intStack.pop());
				} else {
					variables.put(getScope() + split[1], intStack.pop());
				}
				break;
			case EZConstants.BOOL:
				boolStack.push(Boolean.parseBoolean(split[1]));
				break;
			case EZConstants.DECLARE:
				if (scope.isEmpty()) {
					if (!variables.containsKey(split[1])) {
						variables.put(split[1], 0);
					} else {
						throwException("Exception variable " + split[1] + " already declared");
					}
				} else {
					if (!variables.containsKey(getScope() + split[1])) {
						variables.put(getScope() + split[1], 0);
					} else {
						throwException("Exception variable " + split[1] + " already declared in this scope");
					}
				}
				break;
			case EZConstants.DIV:
				accm = intStack.pop();
				if (accm != 0) {
					accm = intStack.pop()/accm;
					intStack.push(accm);
				} else {
					throwException("Exception can't divide number by 0");
				}				
				break;
			case EZConstants.ELSE:
				jumpForward(EZConstants.END_IF.trim() + "_" + tempNest); 
				break;
			case EZConstants.END_IF:
				nestedStack.pop();
				break;
			case EZConstants.END_LOOP:
				jumpBackward(EZConstants.LOOP.trim() + "_" + nestedStack.pop());
				break;
			case EZConstants.EQUAL:
				if (intStack.pop() == intStack.pop()) {
					boolStack.push(true);
				} else {
					boolStack.push(false);
				}
				break;
			case EZConstants.GREAT_EQUAL:
				accm = intStack.pop();
				if (intStack.pop() >= accm) {
					boolStack.push(true);
				} else {
					boolStack.push(false);
				}
				break;
			case EZConstants.GREATER_THAN:
				accm = intStack.pop();
				if (intStack.pop() > accm) {
					boolStack.push(true);
				} else {
					boolStack.push(false);
				}
				break;
			case EZConstants.IF:
				isLoop.push(false);
				break;
			case EZConstants.LESS_EQUAL:
				accm = intStack.pop();
				if (intStack.pop() <= accm) {
					boolStack.push(true);
				} else {
					boolStack.push(false);
				}
				break;
			case EZConstants.LESS_THAN:
				accm = intStack.pop();
				if (intStack.pop() < accm) {
					boolStack.push(true);
				} else {
					boolStack.push(false);
				}
				break;
			case EZConstants.LOAD:
				if (scope.isEmpty()) {
					if (variables.containsKey(split[1])) {
						intStack.push(variables.get(split[1]));
					} else {
						throwException("Undeclared variable" + split[1] + " is used.");
					}
				} else {
					if (variables.containsKey(getScope() + split[1])) {
						intStack.push(variables.get(getScope() + split[1]));
					} else {
						throwException("Undeclared variable" + split[1] + " is used.");
					}
				}
				break;
			case EZConstants.LOOP:
				isLoop.push(true);
				break;
			case EZConstants.MUL:
				intStack.push((intStack.pop() * intStack.pop()));
				break;
			case EZConstants.NOT_EQUAL:
				if (intStack.pop() != intStack.pop()) {
					boolStack.push(true);
				} else {
					boolStack.push(false);
				}
				break;
			case EZConstants.PUSH:
				intStack.push(Integer.parseInt(split[1]));
				break;
			case EZConstants.READ:
				Scanner sc = new Scanner(System.in);
				int temp = sc.nextInt();
				if (scope.isEmpty()) {
					variables.put(split[1], temp);
				} else {
					variables.put(getScope() + split[1], temp);
				}
				break;
			case EZConstants.REM:
				accm = intStack.pop();
				accm = intStack.pop() % accm;
				break;
			case EZConstants.SPACE:
				break;
			case EZConstants.SUB:
				accm = intStack.pop();
				accm = intStack.pop() - accm;
				intStack.push(accm);
				break;
			case EZConstants.WRITE:
				System.out.println("\n" + intStack.pop());
				break;
			case EZConstants.WRITE_STRING :
				System.out.println(split[1]);
				break;
			case EZConstants.COND_END:
				if (isLoop.pop()) {
					if (!boolStack.pop()) {
						jumpForward(EZConstants.END_LOOP.trim() + "_" + nestedStack.pop());
					}
				} else {
					if (!boolStack.pop()) {
						accm = nestedStack.pop();
						nestedStack.push(accm);
						jumpForward(EZConstants.ELSE.trim() + "_" + accm);
					}	
				}
				break;
			case EZConstants.FUNC_CALL :
				stackTrace.push(iteratorIndex);
				jumpToFunction(EZConstants.FUNC_DECL.trim() + "_" + split[1]);
				break;
			case EZConstants.FUNC_END :
				//jumpToFunction(EZConstants.FUNC_CALL.trim() + "_" + split[1]);
				scope.pop();
				iteratorIndex = stackTrace.pop();
				break;
			case EZConstants.FUNC_DECL :
				jumpForward(EZConstants.FUNC_END.trim() + "_" + split[1]);
				break;
			case EZConstants.FUNC_PARAM :
				scope.push(scopeCount);
				scopeCount++;
				for (int i = split.length-1; i > 0; i--) {
					variables.put(getScope() + split[i], intStack.pop());	
				}
				break;
			case EZConstants.FUNC_RETURN:
				scope.pop();
				iteratorIndex = stackTrace.pop();
				break;
				
			default:
		}
	}
	
	public static void jumpForward(String label) {
		boolean ifElse = label.contains(EZConstants.ELSE.trim())? true : false;
		int id = 0;
		if (ifElse) {
			String[] temp = label.split("_");
			id = Integer.parseInt(temp[1]);
		}
		for (int i = iteratorIndex; i < codeList.size(); i++) {
			String code = codeList.get(i);
			if (code.trim().equals(label.trim()) || (ifElse && code.trim().contains(EZConstants.END_IF.trim() + "_" + id))) {
				iteratorIndex = i;
				if (code.trim().contains(EZConstants.END_IF.trim() + "_" + id)) {
					nestedStack.pop();
				}
				break;
			}
		}		
	}
	
	public static void jumpBackward(String label) {
		for (int i = iteratorIndex; i >= 0; i--) {
			String code = codeList.get(i);
			if (code.equals(label.trim())) {
				iteratorIndex = i-1;
				break;
			} 
		}
	}
	
	public static void jumpToFunction(String label) {
		for (int i = 0; i < codeList.size(); i++) {
			String code = codeList.get(i);
			if (code.contains(label.trim())) {
				iteratorIndex = i;
				break;
			} 
		}
	}
	
	public static int getScope() {
		int temp = scope.pop();
		scope.push(temp);
		return temp;
	}
	
}
