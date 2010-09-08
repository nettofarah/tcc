package br.ufla.dcc.ut.command;

public class UnbuiltCommandFactoryException extends Exception {
	
	public UnbuiltCommandFactoryException(){
		super("This factory is not built yet. \n Try calling buildFactory(); method before using any other method on this class.");
	}
}
