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

# In the above example, the Puppy class inherits from the Dog class, which in turn inherits from the Animal class. This is an example of Multilevel Inheritance. The Puppy class has access to the eat method from the Animal class, as well as the bark method from the Dog class. The Puppy class also defines its own method called weep. 