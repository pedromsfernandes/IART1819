from menu import mainMenu
from tkinter import Tk
from game import BloxorzGame
from gui import BloxorzUI

MARGIN = 20  # Pixels around the board
SIDE = 50  # Width of every board cell.
WIDTH = MARGIN * 2 + SIDE * 11  # Width and height of the whole board
HEIGHT = MARGIN * 2 + SIDE * 9
def main():
    mainMenu()

if __name__ == "__main__":
    game = BloxorzGame("../res/levels/level1.txt")
    game.start()
    root = Tk()
    BloxorzUI(root, game)
    root.geometry("%dx%d" % (WIDTH, HEIGHT + 40))
    root.mainloop()

