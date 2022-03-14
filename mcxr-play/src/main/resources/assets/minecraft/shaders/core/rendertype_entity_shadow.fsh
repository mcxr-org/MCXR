#version 100

#moj_import <precision.glsl>

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

varying float vertexDistance;
varying vec4 vertexColor;
varying vec2 texCoord0;



void main() {
    vec4 color = texture2D(Sampler0, clamp(texCoord0, 0.0, 1.0));
    color *= vertexColor * ColorModulator;
    gl_FragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
