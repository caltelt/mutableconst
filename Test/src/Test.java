public class Test {

	
	
	public static void main(String[] args) {
		new Test();
	}
	
	public Test() {
		new Jon();
		new Test2();
	}

};

class Jon {

	public Jon() {
		System.out.println("JOn");
	}
}
