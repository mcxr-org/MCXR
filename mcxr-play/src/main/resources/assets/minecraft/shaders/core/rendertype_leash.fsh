#version 100

#moj_import <precision.glsl>

#moj_import <fog.glsl>

uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

varying float vertexDistance;
varying vec4 vertexColor;



void main() {
    gl_FragColor = linear_fog(vertexColor, vertexDistance, FogStart, FogEnd, FogColor);
}
