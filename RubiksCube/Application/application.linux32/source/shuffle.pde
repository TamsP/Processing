class Shuffle {
  int moves;
  int counter;
  Shuffle(int moves) {
    this.counter = 0;
    this.moves = moves + 1;
    for (int i = 0; i < this.moves; i++) {
      int r = int(random(allMoves.length));
      Move m = allMoves[r];
      sequence.add(m);
    }
  }
  
  void update() {
    if (currentMove.finished()) {
      if (counter < sequence.size() - 1) {
        currentMove = sequence.get(counter);
        currentMove.start();
        this.counter++;
      } else {
        this.counter = 0;
        sequence = new ArrayList<Move>();
        shuffling = false;
      }
    }
    
  }
}
