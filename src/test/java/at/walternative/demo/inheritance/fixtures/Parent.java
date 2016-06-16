package at.walternative.demo.inheritance.fixtures;

public class Parent {

    public String doSomething() {
        return "Hello from parent";
    }

    public int calculate(int a) {
        return a - 1;
    }

    public int calculate(Integer a) {
        return a + 1;
    }
}
