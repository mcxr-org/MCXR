#version 100
#moj_import <light.glsl>
attribute vec3 Position;
attribute vec4 Color;
attribute vec2 UV2;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec4 ColorModulator;

varying float vertexDistance;
varying vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = length((ModelViewMat * vec4(Position, 1.0)).xyz);
    vertexColor = Color * ColorModulator * minecraft_sample_lightmap(Sampler2,UV2);
}
