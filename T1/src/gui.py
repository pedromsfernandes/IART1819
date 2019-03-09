from tkinter import Tk, Canvas, Frame, Button, BOTH, TOP, BOTTOM

MARGIN = 20  # Pixels around the board
SIDE = 50  # Width of every board cell.
WIDTH = HEIGHT = MARGIN * 2 + SIDE * 9  # Width and height of the whole board


class BloxorzUI(Frame):
    def __init__(self, parent, game):
        self.game = game
        self.parent = parent
        Frame.__init__(self, parent)

        self.row, self.col = 0, 0
        self.colors = {
            "X": "gray",
            "V": "blue",
            "H": "blue",
            "O": "red",
            "E": "white",
            "F": "white",
            "A": "green",
            "T": "orange"
        }

        self.__initUI()

    def __initUI(self):
        self.parent.title("Sudoku")
        self.pack(fill=BOTH, expand=1)
        self.canvas = Canvas(self,
                             width=WIDTH,
                             height=HEIGHT)
        self.canvas.pack(fill=BOTH, side=TOP)

        self.__draw_grid()
        # self.__draw_puzzle()

        # self.canvas.bind("<Button-1>", self.__cell_clicked)
        # self.canvas.bind("<Key>", self.__key_pressed)

    def __draw_grid(self):
        numLines = len(self.game.puzzle)
        numColumns = len(self.game.puzzle[0])
        y0 = MARGIN
        x0 = MARGIN

        for i in range(numLines):
            for j in range(numColumns):
                x1 = x0 + SIDE
                y1 = y0 + SIDE
                self.canvas.create_rectangle(x0, y0, x1, y1, fill=self.colors[self.game.puzzle[i][j]])
                x0 += SIDE
            x0 = MARGIN
            y0 += SIDE
