#version 100

#moj_import <precision.glsl>

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulator;

varying vec2 texCoord;
varying vec4 vertexColor;



void main() {
    vec4 color = texture2D(DiffuseSampler, texCoord) * vertexColor;

    // blit final output of compositor into displayed back buffer
    gl_FragColor = color * ColorModulator;
}
