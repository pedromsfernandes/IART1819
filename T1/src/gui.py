from utils import getNumLevels

import tkinter as tk                # python 3
from tkinter import font as tkfont  # python 3
from game import BloxorzGame

MARGIN = 20  # Pixels around the board
SIDE = 50  # Width of every board cell.
WIDTH = HEIGHT = MARGIN * 2 + SIDE * 9  # Width and height of the whole board
levelInt = 1


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


class Bloxorz(tk.Tk):

    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)

        self.title_font = tkfont.Font(
            family='Helvetica', size=18, weight="bold", slant="italic")

        # the container is where we'll stack a bunch of frames
        # on top of each other, then the one we want visible
        # will be raised above the others
        self.container = tk.Frame(self)
        self.container.pack(side="top", fill="both", expand=True)
        self.container.grid_rowconfigure(0, weight=1)
        self.container.grid_columnconfigure(0, weight=1)

        self.frames = {}

        mainMenuFrame = MainMenu(self.container, self)
        self.frames[MainMenu] = mainMenuFrame
        mainMenuFrame.grid(row=0, column=0, sticky="nsew")

        self.show_frame(MainMenu)

    def show_frame(self, page_name):
        '''Show a frame for the given page name'''
        frame = self.frames[page_name]
        frame.tkraise()

    def initGame(self):
        game = BloxorzGame("../res/levels/level" + str(levelInt) + ".txt")
        game.start()

        bloxorzUIFrame = BloxorzUI(self.container, self, game)
        self.frames[BloxorzUI] = bloxorzUIFrame
        bloxorzUIFrame.grid(row=0, column=0, sticky="nsew")

        self.show_frame(BloxorzUI)


class MainMenu(tk.Frame):
    def __init__(self, parent, controller):

        global levelInt
        numLevels = getNumLevels()

        tk.Frame.__init__(self, parent)
        self.controller = controller
        label = tk.Label(self, text="Welcome to Bloxorz!",
                         font=controller.title_font)
        label.pack(side="top", fill="x", pady=10)

        level = tk.StringVar()
        level.set(str(levelInt))
        w = tk.Label(self, textvariable=level)
        w.pack()
        tk.Button(self, text="Previous", command=lambda: prevCallback(
            level, numLevels)).pack()
        tk.Button(self, text="Next", command=lambda: nextCallback(
            level, numLevels)).pack()

        button2 = tk.Button(self, text="Play",
                            command=lambda: controller.initGame())
        button2.pack()


class BloxorzUI(tk.Frame):
    def __init__(self, parent, controller, game):
        self.game = game
        self.controller = controller
        self.parent = parent
        tk.Frame.__init__(self, parent)

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

        self.__initUI(controller)

    def __initUI(self, controller):
        numLines = len(self.game.puzzle)
        numColumns = len(self.game.puzzle[0])

        self.canvas = tk.Canvas(self,
                                width= MARGIN * 2 + SIDE * numColumns,
                                height= MARGIN * 2 + SIDE * numLines)
        self.canvas.pack(fill=tk.BOTH, side=tk.TOP)

        self.__draw_grid()

        tk.Button(self, text="Exit", command=lambda: controller.show_frame(MainMenu)).pack()
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
                self.canvas.create_rectangle(
                    x0, y0, x1, y1, fill=self.colors[self.game.puzzle[i][j]])
                x0 += SIDE
            x0 = MARGIN
            y0 += SIDE
