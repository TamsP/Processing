import peasy.*;

PeasyCam cam;

float speed = 0.1;
int dim = 3;
Cubie[] cube = new Cubie[dim*dim*dim];

Move[] allMoves = new Move[] {
  new Move(0, 1, 0, 1), 
  new Move(0, 1, 0, -1), 
  new Move(0, -1, 0, 1), 
  new Move(0, -1, 0, -1), 
  new Move(1, 0, 0, 1), 
  new Move(1, 0, 0, -1), 
  new Move(-1, 0, 0, 1), 
  new Move(-1, 0, 0, -1), 
  new Move(0, 0, 1, 1), 
  new Move(0, 0, 1, -1), 
  new Move(0, 0, -1, 1), 
  new Move(0, 0, -1, -1) 
};

ArrayList<Move> sequence = new ArrayList<Move>();
Shuffle shuffle;

boolean started = false;
boolean shuffling;

Move currentMove;

void setup() {
  size(600, 600, P3D);
  //fullScreen(P3D);
  cam = new PeasyCam(this, 400);
  cam.setMinimumDistance(200);
  cam.setMaximumDistance(500);
  
  int index = 0;
  for (int x = -1; x <= 1; x++) {
    for (int y = -1; y <= 1; y++) {
      for (int z = -1; z <= 1; z++) {
        PMatrix3D matrix = new PMatrix3D();
        matrix.translate(x, y, z);
        cube[index] = new Cubie(matrix, x, y, z);
        index++;
      }
    }
  }

  currentMove = new Move(0, 0, 0, 1);

  currentMove.start();
}

void draw() {
  background(51); 

  //cam.beginHUD();
  //fill(255);
  //textSize(32);
  //text(counter, 100, 100);
  //cam.endHUD();

  rotateX(-0.3);
  rotateY(0.4);
  rotateZ(0.05);
  
  currentMove.update();
  
  if (shuffling) {
    shuffle.update();
  }


  scale(50);
  for (int i = 0; i < cube.length; i++) {
    push();
    if (abs(cube[i].z) > 0 && cube[i].z == currentMove.z) {
      rotateZ(currentMove.angle);
    } else if (abs(cube[i].x) > 0 && cube[i].x == currentMove.x) {
      rotateX(currentMove.angle);
    } else if (abs(cube[i].y) > 0 && cube[i].y == currentMove.y) {
      rotateY(-currentMove.angle);
    }   
    cube[i].show();
    pop();
  }
}
