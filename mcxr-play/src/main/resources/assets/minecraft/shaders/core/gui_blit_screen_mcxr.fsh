#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    float depth = texture(DepthSampler, texCoord).r;
    // since the gui is the last rendered layer in vannila there are a few issues which
    // don't show up normally. an example of this is a transparent quad in item tooltips
    // which sets fragments to be transparent despite then actually being opaque
    if (depth != 1.0) {
        color.a = 1.0;
    }

    fragColor = color;
}
