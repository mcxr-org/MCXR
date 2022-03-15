#version 100

#moj_import <precision.glsl>

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;

varying float vertexDistance;
varying vec2 texCoord0;



void main() {
    vec4 color = texture2D(Sampler0, texCoord0) * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
    float fade = linear_fog_fade(vertexDistance, FogStart, FogEnd);
    gl_FragColor = vec4(color.rgb * fade, color.a);
}
