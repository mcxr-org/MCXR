#version 100

#moj_import <projection.glsl>

attribute vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

varying vec4 texProj0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    texProj0 = projection_from_position(gl_Position);
}
