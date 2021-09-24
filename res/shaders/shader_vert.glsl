#version 450 core

layout(location = 0) in vec4 vertexPosition_modelspace;
layout(location = 1) in vec4 aColor;
layout(location = 3) in vec2 texCoord;

out vec4 ourColor;

void main() {
	gl_Position = vertexPosition_modelspace;
	ourColor = aColor;
}
