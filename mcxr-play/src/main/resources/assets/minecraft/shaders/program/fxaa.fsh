#version 100
precision highp float;

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;

uniform float VxOffset;
uniform float SpanMax;
uniform float ReduceMul;

varying vec2 texCoord;
varying vec4 posPos;



vec3 FxaaPixelShader(
  vec4 posPos,   // Output of FxaaVertexShader interpolated across screen.
  sampler2D tex, // Input texture2D.
  vec2 rcpFrame) // Constant {1.0/frameWidth, 1.0/frameHeight}.
{

    #define FXAA_REDUCE_MIN   (1.0/128.0)
    //#define FXAA_REDUCE_MUL   (1.0/8.0)
    //#define FXAA_SPAN_MAX     8.0

    vec3 rgbNW = texture2D(tex, posPos.zw).xyz;
    vec3 rgbNE = texture2DOffset(tex, posPos.zw, ivec2(1,0)).xyz;
    vec3 rgbSW = texture2DOffset(tex, posPos.zw, ivec2(0,1)).xyz;
    vec3 rgbSE = texture2DOffset(tex, posPos.zw, ivec2(1,1)).xyz;

    vec3 rgbM  = texture2D(tex, posPos.xy).xyz;

    vec3 luma = vec3(0.299, 0.587, 0.114);
    float lumaNW = dot(rgbNW, luma);
    float lumaNE = dot(rgbNE, luma);
    float lumaSW = dot(rgbSW, luma);
    float lumaSE = dot(rgbSE, luma);
    float lumaM  = dot(rgbM,  luma);

    float lumaMvarying = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
    float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));

    vec2 dir;
    dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
    dir.y =  ((lumaNW + lumaSW) - (lumaNE + lumaSE));

    float dirReduce = max(
        (lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * ReduceMul),
        FXAA_REDUCE_MIN);
    float rcpDirMvarying = 1.0/(min(abs(dir.x), abs(dir.y)) + dirReduce);
    dir = min(vec2( SpanMax,  SpanMax),
          max(vec2(-SpanMax, -SpanMax),
          dir * rcpDirMin)) * rcpFrame.xy;

    vec3 rgbA = (1.0/2.0) * (
    texture2D(tex, posPos.xy + dir * vec2(1.0/3.0 - 0.5)).xyz +
    texture2D(tex, posPos.xy + dir * vec2(2.0/3.0 - 0.5)).xyz);
    vec3 rgbB = rgbA * (1.0/2.0) + (1.0/4.0) * (
    texture2D(tex, posPos.xy + dir * vec2(0.0/3.0 - 0.5)).xyz +
    texture2D(tex, posPos.xy + dir * vec2(3.0/3.0 - 0.5)).xyz);

    float lumaB = dot(rgbB, luma);

    if ((lumaB < lumaMin) || (lumaB > lumaMax)) {
        return rgbA;
    } else {
        return rgbB;
    }
}

void main() {
    vec4 baseTexel = texture2D(DiffuseSampler, posPos.xy);
    gl_FragColor = vec4(FxaaPixelShader(posPos, DiffuseSampler, 1.0 / OutSize), 1.0);
}
