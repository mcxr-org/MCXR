#version 100

#moj_import <precision.glsl>

#moj_import <fog.glsl>

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

varying float vertexDistance;
varying vec4 vertexColor;



void main() {
    vec4 color = vertexColor * ColorModulator;
    gl_FragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
