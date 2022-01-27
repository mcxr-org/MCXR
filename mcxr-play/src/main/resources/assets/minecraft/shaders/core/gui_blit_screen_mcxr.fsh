#version 100

#moj_import <precision.glsl>

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

varying vec2 texCoord;

void main() {
    vec4 color = texture2D(DiffuseSampler, texCoord);
    float depth = texture2D(DepthSampler, texCoord).r;
    // since the gui is the last rendered layer in vannila there are a few issues which
    // don't show up normally. an example of this is a transparent quad in item tooltips
    // which sets fragments to be transparent despite then actually being opaque
    if (depth != 1.0) {
        color.a = 1.0;
    }

    gl_FragColor = color;
}
