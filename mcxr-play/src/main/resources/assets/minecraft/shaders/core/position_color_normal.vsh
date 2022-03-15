#version 100

attribute vec3 Position;
attribute vec4 Color;
attribute vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ModelViewProjMat;

varying float vertexDistance;
varying vec4 vertexColor;
varying vec4 normal;

void main() {
    gl_Position = ModelViewProjMat * vec4(Position, 1.0);

    vertexDistance = length((ModelViewMat * vec4(Position, 1.0)).xyz);
    vertexColor = Color;
    normal = ModelViewProjMat * vec4(Normal, 0.0);
}
