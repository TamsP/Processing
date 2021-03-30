float angle = 0;
int w = 100;
int cols;
int rows;

boolean saving = false;

Curve[][] curves;

void setup() {
  fullScreen();
  cols = width / w - 1;
  rows = height / w - 1;
  curves = new Curve[rows][cols];
  
  for (int j = 0; j < rows; j++) {
    for (int i = 0; i < cols; i++) {
      curves[j][i] = new Curve();
    }
  }
}

void draw() {
  background(0);
  float d = w - 0.2*w;
  float r = d / 2;

  noFill();
  for (int i = 0; i < cols; i++) {
    float cx = w + i * w + w/2;
    float cy = w/2;
    noFill();
    strokeWeight(1);
    stroke(255);
    ellipse(cx, cy, d, d);
    float x = r * cos(angle * (i + 1) - HALF_PI);
    float y = r * sin(angle * (i + 1) - HALF_PI);
    
    strokeWeight(10);
    stroke(16*i, 0, 255);
    point(cx + x, cy + y);
    
    stroke(255, 150);
    strokeWeight(1);
    line(cx + x, 0, cx + x, height);
    textSize(32);
    fill(16*i, 0, 255);
    text(i+1+"x", cx-i-16, r+16);
    
    for (int j = 0; j < rows; j++) {
      curves[j][i].setX(cx + x);
      curves[j][i].r = map(i, 0, 11, 0, 255);
      curves[j][i].g = 255;
    }
  }

  noFill();
  for (int j = 0; j < rows; j++) {
    float cx = w/2;
    float cy = w + j * w + w/2;
    strokeWeight(1);
    stroke(255);
    noFill();
    ellipse(cx, cy, d, d);
    float x = r * cos(angle * (j + 1) - HALF_PI);
    float y = r * sin(angle * (j + 1) - HALF_PI);
    strokeWeight(10);
    stroke(0, 64*j, 255);
    point(cx + x, cy + y);
    stroke(255, 150);
    strokeWeight(1);
    line(0, cy + y, width, cy + y);
    textSize(32);
    fill(0, 64*j, 255);
    text(j+1+"x", r-8, cy+16);

    for (int i = 0; i < cols; i++) {
      curves[j][i].setY(cy + y);
      curves[j][i].b = map(j, 0, 5, 0, 255);
      curves[j][i].g = 255;
    }
  }
  
  for (int j = 0; j < rows; j++) {
    for (int i = 0; i < cols; i++) {
      curves[j][i].addPoint();
      curves[j][i].show();
    }
  }

  angle -= 0.01;
  
  if (keyPressed) {
    if (key == 's') {
      saving = true;
    }
  }
  
  if (angle < -TWO_PI) {
    if (saving == true) {
      saveFrame("lissajousCurve####.png");
      saving = false;
    }
    for (int j = 0; j < rows; j++) {
      for (int i = 0; i < cols; i++) {
        curves[j][i].reset();
      }
    }
    angle = 0;
  }
}
