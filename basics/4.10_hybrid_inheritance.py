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

# In the above example, the Bat class inherits from both the Mammal and Bird classes. This is an example of Hybrid Inheritance. The Bat class has access to the eat method from the Animal class, as well as the feed_milk method from the Mammal class and the layEggs method from the Bird class.
