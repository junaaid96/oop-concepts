# Multiple Inheritance occurs when a class inherits from more than one base class.

class Mammal:
    def feed_milk(self):
        print("Feeding milk")


class Bird:
    def lay_eggs(self):
        print("Laying eggs")


class Bat(Mammal, Bird):
    pass


bat = Bat()
bat.feed_milk()
bat.lay_eggs()
