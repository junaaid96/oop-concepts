package oop.basics;

/** 1. A class is a blueprint: state (fields) + behaviour (methods). Pairs with 1.02_class.cpp. */
public class ClassDemo {
    static class Car {
        private final String brand;
        private final String model;
        Car(String brand, String model) { this.brand = brand; this.model = model; }
        String startEngine() { return "Engine started!"; }
        String stopEngine() { return "Engine stopped!"; }
        String drive() { return "Driving a " + brand + " " + model + "..."; }
    }

    public static void main(String[] args) {
        var car = new Car("Toyota", "Camry");   // 2. an object is one instance of the class
        System.out.println(car.startEngine());
        System.out.println(car.drive());
        System.out.println(car.stopEngine());
    }
}
