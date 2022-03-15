#version 100

#moj_import <precision.glsl>

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform mat4 ProjMat;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

varying vec4 vertexColor;
varying vec2 texCoord0;



void main() {
    vec4 color = texture2D(Sampler0, texCoord0);
    color *= vertexColor * ColorModulator;
    float fragmentDistance = -ProjMat[3].z / ((gl_FragCoord.z) * -2.0 + 1.0 - ProjMat[2].z);
    gl_FragColor = linear_fog(color, fragmentDistance, FogStart, FogEnd, FogColor);
}
