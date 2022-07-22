#version 150

#moj_import <mcxr_fxaa.glsl>

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulator;
uniform vec2 InverseScreenSize;

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    //TODO use a more modern aa alg (smaa?)
    //TODO make aa configuarable
    vec3 color = sample_fxaa(DiffuseSampler, texCoord, InverseScreenSize);
    vec4 mcColor = vec4(color, texture(DiffuseSampler, texCoord).a) * vertexColor * ColorModulator;

    // we are rendering to an SRGB texture so we can leave the colors in the SRGB color space
    fragColor = mcColor;
}