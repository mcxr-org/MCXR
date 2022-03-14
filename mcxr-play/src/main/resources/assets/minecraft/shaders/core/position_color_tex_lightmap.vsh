#version 100

attribute vec3 Position;
attribute vec4 Color;
attribute vec2 UV0;
attribute vec2 UV2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

varying vec4 vertexColor;
varying vec2 texCoord0;
varying vec2 texCoord2;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    texCoord0 = UV0;
    texCoord2 = UV2;
}
