#version 450 core

out vec4 fragColor;

in vec4 ourColor;
in vec2 texCoord;

uniform sampler2D ourTexture;

void main() {
	vec4 texColor = texture(ourTexture, texCoord/2-0.5);
	if(texColor.a < 0.5)
		discard;
	fragColor = texColor;
}
