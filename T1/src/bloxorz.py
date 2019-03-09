from tkinter import Tk, Label, Button
from gui import Bloxorz

MARGIN = 20  # Pixels around the board
SIDE = 50  # Width of every board cell.
WIDTH = MARGIN * 2 + SIDE * 11  # Width and height of the whole board
HEIGHT = MARGIN * 2 + SIDE * 9

def main():
    app = Bloxorz()
    app.mainloop()

if __name__ == "__main__":
    main()
