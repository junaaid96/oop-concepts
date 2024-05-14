# Hybrid Inheritance is a combination of two or more types of inheritance.

class Animal:
    def eat(self):
        print("Eating")


class Mammal(Animal):
    def feed_milk(self):
        print("Feeding milk")


class Bird(Animal):
    def lay_eggs(self):
        print("Laying eggs")


class Bat(Mammal, Bird):
    pass


bat = Bat()
bat.feed_milk()
bat.lay_eggs()
bat.eat()
