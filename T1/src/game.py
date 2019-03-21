from array import *
from aima_search import Problem
import copy
from aima_search import uniform_cost_search, depth_first_graph_search, breadth_first_graph_search, iterative_deepening_search, greedy_best_first_graph_search

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
        self.start_board = BloxorzBoard(board_file).board
        self.state = getInitialState(self.start_board)
        self.problem = BloxorzProblem(self.state)

        self.reservedLetters = ["X", "E", "O", "A"]

        self.algorithms = {
            "DFS": depth_first_graph_search,
            "BFS": breadth_first_graph_search,
            "UCS": uniform_cost_search,
            "IDDFS": iterative_deepening_search,
            "GS": greedy_best_first_graph_search
        }
        
        self.numMovements = 0
        self.gameOver = False

    def tip(self, algorithm):
        self.problem.setState(self.state)
        return self.algorithms[algorithm](self.problem).solution()[0]

    def solve(self, algorithm):
        currState = copy.deepcopy(self.state)
        self.problem.setState(currState)

        return self.algorithms[algorithm](self.problem)

    def move(self, action):

        if self.problem.validate(action, self.state):
            self.state = self.problem.result(self.state, action)
            self.numMovements += 1
            if self.problem.goal_test(self.state):
                self.gameOver = True
                print('found solution in', self.numMovements, 'steps')

        return self.numMovements



class State:
    def __init__(self, blockCoords, solutionCoords, board, togglers):
        self.blockCoords = blockCoords
        self.solutionCoords = solutionCoords
        self.board = board
        self.togglers = togglers

    def __lt__(self, state):

        d1 = (self.blockCoords[2] - self.blockCoords[0]) + (self.blockCoords[3] - self.blockCoords[1])
        d2 = (state.blockCoords[2] - state.blockCoords[0]) + (state.blockCoords[3] - state.blockCoords[1])

        if not self.togglers:
            return d1 < d2
        else:
            num1 = 0
            num2 = 0

            for key in self.togglers:
                if self.togglers[key]:
                    num1+=1
            
            for key in state.togglers:
                if state.togglers[key]:
                    num2+=1
                    
            if num1 == 0 and num2 == 0:
                if len(self.togglers) == 1:
                    (x1,y1) = getCoords(self.board, state.togglers.keys[0])
                    (x2,y2) = getCoords(state.board, state.togglers.keys[0])


            return d1 < d2 if num1 == num2 else num1 > num2

def getCoords(board, block):
    for i in range(len(board)):
        for j in range(len(board[0])):
            if board[i][j] == block:
                return (i, j)

def getInitialState(init_board):
    blockCoords = []
    solutionCoords = []
    board = []
    togglers = {}

    for i in range(len(init_board)):
        board.append([])
        for j in range(len(init_board[0])):
            if init_board[i][j] == "V":
                blockCoords = [i, j, i, j, "V"]
                board[i].append("X")
                continue
            elif init_board[i][j] == "O":
                solutionCoords = [i, j]
            elif init_board[i][j].islower():
                togglers[init_board[i][j]] = False
            board[i].append(init_board[i][j])

    return State(blockCoords, solutionCoords, board, togglers)

class BloxorzProblem(Problem):
    
    def __init__(self, initial):
        self.initial = copy.copy(initial)
        Problem.__init__(self, self.initial)

    def setState(self, state):
        self.initial = copy.copy(state)

    def actions(self, state):
        possibleActions = ['Up', 'Down', 'Left', 'Right']
        validActions = []

        for action in possibleActions:
            if self.validate(action, state):
                validActions.append(action)

        return validActions

    def resultUp(self, blockCoords):

        nextBlockCoords = blockCoords.copy()

        if blockCoords[4] == "V":
            nextBlockCoords[0] -= 2
            nextBlockCoords[2] -= 1
            nextBlockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                nextBlockCoords[0] -= 1
                nextBlockCoords[2] -= 1
            else:
                nextBlockCoords[0] -= 1
                nextBlockCoords[2] -= 2
                nextBlockCoords[4] = "V"

        return nextBlockCoords

    def resultDown(self, blockCoords):

        nextBlockCoords = blockCoords.copy()

        if blockCoords[4] == "V":
            nextBlockCoords[0] += 1
            nextBlockCoords[2] += 2
            nextBlockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                nextBlockCoords[0] += 1
                nextBlockCoords[2] += 1
            else:
                nextBlockCoords[0] += 2
                nextBlockCoords[2] += 1
                nextBlockCoords[4] = "V"

        return nextBlockCoords

    def resultLeft(self, blockCoords):

        nextBlockCoords = blockCoords.copy()

        if blockCoords[4] == "V":
            nextBlockCoords[1] -= 2
            nextBlockCoords[3] -= 1
            nextBlockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                nextBlockCoords[1] -= 1
                nextBlockCoords[3] -= 2
                nextBlockCoords[4] = "V"
            else:
                nextBlockCoords[1] -= 1
                nextBlockCoords[3] -= 1    

        return nextBlockCoords

    def resultRight(self, blockCoords):

        nextBlockCoords = blockCoords.copy()

        if blockCoords[4] == "V":
            nextBlockCoords[1] += 1
            nextBlockCoords[3] += 2
            nextBlockCoords[4] = "H"
        elif blockCoords[4] == "H":
            if blockCoords[0] == blockCoords[2]:
                nextBlockCoords[1] += 2
                nextBlockCoords[3] += 1
                nextBlockCoords[4] = "V"
            else:
                nextBlockCoords[1] += 1
                nextBlockCoords[3] += 1

        return nextBlockCoords

    def checkTogglers(self, state):

        board = state.board
        blockCoords = state.blockCoords
        togglers = state.togglers

        reservedLetters = ["X", "E", "O", "A"]
        block1 = board[blockCoords[0]][blockCoords[1]]
        block2 = board[blockCoords[2]][blockCoords[3]]

        if block1.isupper() and block1 not in reservedLetters:
            if block1 > "G" and blockCoords[4] == 'V' or block1 < "G":
                togglers[block1.lower()] = not togglers[block1.lower()]
        elif block2.isupper() and block2 not in reservedLetters:
            if block2 > "G" and blockCoords[4] == 'V' or block2 < "G":
                togglers[block2.lower()] = not togglers[block2.lower()]

        return togglers

    def result(self, state, action):
        
        nextState = copy.copy(state)

        possibleActions = ['Up', 'Down', 'Left', 'Right']
        resultCheckers = [self.resultUp, self.resultDown, self.resultLeft, self.resultRight]

        nextState.blockCoords = resultCheckers[possibleActions.index(action)](state.blockCoords)
        nextState.togglers = self.checkTogglers(nextState)
        return nextState

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
        elif blockCoords[0] + 2 < length:
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
        elif blockCoords[0] == blockCoords[2] and blockCoords[1] + 2 < length:
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

