package client;

public class Demo {
	
	public static int i;
	
	public static void main(String[] args) {
		//int i;
		
		i = 3 + 6;
		
		String abc;
		
		abc = "i = " + i;
		
		int j = test() + i;
	}
	
	public static int test() {
		return 2;
	}

}