from array import *


class BloxorzBoard(object):
    def __init__(self, board_file):
        self.board = self.__create_board(board_file)

    def __create_board(self, board_file):
        file = open(board_file, "r")
        lines = file.readlines()

        board = []
        for line in lines:
            line = line.strip()
            board.append([])

            for c in line:
                board[-1].append(c)

        file.close()
        return board


class BloxorzGame(object):

    def __init__(self, board_file):
        self.board_file = board_file
        self.start_puzzle = BloxorzBoard(board_file).board

        self.keyEvents = {
            "i": self.moveUp,
            "k": self.moveDown,
            "j": self.moveLeft,
            "l": self.moveRight,
        }

        self.validators = {
            "i": self.validateUp,
            "k": self.validateDown,
            "j": self.validateLeft,
            "l": self.validateRight,
        }

    def start(self):
        self.game_over = False
        self.puzzle = []
        for i in range(len(self.start_puzzle)):
            self.puzzle.append([])
            for j in range(len(self.start_puzzle[0])):
                if self.start_puzzle[i][j] == "V":
                    self.blockCoords = [i, j, i, j, "V"]
                elif self.start_puzzle[i][j] == "O":
                    self.solutionCoords = [i, j]
                self.puzzle[i].append(self.start_puzzle[i][j])

    def validateMove(self, direction):
        return self.validators[direction]()

    def move(self, direction):
        if self.validateMove(direction):
            self.keyEvents[direction]()
            if self.testSolution():
                print('oi')

    def validateUp(self):
        if self.blockCoords[4] == "V":
            if self.blockCoords[0] - 2 >= 0 and self.puzzle[self.blockCoords[0] - 2
                                                            ][self.blockCoords[1]] != "E" and self.puzzle[self.blockCoords[2] - 1
                                                                                                          ][self.blockCoords[3]] != "E":
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            if self.blockCoords[0] - 1 >= 0 and self.puzzle[self.blockCoords[0] - 1
                                                            ][self.blockCoords[1]] != "E" and self.puzzle[self.blockCoords[2] - 1
                                                                                                          ][self.blockCoords[3]] != "E":
                return True
        else:
            if self.blockCoords[0] - 1 >= 0 and self.puzzle[self.blockCoords[0] - 1
                                                            ][self.blockCoords[1]] != "E":
                return True

        return False

    def validateDown(self):
        length = len(self.puzzle)
        print(self.blockCoords)
        if self.blockCoords[4] == "V":
            if self.blockCoords[0] + 2 < length and self.puzzle[self.blockCoords[0] + 2
                                                                 ][self.blockCoords[1]] != "E" and self.puzzle[self.blockCoords[2] + 1
                                                                                                               ][self.blockCoords[3]] != "E":
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            if self.blockCoords[0] + 1 < length and self.puzzle[self.blockCoords[0] + 1
                                                                 ][self.blockCoords[1]] != "E" and self.puzzle[self.blockCoords[2] + 1
                                                                                                               ][self.blockCoords[3]] != "E":
                return True
        else:
            if self.blockCoords[0] + 1 < length and self.puzzle[self.blockCoords[0] + 2
                                                                 ][self.blockCoords[1]] != "E":
                return True

        return False

    def validateLeft(self):
        if self.blockCoords[4] == "V":
            if self.blockCoords[1] - 2 >= 0 and self.puzzle[self.blockCoords[0]
                                                            ][self.blockCoords[1] - 2] != "E" and self.puzzle[self.blockCoords[2]
                                                                                                              ][self.blockCoords[3] - 1] != "E":
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            if self.blockCoords[1] - 1 >= 0 and self.puzzle[self.blockCoords[0]
                                                            ][self.blockCoords[1] - 1] != "E":
                return True
        else:
            if self.blockCoords[1] - 1 >= 0 and self.puzzle[self.blockCoords[0]
                                                            ][self.blockCoords[1] - 1] != "E" and self.puzzle[self.blockCoords[2]
                                                                                                              ][self.blockCoords[3] - 1] != "E":
                return True

        return False

    def validateRight(self):
        length = len(self.puzzle[0])
        if self.blockCoords[4] == "V":
            if self.blockCoords[3] + 2 < length and self.puzzle[self.blockCoords[0]
                                                                 ][self.blockCoords[1] + 1] != "E" and self.puzzle[self.blockCoords[2]
                                                                                                                   ][self.blockCoords[3] + 2] != "E":
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            if self.blockCoords[3] + 1 < length and self.puzzle[self.blockCoords[0]
                                                                 ][self.blockCoords[1] + 2] != "E":
                return True
        else:
            if self.blockCoords[3] + 1 < length and self.puzzle[self.blockCoords[0]
                                                                 ][self.blockCoords[1] + 1] != "E" and self.puzzle[self.blockCoords[2]
                                                                                                                   ][self.blockCoords[3] + 1] != "E":
                return True

        return False

    def moveUp(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[0] -= 2
            self.blockCoords[2] -= 1
            self.blockCoords[4] = "H"
            self.puzzle[self.blockCoords[0]
                        ][self.blockCoords[1]] = "H"
            self.puzzle[self.blockCoords[2]
                        ][self.blockCoords[3]] = "H"
            self.puzzle[self.blockCoords[2] +
                        1][self.blockCoords[3]] = "X"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[0] -= 1
                self.blockCoords[2] -= 1
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "H"
                self.puzzle[self.blockCoords[2]
                            ][self.blockCoords[3]] = "H"
                self.puzzle[self.blockCoords[0] +
                            1][self.blockCoords[1]] = "X"
                self.puzzle[self.blockCoords[2] +
                            1][self.blockCoords[3]] = "X"
            else:
                self.blockCoords[0] -= 1
                self.blockCoords[2] -= 2
                self.blockCoords[4] = "V"
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "V"
                self.puzzle[self.blockCoords[2]
                            ][self.blockCoords[3]] = "V"
                self.puzzle[self.blockCoords[0] +
                            1][self.blockCoords[1]] = "X"
                self.puzzle[self.blockCoords[2] +
                            2][self.blockCoords[3]] = "X"

    def moveDown(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[0] += 1
            self.blockCoords[2] += 2
            self.blockCoords[4] = "H"
            self.puzzle[self.blockCoords[0]
                        ][self.blockCoords[1]] = "H"
            self.puzzle[self.blockCoords[2]
                        ][self.blockCoords[3]] = "H"
            self.puzzle[self.blockCoords[2] -
                        2][self.blockCoords[3]] = "X"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[0] += 1
                self.blockCoords[2] += 1
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "H"
                self.puzzle[self.blockCoords[2]
                            ][self.blockCoords[3]] = "H"
                self.puzzle[self.blockCoords[0] -
                            1][self.blockCoords[1]] = "X"
                self.puzzle[self.blockCoords[2] -
                            1][self.blockCoords[3]] = "X"
            else:
                self.blockCoords[0] += 2
                self.blockCoords[2] += 1
                self.blockCoords[4] = "V"
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "V"
                self.puzzle[self.blockCoords[0] -
                            2][self.blockCoords[1]] = "X"
                self.puzzle[self.blockCoords[2] -
                            1][self.blockCoords[3]] = "X"

    def moveLeft(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[1] -= 2
            self.blockCoords[3] -= 1
            self.blockCoords[4] = "H"
            self.puzzle[self.blockCoords[0]
                        ][self.blockCoords[1]] = "H"
            self.puzzle[self.blockCoords[2]
                        ][self.blockCoords[3]] = "H"
            self.puzzle[self.blockCoords[2]
                        ][self.blockCoords[3] + 1] = "X"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[1] -= 1
                self.blockCoords[3] -= 2
                self.blockCoords[4] = "V"
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "V"
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]+2] = "X"
                self.puzzle[self.blockCoords[2]
                            ][self.blockCoords[3]+1] = "X"
            else:
                self.blockCoords[1] -= 1
                self.blockCoords[3] -= 1
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "H"
                self.puzzle[self.blockCoords[2]
                            ][self.blockCoords[3]] = "H"
                self.puzzle[self.blockCoords[0]][self.blockCoords[1] + 1] = "X"
                self.puzzle[self.blockCoords[2]][self.blockCoords[3] + 1] = "X"

    def moveRight(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[1] += 1
            self.blockCoords[3] += 2
            self.blockCoords[4] = "H"
            self.puzzle[self.blockCoords[0]
                        ][self.blockCoords[1]] = "H"
            self.puzzle[self.blockCoords[2]
                        ][self.blockCoords[3]] = "H"
            self.puzzle[self.blockCoords[2]
                        ][self.blockCoords[1] - 1] = "X"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[1] += 2
                self.blockCoords[3] += 1
                self.blockCoords[4] = "V"
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "V"
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]-2] = "X"
                self.puzzle[self.blockCoords[2]
                            ][self.blockCoords[3]-1] = "X"
            else:
                self.blockCoords[1] += 1
                self.blockCoords[3] += 1
                self.puzzle[self.blockCoords[0]
                            ][self.blockCoords[1]] = "H"
                self.puzzle[self.blockCoords[2]
                            ][self.blockCoords[3]] = "H"
                self.puzzle[self.blockCoords[0]][self.blockCoords[1] - 1] = "X"
                self.puzzle[self.blockCoords[2]][self.blockCoords[3] - 1] = "X"

    def toggleCells(self):
        for line in self.puzzle:
            for cell in line:
                if cell == "F":
                    cell = "A"

    def testSolution(self):
        return True if self.blockCoords[4] == "V" and self.blockCoords[0] == self.solutionCoords[0] and self.blockCoords[1] == self.solutionCoords[1] else False


def readLevel(levelId):
    file = open("../res/levels/level" + str(levelId) + ".txt", "r")
    lines = file.readlines()
    file.close()

    return lines


def fileToLevel(lines):
    print("to implement")


def displayLevel(level):
    print("to implement")


def validatePlay(level, play):
    print("to implement")


def makePlay(level, play):
    print("to implement")


def getAllPlays(level):
    print("to implement")


def evaluateState(level):
    print("to implement")


def testSolution(level):
    print("to implement")
