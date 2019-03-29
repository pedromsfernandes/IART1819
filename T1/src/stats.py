import gspread
from oauth2client.service_account import ServiceAccountCredentials


from utils import getNumLevels
from game import BloxorzGame
import time


# use creds to create a client to interact with the Google Drive API
scope = ['https://www.googleapis.com/auth/drive']
creds = ServiceAccountCredentials.from_json_keyfile_name('cred.json', scope)
client = gspread.authorize(creds)

# Find a workbook by name and open the first sheet
# Make sure you use the right name here.
sheet = client.open("bloxor").get_worksheet(2)

# Extract and print all of the values
# list_of_hashes = sheet.get_all_records()
# print(list_of_hashes)

#sheet.update_cell(4, 5, "I just wrote to a spreadsheet using Python!")



algorithms = ["DFS","BFS", "UCS", "A*", "IDDFS","GS"]


for alg in range(1,6): #algoritmo

    sheet = client.open("bloxor").get_worksheet(alg-1)
    print("ja abriu ", alg-1)
    for i in range(1,23): #niveis

        avg = 0
        max = 0
        min = 99999

        for j in range(1,8): #ensaios

            # niveis que nao correm
            if alg == 1 and (i == 15 or i == 18 or i == 21):
                continue

            game = BloxorzGame("../res/levels/level" + str(i) + ".txt")
            start = time.time()
            (goalNode, numNodes) = game.solve(algorithms[alg-1])
            end = time.time()
            duration = end - start
            avg = avg + duration

            if duration > max:
                max = duration
            
            if duration < min:
                min = duration

            solLen = len(goalNode.solution())

            #print(duration, solLen, numNodes)

            x = 2 + j + 7*(i-1)

            if j == 1:
                time.sleep(1)
                sheet.update_cell(x, 2,solLen)
                time.sleep(1)
                sheet.update_cell(x,3,numNodes)

            time.sleep(1)
            sheet.update_cell(x,5,duration)

            if j == 7:
                time.sleep(1)
                sheet.update_cell(x-6,6,(avg-max-min)/5)