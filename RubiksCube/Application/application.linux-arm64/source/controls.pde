void keyPressed() {
  if (key == ' ') {
    //counter = 0;
    currentMove.start();
  }
   applyMove(key);
}

void applyMove(char move) {
  switch (move) {
  case 'f': 
    if (currentMove.finished()) {
      currentMove = new Move(0, 0, 1, 1);
      currentMove.start();
    }
    break;
  case 'F': 
    if (currentMove.finished()) {
      currentMove = new Move(0, 0, 1, -1);
      currentMove.start();
    }
    break;
  case 'b': 
    if (currentMove.finished()) {
      currentMove = new Move(0, 0, -1, -1);
      currentMove.start();
    }
    break;
  case 'B': 
    if (currentMove.finished()) {
      currentMove = new Move(0, 0, -1, 1);
      currentMove.start();
    }
    break;
  case 'u': 
    if (currentMove.finished()) {
      currentMove = new Move(0, -1, 0, 1);
      currentMove.start();
    }
    break;
  case 'U': 
    if (currentMove.finished()) {
      currentMove = new Move(0, -1, 0, -1);
      currentMove.start();
    }
    break;
  case 'd': 
    if (currentMove.finished()) {
      currentMove = new Move(0, 1, 0, -1);
      currentMove.start();
    }
    break;
  case 'D': 
    if (currentMove.finished()) {
      currentMove = new Move(0, 1, 0, 1);
      currentMove.start();
    }
    break;
  case 'l': 
    if (currentMove.finished()) {
      currentMove = new Move(-1, 0, 0, -1);
      currentMove.start();
    }
    break;
  case 'L': 
    if (currentMove.finished()) {
      currentMove = new Move(-1, 0, 0, 1);
      currentMove.start();
    }
    break;
  case 'r': 
    if (currentMove.finished()) {
      currentMove = new Move(1, 0, 0, 1);
      currentMove.start();
    }
    break;
  case 'R': 
    if (currentMove.finished()) {
      currentMove = new Move(1, 0, 0, -1);
      currentMove.start();
    }
    break;
  case 's':
    shuffling = true;
    shuffle = new Shuffle(10);
    break;
  }
}
