from utils import getNumLevels

import tkinter as tk                # python 3
from tkinter import font as tkfont  # python 3
from game import BloxorzGame, h2
import time

MARGIN = 20  # Pixels around the board
WIDTH = HEIGHT = MARGIN * 2 + 300  # Width and height of the whole board


levelInt = 1 # The level currently chosen by the user

"""
Callback used when the user presses the next level button. Updates the levelInt variable,
so that the user knows what level he is currently choosing.
"""
def nextLevelCallback(level, numLevels):
    global levelInt
    if levelInt < numLevels:
        levelInt += 1
    else:
        levelInt = 1
    level.set(str(levelInt))


"""
Callback used when the user presses the previous level button. Updates the levelInt variable,
so that the user knows what level he is currently choosing.
"""
def prevLevelCallback(level, numLevels):
    global levelInt
    if levelInt > 1:
        levelInt -= 1
    else:
        levelInt = numLevels
    level.set(str(levelInt))

"""
Main class for the GUI, responsible for holding references to each of the windows: main, legend and game.
"""
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

        # Initialize main menu frame and show it

        mainMenuFrame = MainMenu(self.container, self)
        self.frames[MainMenu] = mainMenuFrame
        mainMenuFrame.grid(row=0, column=0, sticky="nsew")

        self.show_frame(MainMenu)

    def show_frame(self, page_name):
        '''Show a frame for the given page name'''
        frame = self.frames[page_name]
        frame.tkraise()

    # Initializes the legend frame and shows it
    def showLegend(self):
        legendFrame = Legend(self.container, self)
        self.frames[Legend] = legendFrame
        legendFrame.grid(row=0, column=0, sticky="nsew")

        self.show_frame(Legend)

    # Initializes the chosen level, and shows it
    def initGame(self):
        game = BloxorzGame("../res/levels/level" + str(levelInt) + ".txt")

        bloxorzUIFrame = BloxorzUI(self.container, self, game)
        self.frames[BloxorzUI] = bloxorzUIFrame
        bloxorzUIFrame.grid(row=0, column=0, sticky="nsew")

        self.show_frame(BloxorzUI)


"""
Window used to illustrate the meaning of each color of the board to the user.
"""
class Legend(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        self.controller = controller

        tk.Label(self, text="Blue - Block", fg="blue").pack()
        tk.Label(self, text="Red - Goal", fg="red").pack()
        tk.Label(self, text="Orange - Toggler", fg="orange").pack()
        tk.Label(self, text="Yellow - Vertical toggler", fg="yellow").pack()
        tk.Label(self, text="White - Normal", fg="white").pack()
        tk.Label(self, text="Gray - Empty", fg="gray").pack()
        tk.Label(self, text="Green - Activated by toggler", fg="green").pack()
        tk.Label(self, text="Black - Teleport: to", fg="black").pack()
        tk.Label(self, text="Purple - Teleport: from", fg="purple").pack()
        tk.Label(self, text="Gold - Fall if standing", fg="gold").pack()
        
        tk.Button(self, text="Exit",
                  command=lambda: controller.show_frame(MainMenu)).pack()

"""
Main menu. Allows the user to choose a level, to access the legend window, or leave.
"""
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
        tk.Label(self, textvariable=level).pack()
        tk.Button(self, text="Previous", command=lambda: prevLevelCallback(
            level, numLevels)).pack()
        tk.Button(self, text="Next", command=lambda: nextLevelCallback(
            level, numLevels)).pack()

        tk.Button(self, text="Play",
                  command=lambda: controller.initGame()).pack()
        tk.Button(self, text="Legend",
                  command=lambda: controller.showLegend()).pack()

"""
Window representing a level of the game. Shows the board and allows the user to choose an algorithm,
solve the level or get a tip for the next movement, and also shows the statistics for the solution.
"""
class BloxorzUI(tk.Frame):
    def __init__(self, parent, controller, game):
        self.game = game
        self.controller = controller
        self.parent = parent

        self.algorithms = ["DFS", "BFS", "UCS", "A*", "IDDFS", "GS"]
        self.i = 0
        tk.Frame.__init__(self, parent)

        self.row, self.col = 0, 0
        self.colors = {
            "E": "gray",
            "O": "red",
            "X": "white",
            "F": "gold",
        }

        self.keyToAction = {
            "w": "Up",
            "s": "Down",
            "a": "Left",
            "d": "Right",
        }

        self.__initUI(controller)

    def prevAlgCallback(self, algorithm):
        if self.i > 0:
            self.i -= 1
        else:
            self.i = len(self.algorithms) - 1
        algorithm.set(self.algorithms[self.i])

    def nextAlgCallback(self, algorithm):
        if self.i < len(self.algorithms) - 1:
            self.i += 1
        else:
            self.i = 0
        algorithm.set(self.algorithms[self.i])

    def tip(self):
        self.tip.set(self.game.tip(self.algorithms[self.i]))

    def solve(self):
        start = time.time()
        (goalNode, numNodes) = self.game.solve(self.algorithms[self.i], h2)

        if goalNode == {} and numNodes == 73:
            duration = 'TIMEOUT'
            numNodes = 0
        else:
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
        self.SIDE = 300 / numColumns
        algorithm = tk.StringVar()
        algorithm.set(self.algorithms[self.i])

        self.canvas = tk.Canvas(self,
                                width=MARGIN * 2 + self.SIDE * numColumns,
                                height=MARGIN * 2 + self.SIDE * numLines)
        self.canvas.pack(fill=tk.BOTH, side=tk.TOP)

        self.__draw_grid()

        self.numMovements = tk.StringVar()
        self.numMovements.set("0")
        tk.Label(self, textvariable=self.numMovements).pack()

        tk.Button(self, text="Solve", command=self.solve).pack()
        tk.Button(self, text="Get a tip", command=self.tip).pack()

        tk.Label(self, textvariable=algorithm).pack()
        tk.Button(self, text="Previous",
                  command=lambda: self.prevAlgCallback(algorithm)).pack()
        tk.Button(self, text="Next",
                  command=lambda: self.nextAlgCallback(algorithm)).pack()

        tk.Button(self, text="Exit",
                  command=lambda: controller.show_frame(MainMenu)).pack()

        self.tip = tk.StringVar()
        self.tip.set("Tip")
        tk.Label(self, textvariable=self.tip).pack()

        self.canvas.bind("<Key>", self.key)
        self.canvas.bind("<Button-1>", self.mouse)
    
    def solveAnim(self, solution):

        for action in solution:
            self.numMovements.set(str(self.game.move(action)))

            self.__draw_grid()
            self.update()
            time.sleep(0.5)

    def key(self, event):
        if not self.game.gameOver:
            self.numMovements.set(
                str(self.game.move(self.keyToAction[event.char])))
            self.__draw_grid()

    def mouse(self, event):
        self.canvas.focus_set()

    def __draw_grid(self):
        numLines = len(self.game.start_board)
        numColumns = len(self.game.start_board[0])
        y0 = MARGIN
        x0 = MARGIN

        for i in range(numLines):
            for j in range(numColumns):
                x1 = x0 + self.SIDE
                y1 = y0 + self.SIDE

                color = ""
                place = self.game.state.board[i][j]
                if place in self.colors:
                    color = self.colors[place]
                elif place.isupper():
                    color = "orange" if place < "G" else "yellow"
                elif place.islower():
                    color = "green" if self.game.state.togglers[place] else "gray"
                elif place.isnumeric():
                    color = "black" if int(place) % 2 == 0 else "purple"
                else:
                    color = "gray"

                self.canvas.create_rectangle(
                    x0, y0, x1, y1, fill=color)

                if place == '>':
                    points = [x0, y0, x0, y1, x1, (y0 + y1)/2]
                    self.canvas.create_polygon(points, fill="blue")
                elif place == '<':
                    points = [x1, y0, x1, y1, x0, (y0 + y1)/2]
                    self.canvas.create_polygon(points, fill="blue")
                elif place == '^':
                    points = [x0, y1, x1, y1, (x0 + x1)/2, y0]
                    self.canvas.create_polygon(points, fill="blue")
                elif place == '_':
                    points = [x0, y0, x1, y0, (x0 + x1)/2, y1]
                    self.canvas.create_polygon(points, fill="blue")

                x0 += self.SIDE
            x0 = MARGIN
            y0 += self.SIDE

        x0 = MARGIN + self.SIDE * self.game.state.blockCoords[1]
        y0 = MARGIN + self.SIDE * self.game.state.blockCoords[0]

        self.canvas.create_rectangle(
            x0, y0, x0 + self.SIDE, y0 + self.SIDE, fill="blue")

        x0 = MARGIN + self.SIDE * self.game.state.blockCoords[3]
        y0 = MARGIN + self.SIDE * self.game.state.blockCoords[2]

        self.canvas.create_rectangle(
            x0, y0, x0 + self.SIDE, y0 + self.SIDE, fill="blue")
