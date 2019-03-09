import os, os.path

def getNumLevels():
    DIR = '../res/levels'
    return len([name for name in os.listdir(DIR) if os.path.isfile(os.path.join(DIR, name))])
