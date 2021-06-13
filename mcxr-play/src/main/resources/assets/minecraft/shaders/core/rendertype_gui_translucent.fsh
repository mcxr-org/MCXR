#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform sampler2D Sampler3;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    float depth = texture(Sampler1, texCoord0).r;
    if (depth != 1.0) { //Fixes an issue with gui rendering which i will document later
        color.a = 1.0;
    }
    if (color.a < 0.1) {
        discard;
    }

    fragColor = color;

    //if (color.a < 0.1) {
    //    discard;
    //}
  //  color *= vertexColor * ColorModulator;
  //  color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
  //  color *= lightMapColor;
   // fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
    //fragColor = color;
}
