from menu import mainMenu
from tkinter import Tk, Label, Button, StringVar
from game import BloxorzGame
from gui import BloxorzUI

MARGIN = 20  # Pixels around the board
SIDE = 50  # Width of every board cell.
WIDTH = MARGIN * 2 + SIDE * 11  # Width and height of the whole board
HEIGHT = MARGIN * 2 + SIDE * 9

levelInt = 1


def main():
    global levelInt
    root = Tk()
    level = StringVar()
    level.set(str(levelInt))
    w = Label(root, textvariable=level)
    w.pack()
    Button(root, text="Previous", command=lambda: prevCallback(level)).pack()
    Button(root, text="Next", command=lambda: nextCallback(level)).pack()
    Button(root, text="Play", command=lambda: startLevel(root)).pack()
    root.geometry("%dx%d" % (WIDTH, HEIGHT + 40))
    root.mainloop()

def startLevel(root):
    game = BloxorzGame("../res/levels/level" + str(levelInt) + ".txt")
    game.start()
    BloxorzUI(root, game)

def nextCallback(level):
    global levelInt
    if levelInt < 5:
        levelInt += 1
        level.set(str(levelInt))


def prevCallback(level):
    global levelInt
    if levelInt > 0:
        levelInt -= 1
        level.set(str(levelInt))


if __name__ == "__main__":
    main()
