from array import *
from aima_search import Problem
import copy
from aima_search import uniform_cost_search, depth_first_graph_search, breadth_first_graph_search, iterative_deepening_search, greedy_best_first_graph_search, astar_search
import time

"""
Board class. Responsible for reading a file and storing the level in a double array.
"""
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

"""
Main game class, used by the GUI to effect the game board.
"""
class BloxorzGame(object):

    def __init__(self, board_file):
        self.start_board = BloxorzBoard(board_file).board
        self.state = getInitialState(self.start_board)
        self.problem = BloxorzProblem(self.state)

        self.reservedLetters = ["X", "E", "O", "V", "F"]

        self.algorithms = {
            "DFS": depth_first_graph_search,
            "BFS": breadth_first_graph_search,
            "UCS": uniform_cost_search,
            "IDDFS": iterative_deepening_search,
            "GS": greedy_best_first_graph_search,
            "A*": astar_search
        }

        self.numMovements = 0
        self.gameOver = False

    """
    Calls the given algorithm and returns the best next move needed to complete the level.
    """
    def tip(self, algorithm, h=None):
        self.problem.setState(self.state)
        self.problem.startTime = time.time()

        if algorithm == "A*" or algorithm == "GS":
            (goalNode, node) = self.algorithms[algorithm](self.problem, h2)
        else:
            (goalNode, node) = self.algorithms[algorithm](self.problem)

        return goalNode.solution()[0]


    """
    Calls the given algorithm and returns the actions needed to solve the current level.
    With this implementation, it allows to solve the level from any state: even if the user moves around and tries to solve
    later, it will work.
    """
    def solve(self, algorithm, h=None):
        currState = copy.deepcopy(self.state)
        self.problem.setState(currState)

        self.problem.startTime = time.time()

        if algorithm == "A*" or algorithm == "GS":
            answer = self.algorithms[algorithm](self.problem, h)
            if answer == None:
                return ({},73) #defined as the infinite loop exception
            
            return answer

        return self.algorithms[algorithm](self.problem)

    """
    Checks if a move is valid, in which case performs it and checks if the state is final.
    The action argument is one of ["Up", "Down", "Left", "Right"]
    Returns the number of movements, in order for the GUI to be updated.
    """
    def move(self, action):
        if self.problem.validate(action, self.state):
            self.state = self.problem.result(self.state, action)
            self.numMovements += 1
            if self.problem.goal_test(self.state):
                self.gameOver = True
                print('found solution in', self.numMovements, 'steps')

        return self.numMovements

"""
State object, holding the game board, and useful information separately, such as
the coordinates of the block, the coordinates of the solution, the state of togglers
and teleporters (only existing in later levels).
"""
class State:
    def __init__(self, blockCoords, solutionCoords, board, togglers, teleport):
        self.blockCoords = blockCoords
        self.solutionCoords = solutionCoords
        self.board = board
        self.togglers = togglers
        self.teleport = teleport

    """
    Overload the == operator, comparing the content of each field.
    """
    def __eq__(self, other):
        return self.blockCoords[0] == other.blockCoords[0] and self.blockCoords[4] == other.blockCoords[4] and self.blockCoords[1] == other.blockCoords[1] and self.blockCoords[2] == other.blockCoords[2] and self.blockCoords[3] == other.blockCoords[3] and self.solutionCoords == other.solutionCoords and self.board == other.board and self.togglers == other.togglers

    def __hash__(self):
        return hash(repr(self))

    def distance(self, coords1, coords2):
        return (coords2[0] - coords1[0]) + (coords2[1] - coords1[1])

    """
    Overload the < operator. A state is considered less than another if it is closer to the solution.
    """
    def __lt__(self, other):

        d1 = self.distance(
            [self.blockCoords[0], self.blockCoords[1]], self.solutionCoords)
        d2 = self.distance(
            [self.blockCoords[2], self.blockCoords[3]], self.solutionCoords)

        ds = d1 if d1 < d2 else d2

        d3 = self.distance(
            [other.blockCoords[0], other.blockCoords[1]], other.solutionCoords)
        d4 = self.distance(
            [other.blockCoords[2], other.blockCoords[3]], other.solutionCoords)

        do = d3 if d3 < d4 else d4

        if not self.togglers:
            return ds < do

"""
Returns the initial state of the level, by analyzing the result of reading the respective text file.
"""
def getInitialState(init_board):
    blockCoords = []
    solutionCoords = []
    board = []
    togglers = {}
    teleport = {}

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
            elif init_board[i][j].isdigit() and int(init_board[i][j]) % 2 == 0:
                teleport[init_board[i][j]] = [i, j]

            board[i].append(init_board[i][j])

    print(teleport)

    return State(blockCoords, solutionCoords, board, togglers, teleport)


"""
Main game logic. Used both by the GUI and the search algorithms.
"""
class BloxorzProblem(Problem):

    def __init__(self, initial):
        self.initial = copy.copy(initial)
        Problem.__init__(self, self.initial)

    def setState(self, state):
        self.initial = copy.copy(state)

    """
    Returns all of the possible actions (movements) to take in the current state.
    """
    def actions(self, state):
        possibleActions = ['Up', 'Down', 'Left', 'Right']
        validActions = []

        for action in possibleActions:
            if self.validate(action, state):
                validActions.append(action)

        return validActions


    """
    Returns the next coordinates of the block after the action Up.
    """
    def resultUp(self, state):

        nextBlockCoords = state.blockCoords.copy()

        if nextBlockCoords[4] == "V":
            nextBlockCoords[0] -= 2
            nextBlockCoords[2] -= 1
            nextBlockCoords[4] = "H"
        elif nextBlockCoords[4] == "H":

            if nextBlockCoords[0] == nextBlockCoords[2]:
                nextBlockCoords[0] -= 1
                nextBlockCoords[2] -= 1
            else:
                piece = state.board[nextBlockCoords[0] - 1][nextBlockCoords[1]]
                nextBlockCoords[4] = "V"

                if piece.isnumeric() and int(piece) % 2 == 1:
                    key = str(int(piece) + 1)
                    nextBlockCoords[0] = state.teleport[key][0]
                    nextBlockCoords[1] = state.teleport[key][1]
                    nextBlockCoords[2] = state.teleport[key][0]
                    nextBlockCoords[3] = state.teleport[key][1]
                elif piece == '^':
                    nextBlockCoords[0] -= 2
                    nextBlockCoords[2] -= 3
                elif piece == '_':
                    nextBlockCoords[2] -= 1
                elif piece == '>':
                    nextBlockCoords[0] -= 1
                    nextBlockCoords[2] -= 2
                    nextBlockCoords[1] += 1
                    nextBlockCoords[3] += 1
                elif piece == '<':
                    nextBlockCoords[0] -= 1
                    nextBlockCoords[2] -= 2
                    nextBlockCoords[1] -= 1
                    nextBlockCoords[3] -= 1
                else:
                    nextBlockCoords[0] -= 1
                    nextBlockCoords[2] -= 2

        return nextBlockCoords

    """
    Returns the next coordinates of the block after the action Down.
    """
    def resultDown(self, state):

        nextBlockCoords = state.blockCoords.copy()

        if nextBlockCoords[4] == "V":
            nextBlockCoords[0] += 1
            nextBlockCoords[2] += 2
            nextBlockCoords[4] = "H"
        elif nextBlockCoords[4] == "H":
            if nextBlockCoords[0] == nextBlockCoords[2]:
                nextBlockCoords[0] += 1
                nextBlockCoords[2] += 1
            else:
                piece = state.board[nextBlockCoords[0] + 2][nextBlockCoords[1]]
                nextBlockCoords[4] = "V"

                if piece.isnumeric() and int(piece) % 2 == 1:
                    key = str(int(piece) + 1)
                    nextBlockCoords[0] = state.teleport[key][0]
                    nextBlockCoords[1] = state.teleport[key][1]
                    nextBlockCoords[2] = state.teleport[key][0]
                    nextBlockCoords[3] = state.teleport[key][1]
                elif piece == '^':
                    nextBlockCoords[0] += 1
                elif piece == '_':
                    nextBlockCoords[0] += 3
                    nextBlockCoords[2] += 2
                elif piece == '>':
                    nextBlockCoords[0] += 2
                    nextBlockCoords[2] += 1
                    nextBlockCoords[1] += 1
                    nextBlockCoords[3] += 1
                elif piece == '<':
                    nextBlockCoords[0] += 2
                    nextBlockCoords[2] += 1
                    nextBlockCoords[1] -= 1
                    nextBlockCoords[3] -= 1
                else:
                    nextBlockCoords[0] += 2
                    nextBlockCoords[2] += 1

        return nextBlockCoords

    """
    Returns the next coordinates of the block after the action Left.
    """
    def resultLeft(self, state):

        nextBlockCoords = state.blockCoords.copy()

        if nextBlockCoords[4] == "V":
            nextBlockCoords[1] -= 2
            nextBlockCoords[3] -= 1
            nextBlockCoords[4] = "H"
        elif nextBlockCoords[4] == "H":

            if nextBlockCoords[0] != nextBlockCoords[2]:
                nextBlockCoords[1] -= 1
                nextBlockCoords[3] -= 1
            else:
                piece = state.board[nextBlockCoords[0]][nextBlockCoords[1] - 1]
                nextBlockCoords[4] = "V"

                if piece.isnumeric() and int(piece) % 2 == 1:
                    key = str(int(piece) + 1)
                    nextBlockCoords[0] = state.teleport[key][0]
                    nextBlockCoords[1] = state.teleport[key][1]
                    nextBlockCoords[2] = state.teleport[key][0]
                    nextBlockCoords[3] = state.teleport[key][1]
                elif piece == '^':
                    nextBlockCoords[0] -= 1
                    nextBlockCoords[2] -= 1
                    nextBlockCoords[1] -= 1
                    nextBlockCoords[3] -= 2
                elif piece == '_':
                    nextBlockCoords[0] += 1
                    nextBlockCoords[2] += 1
                    nextBlockCoords[1] -= 1
                    nextBlockCoords[3] -= 2
                elif piece == '>':
                    nextBlockCoords[3] -= 1
                elif piece == '<':
                    nextBlockCoords[1] -= 2
                    nextBlockCoords[3] -= 3
                else:
                    nextBlockCoords[1] -= 1
                    nextBlockCoords[3] -= 2

        return nextBlockCoords


    """
    Returns the next coordinates of the block after the action Right.
    """
    def resultRight(self, state):

        nextBlockCoords = state.blockCoords.copy()

        if nextBlockCoords[4] == "V":
            nextBlockCoords[1] += 1
            nextBlockCoords[3] += 2
            nextBlockCoords[4] = "H"
        elif nextBlockCoords[4] == "H":
            if nextBlockCoords[0] != nextBlockCoords[2]:
                nextBlockCoords[1] += 1
                nextBlockCoords[3] += 1
            else:
                piece = state.board[nextBlockCoords[0]][nextBlockCoords[1] + 2]
                nextBlockCoords[4] = "V"

                if piece.isnumeric() and int(piece) % 2 == 1:
                    key = str(int(piece) + 1)
                    nextBlockCoords[0] = state.teleport[key][0]
                    nextBlockCoords[1] = state.teleport[key][1]
                    nextBlockCoords[2] = state.teleport[key][0]
                    nextBlockCoords[3] = state.teleport[key][1]
                elif piece == '^':
                    nextBlockCoords[0] -= 1
                    nextBlockCoords[2] -= 1
                    nextBlockCoords[1] += 2
                    nextBlockCoords[3] += 1
                elif piece == '_':
                    nextBlockCoords[0] += 1
                    nextBlockCoords[2] += 1
                    nextBlockCoords[1] += 2
                    nextBlockCoords[3] += 1
                elif piece == '>':
                    nextBlockCoords[1] += 3
                    nextBlockCoords[3] += 2
                elif piece == '<':
                    nextBlockCoords[1] += 1
                else:
                    nextBlockCoords[1] += 2
                    nextBlockCoords[3] += 1

        return nextBlockCoords

    """
    Returns the next state of the togglers, checking if the block is pressing them, and (de)activating
    the respective places.
    """
    def checkTogglers(self, state):

        board = state.board
        blockCoords = state.blockCoords
        togglers = copy.copy(state.togglers)

        reservedLetters = ["X", "E", "O", "V", "F"]
        block1 = board[blockCoords[0]][blockCoords[1]]
        block2 = board[blockCoords[2]][blockCoords[3]]

        if block1.isupper() and block1 not in reservedLetters:
            if block1 > "G" and blockCoords[4] == 'V' or block1 < "G":
                togglers[block1.lower()] = not togglers[block1.lower()]
        elif block2.isupper() and block2 not in reservedLetters:
            if block2 > "G" and blockCoords[4] == 'V' or block2 < "G":
                togglers[block2.lower()] = not togglers[block2.lower()]

        return togglers

    """
    Returns the next state of the game after a given action is taken. At this point it is assumed that the move is valid.
    """
    def result(self, state, action):

        nextState = copy.copy(state)

        possibleActions = ['Up', 'Down', 'Left', 'Right']
        resultCheckers = [self.resultUp, self.resultDown,
                          self.resultLeft, self.resultRight]

        nextState.blockCoords = resultCheckers[possibleActions.index(action)](
            state)
        nextState.togglers = self.checkTogglers(nextState)
        return nextState

    """
    Checks if the block is standing vertically upon the goal place.
    """
    def goal_test(self, state):

        blockCoords = state.blockCoords
        solutionCoords = state.solutionCoords

        return True if blockCoords[4] == "V" and blockCoords[0] == solutionCoords[0] and blockCoords[1] == solutionCoords[1] else False

    """
    Checks if the pieces where the block would move are valid, in case there are togglers in this level.
    In order to be valid, those pieces need to be activated, otherwise they're just empty.
    """
    def areTogglersValid(self, state, i0, j0, i1, j1):

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
            return togglers[piece1] and piece2 != "E"

        if piece2 in togglers:
            if piece1 in togglers:
                return togglers[piece1] and togglers[piece2]
            return togglers[piece2] and piece1 != "E"

        if piece1 != "E" and piece2 != "E":
            return True

        return False

    """
    Checks if moving up is valid in the current state.
    """
    def validateUp(self, state):

        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        if blockCoords[4] == "V":
            if blockCoords[0] - 2 >= 0 and self.areTogglersValid(state, -2, 0, -1, 0):
                return True
        elif blockCoords[0] == blockCoords[2]:
            if blockCoords[0] - 1 >= 0 and self.areTogglersValid(state, -1, 0, -1, 0):
                return True
        else:
            piece = board[blockCoords[0] - 1][blockCoords[1]]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[0] - 1 >= 0 and piece != "E" and piece != "F" else False

        return False

    """
    Checks if moving down is valid in the current state.
    """
    def validateDown(self, state):

        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        length = len(board)

        if blockCoords[4] == "V":
            if blockCoords[0] + 2 < length and self.areTogglersValid(state, 2, 0, 1, 0):
                return True
        elif blockCoords[0] == blockCoords[2]:
            if blockCoords[0] + 1 < length and self.areTogglersValid(state, 1, 0, 1, 0):
                return True
        elif blockCoords[0] + 2 < length:
            piece = board[blockCoords[0] + 2][blockCoords[1]]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[0] + 1 < length and piece != "E" and piece != "F" else False

        return False

    """
    Checks if moving left is valid in the current state.
    """
    def validateLeft(self, state):

        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        if blockCoords[4] == "V":
            if blockCoords[1] - 2 >= 0 and self.areTogglersValid(state, 0, -2, 0, -1):
                return True
        elif blockCoords[0] == blockCoords[2]:
            piece = board[blockCoords[0]][blockCoords[1] - 1]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[1] - 1 >= 0 and piece != "E" and piece != "F" else False
        else:
            if blockCoords[1] - 1 >= 0 and self.areTogglersValid(state, 0, -1, 0, -1):
                return True

        return False

    """
    Checks if moving right is valid in the current state.
    """
    def validateRight(self, state):

        blockCoords = state.blockCoords
        board = state.board
        togglers = state.togglers

        length = len(board[0])

        if blockCoords[4] == "V":
            if blockCoords[3] + 2 < length and self.areTogglersValid(state, 0, 1, 0, 2):
                return True
        elif blockCoords[0] == blockCoords[2] and blockCoords[1] + 2 < length:
            piece = board[blockCoords[0]][blockCoords[1] + 2]
            if piece in togglers:
                return togglers[piece]

            return True if blockCoords[3] + 1 < length and piece != "E" and piece != "F" else False
        else:
            if blockCoords[3] + 1 < length and self.areTogglersValid(state, 0, 1, 0, 1):
                return True

        return False

    """
    Checks if the given move is valid in the current state.
    """
    def validate(self, action, state):
        actions = ['Up', 'Down', 'Left', 'Right']
        validators = [self.validateUp, self.validateDown,
                      self.validateLeft, self.validateRight]

        return validators[actions.index(action)](state)

"""
First heuristic for the greedy and astar methods.
Calculates the distance of the current node's block to the goal.
"""
def h1(node):
    x1, y1, t, z, s = node.state.blockCoords
    x2, y2 = node.state.solutionCoords

    return (abs(x2 - x1) + abs(y2 - y1))

"""
Second heuristic for the greedy and astar methods.
Calculates an estimate for the number of moves necessary to cover the
distance of the current node's block to the goal.
"""
def h2(node):
    x1, y1, t, z, s = node.state.blockCoords
    x2, y2 = node.state.solutionCoords

    return (abs(x2 - x1) + abs(y2 - y1))/1.5
