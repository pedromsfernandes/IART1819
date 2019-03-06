from game import readLevel
from utils import cls


def mainMenu():
    print("Welcome to Bloxorz!")
    print("Developed by @FilipaDurao, @pedromiguelsilva and @mrzephyr17")

    level = 0
    while level < 1 or level > 5:
        level = int(input("Choose a level (1-5): "))

    cls()
    readLevel(level)
