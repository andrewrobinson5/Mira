#version 450 core

layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec4 vertexColor;
layout(location = 2) in vec2 vertexTexCoord;

out vec4 ourColor;
out vec2 texCoord;

void main() {
	gl_Position = vec4(vertexPosition_modelspace, 1.0);
	ourColor = vertexColor;
	texCoord = vertexTexCoord;
}
