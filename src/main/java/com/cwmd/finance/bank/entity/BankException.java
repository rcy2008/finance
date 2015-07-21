package com.cwmd.finance.bank.entity;

public class BankException extends Exception{

	private static final long serialVersionUID = -8344206263747700565L;

	public BankException(){
		super();
	}
	
	public BankException(String msg){
		super(msg);
	}
}
