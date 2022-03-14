#version 100

attribute vec3 Position;
attribute vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

varying vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
}
