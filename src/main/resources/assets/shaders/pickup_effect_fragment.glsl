#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;
uniform float u_intensity;
uniform vec3 u_pickupColor;

void main() {
    vec4 base = v_color * texture2D(u_texture, v_texCoords);
    float pulse = 0.65 + 0.35 * sin(u_time * 24.0);
    float glow = clamp(u_intensity * pulse, 0.0, 1.0);
    vec3 boosted = mix(base.rgb, u_pickupColor, 0.6 * glow);
    gl_FragColor = vec4(boosted, base.a);
}
