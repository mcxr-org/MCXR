#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in vec2 texCoord0;

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
}
