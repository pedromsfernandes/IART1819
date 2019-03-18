from array import *
from aima_search import Problem

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

        self.reservedLetters = ["X", "E", "O", "A"]

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

        self.numMovements = 0
        self.gameOver = False

    def start(self):
        self.game_over = False
        self.puzzle = []
        self.togglers = {}
        for i in range(len(self.start_puzzle)):
            self.puzzle.append([])
            for j in range(len(self.start_puzzle[0])):
                if self.start_puzzle[i][j] == "V":
                    self.blockCoords = [i, j, i, j, "V"]
                    self.puzzle[i].append("X")
                    continue
                elif self.start_puzzle[i][j] == "O":
                    self.solutionCoords = [i, j]
                elif self.start_puzzle[i][j].islower():
                    self.togglers[self.start_puzzle[i][j]] = False
                self.puzzle[i].append(self.start_puzzle[i][j])

    def validateMove(self, direction):
        return self.validators[direction]()

    def move(self, direction):
        if self.validateMove(direction):
            self.keyEvents[direction]()
            self.numMovements += 1
            self.checkTogglers()
            if self.testSolution():
                self.gameOver = True
                print('found solution in', self.numMovements, 'steps')

        return self.numMovements

    def checkTogglers(self):
        block1 = self.puzzle[self.blockCoords[0]][self.blockCoords[1]]
        block2 = self.puzzle[self.blockCoords[2]][self.blockCoords[3]]

        if block1.isupper() and block1 not in self.reservedLetters:
            if block1 > "G" and self.blockCoords[4] == 'V' or block1 < "G":
                self.toggleCells(block1)
        elif block2.isupper() and block2 not in self.reservedLetters:
            if block2 > "G" and self.blockCoords[4] == 'V' or block2 < "G":
                self.toggleCells(block2)

    def isValid(self, i0, j0, i1, j1):
        piece1 = self.puzzle[self.blockCoords[0] +
                             i0][self.blockCoords[1] + j0]
        piece2 = self.puzzle[self.blockCoords[2] +
                             i1][self.blockCoords[3] + j1]

        if piece1 in self.togglers:
            if piece2 in self.togglers:
                return self.togglers[piece1] and self.togglers[piece2]
            return self.togglers[piece1]

        if piece2 in self.togglers:
            if piece1 in self.togglers:
                return self.togglers[piece1] and self.togglers[piece2]
            return self.togglers[piece2]

        print(piece1)
        if piece1 != "E" and piece2 != "E":
            return True

        return False

    def validateUp(self):
        if self.blockCoords[4] == "V":
            if self.blockCoords[0] - 2 >= 0 and self.isValid(-2, 0, -1, 0):
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            if self.blockCoords[0] - 1 >= 0 and self.isValid(-1, 0, -1, 0):
                return True
        else:
            piece = self.puzzle[self.blockCoords[0] - 1][self.blockCoords[1]]
            if piece in self.togglers:
                return self.togglers[piece]

            return True if self.blockCoords[0] - 1 >= 0 and piece != "E" else False

        return False

    def validateDown(self):
        length = len(self.puzzle)
        if self.blockCoords[4] == "V":
            if self.blockCoords[0] + 2 < length and self.isValid(2, 0, 1, 0):
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            if self.blockCoords[0] + 1 < length and self.isValid(1, 0, 1, 0):
                return True
        else:
            piece = self.puzzle[self.blockCoords[0] + 2][self.blockCoords[1]]
            if piece in self.togglers:
                return self.togglers[piece]

            return True if self.blockCoords[0] + 1 < length and piece != "E" else False

        return False

    def validateLeft(self):
        if self.blockCoords[4] == "V":
            if self.blockCoords[1] - 2 >= 0 and self.isValid(0, -2, 0, -1):
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            piece = self.puzzle[self.blockCoords[0]][self.blockCoords[1] - 1]
            if piece in self.togglers:
                return self.togglers[piece]

            return True if self.blockCoords[1] - 1 >= 0 and piece != "E" else False
        else:
            if self.blockCoords[1] - 1 >= 0 and self.isValid(0, -1, 0, -1):
                return True

        return False

    def validateRight(self):
        length = len(self.puzzle[0])
        if self.blockCoords[4] == "V":
            if self.blockCoords[3] + 2 < length and self.isValid(0, 1, 0, 2):
                return True
        elif self.blockCoords[0] == self.blockCoords[2]:
            piece = self.puzzle[self.blockCoords[0]][self.blockCoords[1] + 2]
            if piece in self.togglers:
                return self.togglers[piece]

            return True if self.blockCoords[3] + 1 < length and piece != "E" else False
        else:
            if self.blockCoords[3] + 1 < length and self.isValid(0, 1, 0, 1):
                return True

        return False

    def moveUp(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[0] -= 2
            self.blockCoords[2] -= 1
            self.blockCoords[4] = "H"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[0] -= 1
                self.blockCoords[2] -= 1
            else:
                self.blockCoords[0] -= 1
                self.blockCoords[2] -= 2
                self.blockCoords[4] = "V"

    def moveDown(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[0] += 1
            self.blockCoords[2] += 2
            self.blockCoords[4] = "H"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[0] += 1
                self.blockCoords[2] += 1
            else:
                self.blockCoords[0] += 2
                self.blockCoords[2] += 1
                self.blockCoords[4] = "V"

    def moveLeft(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[1] -= 2
            self.blockCoords[3] -= 1
            self.blockCoords[4] = "H"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[1] -= 1
                self.blockCoords[3] -= 2
                self.blockCoords[4] = "V"
            else:
                self.blockCoords[1] -= 1
                self.blockCoords[3] -= 1

    def moveRight(self):
        if self.blockCoords[4] == "V":
            self.blockCoords[1] += 1
            self.blockCoords[3] += 2
            self.blockCoords[4] = "H"
        elif self.blockCoords[4] == "H":
            if self.blockCoords[0] == self.blockCoords[2]:
                self.blockCoords[1] += 2
                self.blockCoords[3] += 1
                self.blockCoords[4] = "V"
            else:
                self.blockCoords[1] += 1
                self.blockCoords[3] += 1

    def toggleCells(self, letter):
        self.togglers[letter.lower()] = not self.togglers[letter.lower()]

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


class BloxorzProblem(Problem):
    
    def __init__(self, initial, goal):
        Problem.__init__(self, initial, goal)

    def actions(self, state):
        possibleActions = ['Up', 'Down', 'Left', 'Right']

        for action in possibleActions:
            if not self.validate(action, state):
                possibleActions.remove(action)

        return possibleActions

    def resultUp(self, blockCoords):

        if blockCoords[4] == "V":
            blockCoords[0] -= 2
            blockCoords[2] -= 1
            blockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                blockCoords[0] -= 1
                blockCoords[2] -= 1
            else:
                blockCoords[0] -= 1
                blockCoords[2] -= 2
                blockCoords[4] = "V"

        return blockCoords

    def resultDown(self, blockCoords):

        if blockCoords[4] == "V":
            blockCoords[0] += 1
            blockCoords[2] += 2
            blockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                blockCoords[0] += 1
                blockCoords[2] += 1
            else:
                blockCoords[0] += 2
                blockCoords[2] += 1
                blockCoords[4] = "V"

        return blockCoords

    def resultLeft(self, blockCoords):

        if blockCoords[4] == "V":
            blockCoords[1] -= 2
            blockCoords[3] -= 1
            blockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                blockCoords[1] -= 1
                blockCoords[3] -= 2
                blockCoords[4] = "V"
            else:
                blockCoords[1] -= 1
                blockCoords[3] -= 1    

        return blockCoords

    def resultRight(self, blockCoords):

        if blockCoords[4] == "V":
            blockCoords[1] += 1
            blockCoords[3] += 2
            blockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                blockCoords[1] += 2
                blockCoords[3] += 1
                blockCoords[4] = "V"
            else:
                blockCoords[1] += 1
                blockCoords[3] += 1

        return blockCoords

    def checkTogglers(self, state):

        board = state.board
        blockCoords = state.blockCoords
        togglers = state.togglers

        reservedLetters = ["X", "E", "O", "A"]

        block1 = board[blockCoords[0]][blockCoords[1]]
        block2 = board[blockCoords[2]][blockCoords[3]]

        if block1.isupper() and block1 not in reservedLetters:
            if block1 > "G" and blockCoords[4] == 'V' or block1 < "G":
                togglers[block1] = not togglers[block1]
        elif block2.isupper() and block2 not in reservedLetters:
            if block2 > "G" and blockCoords[4] == 'V' or block2 < "G":
                togglers[block2] = not togglers[block2]

        return togglers

    def result(self, state, action):

        possibleActions = ['Up', 'Down', 'Left', 'Right']
        resultCheckers = [self.resultUp, self.resultDown, self.resultLeft, self.resultRight]

        state.blockCoords = resultCheckers[possibleActions.index(action)](state.blockCoords)
        state.togglers = self.checkTogglers(state)

        return state

    def goal_test(self, state):

        blockCoords = state.blockCoords
        solutionCoords = state.solutionCoords

        return True if blockCoords[4] == "V" and blockCoords[0] == solutionCoords[0] and blockCoords[1] == solutionCoords[1] else False


    def isValid(self, state, i0, j0, i1, j1):

        board = state.board
        blockCoords = state.blockCoords
        togglers = state.togglers

        piece1 = board[blockCoords[0] +
                             i0][blockCoords[1] + j0]
        piece2 = board[blockCoords[2] +
                             i1][blockCoords[3] + j1]

        if piece1 in togglers:
            if piece2 in togglers:
                return togglers[piece1] and togglers[piece2]
            return togglers[piece1]

        if piece2 in togglers:
            if piece1 in togglers:
                return togglers[piece1] and togglers[piece2]
            return togglers[piece2]

        print(piece1)
        if piece1 != "E" and piece2 != "E":
            return True

        return False

    def validateUp(self, state):

        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        if blockCoords[4] == "V":
            if blockCoords[0] - 2 >= 0 and self.isValid(state, -2, 0, -1, 0):
                return True
        elif blockCoords[0] == blockCoords[2]:
            if blockCoords[0] - 1 >= 0 and self.isValid(state, -1, 0, -1, 0):
                return True
        else:
            piece = board[blockCoords[0] - 1][blockCoords[1]]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[0] - 1 >= 0 and piece != "E" else False

        return False

    def validateDown(self, state):

        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        length = len(board)

        if blockCoords[4] == "V":
            if blockCoords[0] + 2 < length and self.isValid(state, 2, 0, 1, 0):
                return True
        elif blockCoords[0] == blockCoords[2]:
            if blockCoords[0] + 1 < length and self.isValid(state, 1, 0, 1, 0):
                return True
        else:
            piece = board[blockCoords[0] + 2][blockCoords[1]]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[0] + 1 < length and piece != "E" else False

        return False

    def validateLeft(self, state):

        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        if blockCoords[4] == "V":
            if blockCoords[1] - 2 >= 0 and self.isValid(state, 0, -2, 0, -1):
                return True
        elif blockCoords[0] == blockCoords[2]:
            piece = board[blockCoords[0]][blockCoords[1] - 1]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[1] - 1 >= 0 and piece != "E" else False
        else:
            if blockCoords[1] - 1 >= 0 and self.isValid(state, 0, -1, 0, -1):
                return True

        return False

    def validateRight(self, state):
        
        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        length = len(board[0])
       
        if blockCoords[4] == "V":
            if blockCoords[3] + 2 < length and self.isValid(state, 0, 1, 0, 2):
                return True
        elif blockCoords[0] == blockCoords[2]:
            piece = board[blockCoords[0]][blockCoords[1] + 2]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[3] + 1 < length and piece != "E" else False
        else:
            if blockCoords[3] + 1 < length and self.isValid(state, 0, 1, 0, 1):
                return True

        return False

    def validate(self, action, state):
        actions = ['Up', 'Down', 'Left', 'Right']
        validators = [self.validateUp, self.validateDown, self.validateLeft, self.validateRight]

        return validators[actions.index(action)](state)

