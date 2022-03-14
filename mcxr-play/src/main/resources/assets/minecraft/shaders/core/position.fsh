#version 100

#moj_import <precision.glsl>

#moj_import <fog.glsl>

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

varying float vertexDistance;

void main() {
    gl_FragColor = linear_fog(ColorModulator, vertexDistance, FogStart, FogEnd, FogColor);
}
