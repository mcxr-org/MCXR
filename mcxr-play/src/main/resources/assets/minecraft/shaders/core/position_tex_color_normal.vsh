#version 100

attribute vec3 Position;
attribute vec2 UV0;
attribute vec4 Color;
attribute vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

varying vec2 texCoord0;
varying float vertexDistance;
varying vec4 vertexColor;
varying vec4 normal;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    texCoord0 = UV0;
    vertexDistance = length((ModelViewMat * vec4(Position, 1.0)).xyz);
    vertexColor = Color;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
