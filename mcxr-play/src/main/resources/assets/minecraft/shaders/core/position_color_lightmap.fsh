#version 100

#moj_import <precision.glsl>

uniform sampler2D Sampler2;

uniform vec4 ColorModulator;

varying vec4 vertexColor;
varying vec2 texCoord2;

void main() {
    vec4 color = texture2D(Sampler2, texCoord2) * vertexColor;
    gl_FragColor = color * ColorModulator;
}
