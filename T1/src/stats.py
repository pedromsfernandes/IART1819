import gspread
from oauth2client.service_account import ServiceAccountCredentials
from utils import getNumLevels
from game import BloxorzGame, h1, h2
import time


# use creds to create a client to interact with the Google Drive API
scope = ['https://www.googleapis.com/auth/drive']
creds = ServiceAccountCredentials.from_json_keyfile_name('cred.json', scope)
client = gspread.authorize(creds)

# List of the algorithms
algorithms = ["DFS", "BFS", "UCS", "IDDFS", "GS", "A*", "A*"]


for alg in range(5, len(algorithms) + 1):  # algorithm


    sheet = client.open("bloxor").get_worksheet(alg-1)
    for i in range(1, getNumLevels() + 1):  # level

        avg = 0
        mag = 0
        min = 99999

        for j in range(1, 8):  # number of tries

            game = BloxorzGame("../res/levels/level" + str(i) + ".txt")
            start = time.time()

            if alg - 1 == 5: # first heuristic a star
                (goalNode, numNodes) = game.solve(algorithms[alg-1], h1)

            elif alg - 1 == 6: # second heuristic a star
                (goalNode, numNodes) = game.solve(algorithms[alg-1], h2)
            elif alg - 1 == 4: # second heuristic greedy
                (goalNode, numNodes) = game.solve(algorithms[alg-1], h2)
            else:
                (goalNode, numNodes) = game.solve(algorithms[alg-1])
            
            # To catch timeouts
            if goalNode == {} and numNodes == 73:
                numNodes = 0
                duration = 'TIMEOUT'
            else:
                end = time.time()
                duration = end - start
                avg = avg + duration
            
                if duration > mag:
                    mag = duration

                if duration < min:
                    min = duration

                solLen = len(goalNode.solution())

            x = 2 + j + 7*(i-1)

            # Update the google sheet
            if duration == 'TIMEOUT':
                sheet.update_cell(x, 5, duration)
                break
                
            if j == 1:
                time.sleep(1)
                sheet.update_cell(x, 2, solLen)
                time.sleep(1)
                sheet.update_cell(x, 3, numNodes)

            time.sleep(1)
            sheet.update_cell(x, 5, duration)

            if j == 7:
                time.sleep(1)
                sheet.update_cell(x-6, 6, (avg-mag-min)/5)
