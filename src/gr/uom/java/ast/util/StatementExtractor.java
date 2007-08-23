package gr.uom.java.ast.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class StatementExtractor {
	private StatementInstanceChecker instanceChecker;
	
	public List<Statement> getConstructorInvocations(Statement statement) {
		instanceChecker = new InstanceOfConstructorInvocation();
		return getStatements(statement);
	}
	
	public List<Statement> getVariableDeclarations(Statement statement) {
		instanceChecker = new InstanceOfVariableDeclaration();
		return getStatements(statement);
	}
	
	private List<Statement> getStatements(Statement statement) {
		List<Statement> statementList = new ArrayList<Statement>();
		if(statement instanceof Block) {
			Block block = (Block)statement;
			List<Statement> blockStatements = block.statements();
			for(Statement blockStatement : blockStatements)
				statementList.addAll(getStatements(blockStatement));
		}
		else if(statement instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement)statement;
			statementList.addAll(getStatements(ifStatement.getThenStatement()));
			if(ifStatement.getElseStatement() != null) {
				statementList.addAll(getStatements(ifStatement.getElseStatement()));
			}
		}
		else if(statement instanceof ForStatement) {
			ForStatement forStatement = (ForStatement)statement;
			statementList.addAll(getStatements(forStatement.getBody()));
		}
		else if(statement instanceof EnhancedForStatement) {
			EnhancedForStatement enhancedForStatement = (EnhancedForStatement)statement;
			statementList.addAll(getStatements(enhancedForStatement.getBody()));
		}
		else if(statement instanceof WhileStatement) {
			WhileStatement whileStatement = (WhileStatement)statement;
			statementList.addAll(getStatements(whileStatement.getBody()));
		}
		else if(statement instanceof DoStatement) {
			DoStatement doStatement = (DoStatement)statement;
			statementList.addAll(getStatements(doStatement.getBody()));
		}
		else if(statement instanceof ExpressionStatement) {
			ExpressionStatement expressionStatement = (ExpressionStatement)statement;
		}
		else if(statement instanceof SwitchStatement) {
			SwitchStatement switchStatement = (SwitchStatement)statement;
			List<Statement> switchStatements = switchStatement.statements();
			for(Statement switchStatement2 : switchStatements)
				statementList.addAll(getStatements(switchStatement2));
		}
		else if(statement instanceof SwitchCase) {
			SwitchCase switchCase = (SwitchCase)statement;
		}
		else if(statement instanceof AssertStatement) {
			AssertStatement assertStatement = (AssertStatement)statement;
		}
		else if(statement instanceof LabeledStatement) {
			LabeledStatement labeledStatement = (LabeledStatement)statement;
			statementList.addAll(getStatements(labeledStatement.getBody()));
		}
		else if(statement instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement)statement;
		}
		else if(statement instanceof SynchronizedStatement) {
			SynchronizedStatement synchronizedStatement = (SynchronizedStatement)statement;
			statementList.addAll(getStatements(synchronizedStatement.getBody()));
		}
		else if(statement instanceof ThrowStatement) {
			ThrowStatement throwStatement = (ThrowStatement)statement;
		}
		else if(statement instanceof TryStatement) {
			TryStatement tryStatement = (TryStatement)statement;
			statementList.addAll(getStatements(tryStatement.getBody()));
			List<CatchClause> catchClauses = tryStatement.catchClauses();
			for(CatchClause catchClause : catchClauses) {
				statementList.addAll(getStatements(catchClause.getBody()));
			}
			Block finallyBlock = tryStatement.getFinally();
			if(finallyBlock != null)
				statementList.addAll(getStatements(finallyBlock));
		}
		else if(statement instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement)statement;
			if(instanceChecker.instanceOf(variableDeclarationStatement))
				statementList.add(variableDeclarationStatement);
		}
		else if(statement instanceof ConstructorInvocation) {
			ConstructorInvocation constructorInvocation = (ConstructorInvocation)statement;
			if(instanceChecker.instanceOf(constructorInvocation))
				statementList.add(constructorInvocation);
		}
		else if(statement instanceof SuperConstructorInvocation) {
			SuperConstructorInvocation superConstructorInvocation = (SuperConstructorInvocation)statement;
			if(instanceChecker.instanceOf(superConstructorInvocation))
				statementList.add(superConstructorInvocation);
		}
		
		return statementList;
	}
}