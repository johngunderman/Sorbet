package client;

public class Main {
	
	static int foo = 0;
	static int foobar = 9;
	
	public static void main(String[] args) {
		 
		foo = 3 + 3;
		
		int b = 0 + 2;
		
		for (int a = 0; a < 2; a++) {
			foo++;
			b++;
		}
		
		foobar--;
		
		String i = "hi!";
		int j = 0;
		
		bar();
		
		foo = 5;
		
		foo = foo + 5;
		
		if (foo == 5) {
			
		}
		else {
			
		}
		
		double randomVariableOne = Math.random();
		double randomVariableTwo = Math.random();
		
		Test test = new Test();
		
		System.out.println("Client program " + test.getTest());
	}
	
	public static void bar() {
		int i = 2;
		
		int j = 6;
		
		foobar += i + j;
	}
}
