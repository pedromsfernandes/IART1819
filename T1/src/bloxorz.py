from menu import mainMenu
from tkinter import Tk, Label, Button, StringVar
from game import BloxorzGame
from gui import BloxorzUI
from utils import getNumLevels

MARGIN = 20  # Pixels around the board
SIDE = 50  # Width of every board cell.
WIDTH = MARGIN * 2 + SIDE * 11  # Width and height of the whole board
HEIGHT = MARGIN * 2 + SIDE * 9

levelInt = 1


def main():
    global levelInt
    numLevels = getNumLevels()
    root = Tk()
    level = StringVar()
    level.set(str(levelInt))
    w = Label(root, textvariable=level)
    w.pack()
    Button(root, text="Previous", command=lambda: prevCallback(
        level, numLevels)).pack()
    Button(root, text="Next", command=lambda: nextCallback(
        level, numLevels)).pack()
    Button(root, text="Play", command=lambda: startLevel(root)).pack()
    root.geometry("%dx%d" % (WIDTH, HEIGHT + 40))
    root.mainloop()


def startLevel(root):
    game = BloxorzGame("../res/levels/level" + str(levelInt) + ".txt")
    game.start()
    BloxorzUI(root, game)


def nextCallback(level, numLevels):
    global levelInt
    if levelInt < numLevels:
        levelInt += 1
    else:
        levelInt = 1
    level.set(str(levelInt))


def prevCallback(level, numLevels):
    global levelInt
    if levelInt > 1:
        levelInt -= 1
    else:
        levelInt = numLevels
    level.set(str(levelInt))


if __name__ == "__main__":
    main()
