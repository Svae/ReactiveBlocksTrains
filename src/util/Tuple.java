package util;

public class Tuple<T,S> {
	
	private T value1;
	private S value2;

	public Tuple(T value1, S value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	public T getValue1() {
		return value1;
	}

	public void setValue1(T name) {
		this.value1 = name;
	}

	public S getValue2(){
		return value2;
	}
	
	public void setValue2(S value){
		this.value2 = value;
	}
}

