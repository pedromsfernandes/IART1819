from utils import getNumLevels

import tkinter as tk                # python 3
from tkinter import font as tkfont  # python 3
from game import BloxorzGame
import time

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

        self.algorithms = ["DFS","BFS", "UCS", "A*", "IDDFS","GS"]
        self.i = 0
        tk.Frame.__init__(self, parent)

        self.row, self.col = 0, 0
        self.colors = {
            "E": "gray",
            "O": "red",
            "X": "white",
        }

        self.keyToAction = {
            "i": "Up",
            "k": "Down",
            "j": "Left",
            "l": "Right",
        }

        self.__initUI(controller)

    def prevCallback(self, algorithm):
        if self.i > 0:
            self.i -= 1
        else:
            self.i = len(self.algorithms) - 1
        algorithm.set(self.algorithms[self.i])

    def nextCallback(self, algorithm):
        if self.i < len(self.algorithms) - 1:
            self.i += 1
        else:
            self.i = 0
        algorithm.set(self.algorithms[self.i])

    def tip(self):
        tip = self.game.tip(self.algorithms[self.i])
        tk.Label(self, text=tip).pack()

    def solve(self):
        start = time.time()
        (goalNode, numNodes) = self.game.solve(self.algorithms[self.i])
        end = time.time()
        duration = end - start

        actions = goalNode.solution()
        print(actions)
        self.solveAnim(actions)

        tk.Label(self, text="Statistics").pack()
        tk.Label(self, text="Expanded nodes").pack()
        self.nodes = tk.StringVar()
        self.nodes.set(numNodes)
        self.time = tk.StringVar()
        self.time.set(str(duration) + " s")
        tk.Label(self, textvariable=self.nodes).pack()
        tk.Label(self, text="Time:").pack()
        tk.Label(self, textvariable=self.time).pack()

    def __initUI(self, controller):
        numLines = len(self.game.start_board)
        numColumns = len(self.game.start_board[0])

        algorithm = tk.StringVar()
        algorithm.set(self.algorithms[self.i])

        self.canvas = tk.Canvas(self,
                                width=MARGIN * 2 + SIDE * numColumns,
                                height=MARGIN * 2 + SIDE * numLines)
        self.canvas.pack(fill=tk.BOTH, side=tk.TOP)

        self.__draw_grid()

        self.numMovements = tk.StringVar()
        self.numMovements.set("0")
        tk.Label(self, textvariable=self.numMovements).pack()

        tk.Button(self, text="Solve", command=self.solve).pack()
        tk.Button(self, text="Get a tip", command=self.tip).pack()

        tk.Label(self, textvariable=algorithm).pack()
        tk.Button(self, text="Previous",
                  command=lambda: self.prevCallback(algorithm)).pack()
        tk.Button(self, text="Next",
                  command=lambda: self.nextCallback(algorithm)).pack()

        tk.Button(self, text="Exit",
                  command=lambda: controller.show_frame(MainMenu)).pack()

        self.canvas.bind("<Key>", self.key)
        self.canvas.bind("<Button-1>", self.callback)

    def validMove(self, initState, afterState):
        print("todo")

    def solveAnim(self, solution):

        for action in solution:
            self.numMovements.set(str(self.game.move(action)))

            self.__draw_grid()
            self.update()
            time.sleep(0.5)

    def key(self, event):
        print("pressed", repr(event.char))
        if not self.game.gameOver:
            self.numMovements.set(
                str(self.game.move(self.keyToAction[event.char])))
            self.__draw_grid()

    def callback(self, event):
        self.canvas.focus_set()
        print("clicked at", event.x, event.y)

    def __draw_grid(self):
        numLines = len(self.game.start_board)
        numColumns = len(self.game.start_board[0])
        y0 = MARGIN
        x0 = MARGIN

        for i in range(numLines):
            for j in range(numColumns):
                x1 = x0 + SIDE
                y1 = y0 + SIDE

                color = ""
                place = self.game.state.board[i][j]
                if place in self.colors:
                    color = self.colors[place]
                elif place.isupper():
                    if place < "G":
                        color = "orange"
                    else:
                        color = "yellow"
                elif place.islower():
                    color = "green" if self.game.state.togglers[place] else "gray"
                else:
                    color = "gray"

                self.canvas.create_rectangle(
                    x0, y0, x1, y1, fill=color)

                x0 += SIDE
            x0 = MARGIN
            y0 += SIDE

        x0 = MARGIN + SIDE * self.game.state.blockCoords[1]
        y0 = MARGIN + SIDE * self.game.state.blockCoords[0]

        self.canvas.create_rectangle(
            x0, y0, x0 + SIDE, y0 + SIDE, fill="blue")

        x0 = MARGIN + SIDE * self.game.state.blockCoords[3]
        y0 = MARGIN + SIDE * self.game.state.blockCoords[2]

        self.canvas.create_rectangle(
            x0, y0, x0 + SIDE, y0 + SIDE, fill="blue")
