#version 100

#moj_import <precision.glsl>

uniform sampler2D Sampler0;

varying vec4 vertexColor;
varying vec2 texCoord0;
varying vec2 texCoord1;
varying vec2 texCoord2;
varying vec4 normal;



void main() {
    vec4 color = texture2D(Sampler0, texCoord0);
    if (color.a < vertexColor.a) {
        discard;
    }
    gl_FragColor = color;
}
