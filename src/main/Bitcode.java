package main;

public class Bitcode {
	private char code;
	private int value;
	private char numOfBit;
	public Bitcode(char code, int value, char numOfBit) {
		super();
		this.code = code;
		this.value = value;
		this.numOfBit = numOfBit;
	}
	
	public void printCodeWord() {
		String stringCodeWord = Integer.toBinaryString(code);
		System.out.print((int)code+ ":"+stringCodeWord+"\t");
	}
	public void myPrintCodeWord() {
		System.out.print(value + ":" + codeWord2BinaryString()+"\t");
	}
	public void myPrintCodeWord(int i) {
		System.out.print(Integer.toHexString((int)i) + ":" + codeWord2BinaryString()+"\t");
	}
	public String codeWord2BinaryString() {
		
		StringBuilder reverseResult = new StringBuilder();
		StringBuilder result = new StringBuilder();
		int number = code;
		int redun = 0;
		
		while(number>0) {
			redun = number % 2;
			if(redun==0) reverseResult.append("0"); else reverseResult.append("1");
			number = number/2;
		}
		if(reverseResult.length()<numOfBit) {
			
			int offset = numOfBit - reverseResult.length();
			for(int i = 0; i<offset; i++) {
				reverseResult.append("0");
			}
		}

		for(int i=reverseResult.length()-1; i>=0; i--) result.append(reverseResult.charAt(i));
		return result.toString();
	}
	public char getCode() {
		return code;
	}
	public void setCode(char code) {
		this.code = code;
	}
	public char getNumOfBit() {
		return numOfBit;
	}
	public void setNumOfBit(char numOfBit) {
		this.numOfBit = numOfBit;
	}
	
	
}
