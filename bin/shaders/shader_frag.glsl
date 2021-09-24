#version 450 core

out vec4 fragColor;
in vec4 ourColor;

void main() {
  fragColor = ourColor;
}
