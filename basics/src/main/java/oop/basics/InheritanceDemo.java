package oop.basics;

/**
 * 4. Inheritance. Java has single, multilevel and hierarchical CLASS inheritance.
 * Multiple/hybrid inheritance of TYPE is done with interfaces (default methods), because
 * a class may extend only one class (no diamond problem for state).
 */
public class InheritanceDemo {
    static class Base { String display() { return "Base display"; } }
    static class Derived extends Base {}                                     // single

    static class Animal { String eat() { return "Eating"; } }
    static class Dog extends Animal { String bark() { return "Woof!"; } }    // hierarchical: Dog and Cat
    static class Cat extends Animal { String meow() { return "Meow!"; } }
    static class Puppy extends Dog { String weep() { return "Weeping"; } }   // multilevel

    interface Mammal { default String feedMilk() { return "Feeding milk"; } }
    interface Bird { default String layEggs() { return "Laying eggs"; } }
    static class Bat extends Animal implements Mammal, Bird {}               // multiple + hybrid

    public static void main(String[] args) {
        System.out.println(new Derived().display());
        var p = new Puppy();
        System.out.println(p.eat() + " " + p.bark() + " " + p.weep());
        System.out.println(new Cat().meow());
        var bat = new Bat();
        System.out.println(bat.eat() + " " + bat.feedMilk() + " " + bat.layEggs());
    }
}
