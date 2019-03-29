import gspread
from oauth2client.service_account import ServiceAccountCredentials


# use creds to create a client to interact with the Google Drive API
scope = ['https://www.googleapis.com/auth/drive']
creds = ServiceAccountCredentials.from_json_keyfile_name('cred.json', scope)
client = gspread.authorize(creds)

# Find a workbook by name and open the first sheet
# Make sure you use the right name here.
sheet = client.open("bloxor").get_worksheet(0)

# Extract and print all of the values
# list_of_hashes = sheet.get_all_records()
# print(list_of_hashes)

sheet.update_cell(4, 5, "I just wrote to a spreadsheet using Python!")