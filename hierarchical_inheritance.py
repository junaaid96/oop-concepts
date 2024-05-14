# Hierarchical Inheritance occurs when multiple classes inherit from a single base class.

class Animal:
    def eat(self):
        print("Eating")


class Dog(Animal):
    def bark(self):
        print("Woof!")


class Cat(Animal):
    def meow(self):
        print("Meow!")


dog = Dog()
dog.eat()
dog.bark()

cat = Cat()
cat.eat()
cat.meow()
