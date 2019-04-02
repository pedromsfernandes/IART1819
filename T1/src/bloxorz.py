from tkinter import Tk, Label, Button
from gui import Bloxorz
from game import BloxorzProblem
from aima_search import uniform_cost_search, depth_first_tree_search, breadth_first_graph_search

def main():
    # Start the tkinter app main loop, showing the main menu
    app = Bloxorz()
    app.mainloop()

if __name__ == "__main__":
    main()
