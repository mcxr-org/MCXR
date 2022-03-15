#version 100

#moj_import <precision.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

varying vec4 vertexColor;
varying vec2 texCoord0;



void main() {
    vec4 color = texture2D(Sampler0, texCoord0);
    if (color.a == 0.0) {
        discard;
    }
    gl_FragColor = vec4(ColorModulator.rgb * vertexColor.rgb, ColorModulator.a);
}
