#version 150

#moj_import <mcxr_fxaa.glsl>

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulator;
uniform vec2 InverseScreenSize;

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

float sRGBToLinear(float f) {
    if (f < 0.04045f) {
        return f / 12.92f;
    } else {
        return pow((f + 0.055f) / 1.055f, 2.4f);
    }
}

void main() {
    //TODO use a more modern aa alg (smaa?)
    //TODO make aa configuarable
    vec3 color = sample_fxaa(DiffuseSampler, texCoord, InverseScreenSize);
    vec4 mcColor = vec4(color, texture(DiffuseSampler, texCoord).a) * vertexColor * ColorModulator;

    // apply inverse gamma correction since minecraft renders in sRGB space but we want our output to be linear
    fragColor = vec4(sRGBToLinear(mcColor.r), sRGBToLinear(mcColor.g), sRGBToLinear(mcColor.b), mcColor.a);
}