package com.bo.test;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * DrawCircle.java
 * Copyright(C) EnRich DTV Group Co.,Ltd.
 * 功能描述:
 * 功能描述XXX
 * 创建者: qiubo@evmtv.com
 * 编辑者: qiubo@evmtv.com
 * 2023 2023/3/31 14:43
 */
public class DrawCircle {

	public static final float[] DEFAULT_VERTEX = {
			-1.0f, -1.0f, //左下
			1.0f, -1.0f, //右下
			-1.0f, 1.0f, //左上
			1.0f, 1.0f   //右上
	};

	public static void drawCircle(float x, float y, float radius, float aspectRatio) {
		// 定义顶点坐标数组
		final int segments = 32;
		float[] vertexData = new float[segments * 2];
		for (int i = 0; i < segments; i++) {
			float angle = (float) (2 * Math.PI * i / segments);
			float x1 = x + radius * (float) Math.cos(angle) * aspectRatio;
			float y1 = y + radius * (float) Math.sin(angle);
			vertexData[i * 2] = x1;
			vertexData[i * 2 + 1] = y1;
		}

		// 创建顶点缓冲对象
		FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer()
				.put(vertexData);
		vertexBuffer.position(0);

		// 定义着色器程序
		String vertexShaderCode =
				"attribute vec4 a_Position;\n" +
						"void main() {\n" +
						"  gl_Position = a_Position;\n" +
						"  gl_PointSize = 10.0;\n" +
						"}";
		String fragmentShaderCode =
				"precision mediump float;\n" +
						"uniform vec4 u_Color;\n" +
						"void main() {\n" +
						"  gl_FragColor = u_Color;\n" +
						"}";
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		// 创建着色器程序
		int program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);
		GLES20.glLinkProgram(program);
		GLES20.glUseProgram(program);

		// 绑定顶点坐标属性
		int aPositionLocation = GLES20.glGetAttribLocation(program, "a_Position");
		GLES20.glEnableVertexAttribArray(aPositionLocation);
		GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);

		// 设置颜色
		int uColorLocation = GLES20.glGetUniformLocation(program, "u_Color");
		GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

		// 绘制圆形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, segments);

		// 绘制直线
		float[] lineVertices = {0.2f, 0.3f, 0.9f, 0.7f};
		FloatBuffer lineVertexBuffer = ByteBuffer.allocateDirect(lineVertices.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer()
				.put(lineVertices);
		lineVertexBuffer.position(0);

		GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glLineWidth(5.0f);
		GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 0, lineVertexBuffer);
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
	}

	// 加载着色器
	private static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}






}
