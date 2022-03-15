#version 100

attribute vec3 Position;
attribute vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

varying float vertexDistance;
varying vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = length((ModelViewMat * vec4(Position, 1.0)).xyz);
    vertexColor = Color;
}
