import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class RubiksCube extends PApplet {



PeasyCam cam;

float speed = 0.1f;
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

public void setup() {
  //size(600, 600, P3D);
  
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

public void draw() {
  background(51); 

  //cam.beginHUD();
  //fill(255);
  //textSize(32);
  //text(counter, 100, 100);
  //cam.endHUD();

  rotateX(-0.3f);
  rotateY(0.4f);
  rotateZ(0.05f);
  
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
class Cubie {
  PMatrix3D matrix;
  int x = 0;
  int y = 0;
  int z = 0;
  int c;
  Face[] faces = new Face[6];

  Cubie(PMatrix3D m, int x, int y, int z) {
    this.matrix = m;
    this.x = x;
    this.y = y;
    this.z = z;
    c = color(255);

    faces[0] = new Face(new PVector(0, 0, -1), color(0, 0, 255));
    faces[1] = new Face(new PVector(0, 0, 1), color(0, 255, 0));
    faces[2] = new Face(new PVector(0, 1, 0), color(255, 255, 255));
    faces[3] = new Face(new PVector(0, -1, 0), color(255, 255, 0));
    faces[4] = new Face(new PVector(1, 0, 0), color(255, 150, 0));
    faces[5] = new Face(new PVector(-1, 0, 0), color(255, 0, 0));
  }
  
  public void turnFacesZ(int dir) {
    for (Face f : faces) {
      f.turnZ(dir*HALF_PI); 
    }
  }

  public void turnFacesY(int dir) {
    for (Face f : faces) {
      f.turnY(dir*HALF_PI); 
    }
  }

    public void turnFacesX(int dir) {
    for (Face f : faces) {
      f.turnX(dir*HALF_PI); 
    }
  }
  
  
  
  public void update(int x, int y, int z) {
    matrix.reset(); 
    matrix.translate(x, y, z);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void show() {
    //fill(c);
    noFill();
    stroke(0);
    strokeWeight(0.1f);
    pushMatrix(); 
    applyMatrix(matrix);
    box(1);
    for (Face f : faces) {
      f.show();
    }
    popMatrix();
  }
}
class Face {
  PVector normal;
  int c;

  Face(PVector normal, int c) {
    this.normal = normal;
    this.c = c;
  }


  public void turnZ(float angle) {
    PVector v2 = new PVector();
    v2.x = round(normal.x * cos(angle) - normal.y * sin(angle));
    v2.y = round(normal.x * sin(angle) + normal.y * cos(angle));
    v2.z = round(normal.z);
    normal = v2;
  }

  public void turnY(float angle) {
    PVector v2 = new PVector();
    v2.x = round(normal.x * cos(angle) - normal.z * sin(angle));
    v2.z = round(normal.x * sin(angle) + normal.z * cos(angle));
    v2.y = round(normal.y);
    normal = v2;
  }

  public void turnX(float angle) {
    PVector v2 = new PVector();
    v2.y = round(normal.y * cos(angle) - normal.z * sin(angle));
    v2.z = round(normal.y * sin(angle) + normal.z * cos(angle));
    v2.x = round(normal.x);
    normal = v2;
  }

  public void show() {
    pushMatrix();
    fill(c);
    noStroke();
    rectMode(CENTER);
    translate(0.5f*normal.x, 0.5f*normal.y, 0.5f*normal.z);
    if (abs(normal.x) > 0) {
      rotateY(HALF_PI);
    } else if (abs(normal.y) > 0) {
      rotateX(HALF_PI);
    }
    square(0, 0, 1);
    popMatrix();
  }
}
class Move {
  float angle = 0;
  int x = 0;
  int y = 0;
  int z = 0;
  int dir;
  boolean animating = false;
  boolean finished = false;

  Move(int x, int y, int z, int dir) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.dir = dir;
  }

  public Move copy() {
    return new Move(x, y, z, dir);
  }

  public void reverse() {
    dir *= -1;
  }

  public void start() {
    animating = true;
    finished = false;
    this.angle = 0;
  }

  public boolean finished() {
    return finished;
  }

  public void update() {
    if (animating) {
      angle += dir * speed;
      if (abs(angle) > HALF_PI) {
        angle = 0;
        animating = false;
        finished = true;
        if (abs(z) > 0) {
          turnZ(z, dir);
        } else if (abs(x) > 0) {
          turnX(x, dir);
        } else if (abs(y) > 0) {
          turnY(y, dir);
        }
      }
    }
  }
}
public void keyPressed() {
  if (key == ' ') {
    //counter = 0;
    currentMove.start();
  }
   applyMove(key);
}

public void applyMove(char move) {
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
class Shuffle {
  int moves;
  int counter;
  Shuffle(int moves) {
    this.counter = 0;
    this.moves = moves + 1;
    for (int i = 0; i < this.moves; i++) {
      int r = PApplet.parseInt(random(allMoves.length));
      Move m = allMoves[r];
      sequence.add(m);
    }
  }
  
  public void update() {
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
public void turnZ(int index, int dir) {
  for (int i = 0; i < cube.length; i++) {
    Cubie qb = cube[i];
    if (qb.z == index) {
      PMatrix2D matrix = new PMatrix2D();
      matrix.rotate(dir*HALF_PI);
      matrix.translate(qb.x, qb.y);
      qb.update(round(matrix.m02), round(matrix.m12), round(qb.z));
      qb.turnFacesZ(dir);
    }
  }
}

public void turnY(int index, int dir) {
  for (int i = 0; i < cube.length; i++) {
    Cubie qb = cube[i];
    if (qb.y == index) {
      PMatrix2D matrix = new PMatrix2D();
      matrix.rotate(dir*HALF_PI);
      matrix.translate(qb.x, qb.z);
      qb.update(round(matrix.m02), qb.y, round(matrix.m12));
      qb.turnFacesY(dir);
    }
  }
}

public void turnX(int index, int dir) {
  for (int i = 0; i < cube.length; i++) {
    Cubie qb = cube[i];
    if (qb.x == index) {
      PMatrix2D matrix = new PMatrix2D();
      matrix.rotate(dir*HALF_PI);
      matrix.translate(qb.y, qb.z);
      qb.update(qb.x, round(matrix.m02), round(matrix.m12));
      qb.turnFacesX(dir);
    }
  }
}
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "RubiksCube" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
