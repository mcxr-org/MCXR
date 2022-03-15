#version 100

#moj_import <precision.glsl>

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;
uniform int EndPortalLayers;

varying vec4 texProj0;

mat4 end_portal_layer(float layer) {
    mat4 SCALE_TRANSLATE = mat4(
        0.5, 0.0, 0.0, 0.25,
        0.0, 0.5, 0.0, 0.25,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}
vec3 color_cst(int idx) {
    if(idx == 0) return vec3(0.022087, 0.098399, 0.110818);
    if(idx == 1) return vec3(0.011892, 0.095924, 0.089485);
    if(idx == 2) return vec3(0.027636, 0.101689, 0.100326);
    if(idx == 3) return vec3(0.046564, 0.109883, 0.114838);
    if(idx == 4) return vec3(0.064901, 0.117696, 0.097189);
    if(idx == 5) return vec3(0.063761, 0.086895, 0.123646);
    if(idx == 6) return vec3(0.084817, 0.111994, 0.166380);
    if(idx == 7) return vec3(0.097489, 0.154120, 0.091064);
    if(idx == 8) return vec3(0.106152, 0.131144, 0.195191);
    if(idx == 9) return vec3(0.097721, 0.110188, 0.187229);
    if(idx == 10) return vec3(0.133516, 0.138278, 0.148582);
    if(idx == 11) return vec3(0.070006, 0.243332, 0.235792);
    if(idx == 12) return vec3(0.196766, 0.142899, 0.214696);
    if(idx == 13) return vec3(0.047281, 0.315338, 0.321970);
    if(idx == 14) return vec3(0.204675, 0.390010, 0.302066);
    if(idx == 15) return vec3(0.080955, 0.314821, 0.661491);
    return vec3(0,0,0);
}

void main() {
    vec3 color = texture2DProj(Sampler0, texProj0).rgb * color_cst(0);
    for (int i = 0; i < EndPortalLayers; i++) {
        color += texture2DProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * color_cst(i);
    }
    gl_FragColor = vec4(color, 1.0);
}
