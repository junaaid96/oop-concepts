# Multilevel Inheritance involves a hierarchy where a derived class inherits from a base class which in turn is a derived class of another base class.

class Animal:
    def eat(self):
        print("Eating")


class Dog(Animal):
    def bark(self):
        print("Woof!")


class Puppy(Dog):
    def weep(self):
        print("Weeping")


puppy = Puppy()
puppy.eat()
puppy.bark()
puppy.weep()
