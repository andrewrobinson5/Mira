#version 450 core

out vec4 fragColor;

in vec4 ourColor;
in vec2 texCoord;

void main() {

	fragColor = ourColor;
}
