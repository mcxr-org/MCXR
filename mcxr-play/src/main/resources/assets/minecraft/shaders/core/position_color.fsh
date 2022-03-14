#version 100

#moj_import <precision.glsl>

varying vec4 vertexColor;

uniform vec4 ColorModulator;

void main() {
    vec4 color = vertexColor;
    if (color.a == 0.0) {
        discard;
    }
    gl_FragColor = color * ColorModulator;
}
