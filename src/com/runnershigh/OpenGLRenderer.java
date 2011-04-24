/**
 * Copyright 2010 Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.runnershigh;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.runnershigh.Group;
import com.runnershigh.Mesh;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class OpenGLRenderer implements Renderer {
	private final Group root;
	private long timeAtLastSecond;
	private int fpsCounter;

	public OpenGLRenderer() {
		// Initialize our root.
		Group group = new Group();
		root = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
	 * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		//fuer transperancy?
		//gl.glEnable(GL10.GL_ALPHA);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); 
        
        timeAtLastSecond = System.currentTimeMillis();
        fpsCounter=0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.
	 * khronos.opengles.GL10)
	 */

	public void onDrawFrame(GL10 gl) {
		//long starttime = System.currentTimeMillis();
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		// Translates 4 units into the screen.
		// gl.glTranslatef(-1, 1, 0);
		// gl.glScalef(1, 0.5f, 1);
		// gl.glScalef(10, 10, 1);
		// Draw our scene.
		root.draw(gl);
		fpsCounter++;
		
		//long timeForOneCycle= System.currentTimeMillis()- starttime;
		if((System.currentTimeMillis() - timeAtLastSecond) > 1000){
			timeAtLastSecond = System.currentTimeMillis();
			Log.d("frametime", "draws per second: " + Integer.toString(fpsCounter));
			fpsCounter=0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition
	 * .khronos.opengles.GL10, int, int)
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.

		// gl.glViewport(0, 0, width, height);
		// GLU.gluOrtho2D(gl, 0, width, 0, height);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, 0, width, 0, height);
		// gl.glOrthox(0, width, 0, height, -100, 100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// Select the projection matrix
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		// gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		// GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
		// 1000.0f);
		// Select the modelview matrix
		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		// gl.glLoadIdentity();

	}

	/**
	 * Adds a mesh to the root.
	 * 
	 * @param mesh
	 *            the mesh to add.
	 */
	public void addMesh(Mesh mesh) {
		root.add(mesh);
	}
	
	public void removeMesh(Mesh mesh) {
		root.remove(mesh);
	}
}