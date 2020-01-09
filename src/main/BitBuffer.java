package main;

public class BitBuffer {
	private int data;
	private char numbits;
	public BitBuffer(int data, char numbits) {
		super();
		this.data = data;
		this.numbits = numbits;
	}
	
	public void printBufferData() {
		// System.out.println("buffer: "+codeWord2BinaryString()+"\t"+(int)numbits);
	}
	
	public String codeWord2BinaryString() {
		int code = data;
		StringBuilder reverseResult = new StringBuilder();
		StringBuilder result = new StringBuilder();
		int number = data;
		number = number&createMaks(numbits);
		int redun = 0;
		
		while(number>0) {
			redun = number % 2;
			if(redun==0) reverseResult.append("0"); else reverseResult.append("1");
			number = number/2;
		}
		if(reverseResult.length()<numbits) {
			
			int offset = numbits - reverseResult.length();
			for(int i = 0; i<offset; i++) {
				reverseResult.append("0");
			}
		}

		for(int i=reverseResult.length()-1; i>=0; i--) result.append(reverseResult.charAt(i));
		return result.toString();
	}
	private int createMaks(char numbits) {
		int mask = 1;
		for(int i=1; i<numbits; i++) mask = (mask<<1)|1;
		return mask;
	}
	
	public void writeAllFullBits() {
		
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	public char getNumbits() {
		return numbits;
	}
	public void setNumbits(char numbits) {
		this.numbits = numbits;
	}
	
	
}
