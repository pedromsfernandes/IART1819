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

    def start(self):
        self.game_over = False
        self.puzzle = []
        for i in range(len(self.start_puzzle)):
            self.puzzle.append([])
            for j in range(len(self.start_puzzle[0])):
                self.puzzle[i].append(self.start_puzzle[i][j])


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
