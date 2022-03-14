#version 100

#moj_import <precision.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

varying vec2 texCoord0;
varying vec2 texCoord2;
varying vec4 vertexColor;

void main() {
    vec4 color = texture2D(Sampler0, texCoord0) * vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    gl_FragColor = color * ColorModulator;
}
