#version 100

#moj_import <precision.glsl>

#moj_import <fog.glsl>

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;

varying float vertexDistance;
varying vec4 vertexColor;



void main() {
    gl_FragColor = vertexColor * ColorModulator * linear_fog_fade(vertexDistance, FogStart, FogEnd);
}
