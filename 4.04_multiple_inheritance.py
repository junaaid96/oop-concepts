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

# In the above example, the Bat class inherits from both the Mammal and Bird classes. This is an example of Multiple Inheritance. The Bat class has access to the feed_milk method from the Mammal class and the lay_eggs method from the Bird class.
# Multiple Inheritance can be useful for creating complex class hierarchies, but it can also lead to ambiguity and complexity. It is important to carefully design class hierarchies when using Multiple Inheritance to avoid potential issues.