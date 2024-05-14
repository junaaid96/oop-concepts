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

# In the above example, the Animal class is the base class, and the Dog and Cat classes inherit from it. This is an example of Hierarchical Inheritance. The Dog and Cat classes have access to the eat method defined in the Animal class. They can also define their own methods like bark and meow.
# Hierarchical Inheritance allows for code reuse and promotes code organization by grouping related classes together.
