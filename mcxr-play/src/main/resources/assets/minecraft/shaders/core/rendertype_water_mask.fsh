#version 100

#moj_import <precision.glsl>

uniform vec4 ColorModulator;



void main() {
    gl_FragColor = ColorModulator;
}
