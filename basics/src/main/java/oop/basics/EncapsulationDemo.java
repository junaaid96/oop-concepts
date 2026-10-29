package oop.basics;

/** 5. Encapsulation with access modifiers: public, protected, package-private (none), private. */
public class EncapsulationDemo {
    static class MyClass {
        public int publicVar;            // anyone can read/write
        private int privateVar;          // only code inside MyClass

        public void setVar(int v) {
            if (v < 0) throw new IllegalArgumentException("must be >= 0");   // setters can validate
            privateVar = v;
        }
        public int getVar() { return privateVar; }
    }

    public static void main(String[] args) {
        var obj = new MyClass();
        obj.publicVar = 10;
        obj.setVar(10);
        // obj.privateVar = 1;  // compile error: privateVar has private access in MyClass
        System.out.println(obj.publicVar + " " + obj.getVar());
    }
}
