package com.openglengine.entitity.component;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;

/**
 * Component for rendering simple model data without a texture
 * 
 * @author Dominik
 *
 */
public class RenderComponent extends Component {
	/** Shader program */
	protected Shader shader;

	/** Loaded model to render */
	protected Model model;

	/**
	 * Initialize this render component
	 * 
	 * @param modelPath
	 *            path to the model file
	 * @param shader
	 *            shader program
	 */
	public RenderComponent(String modelPath, Shader shader) {
		this.shader = shader;
		this.model = Engine.MODEL_MANAGER.getModel(modelPath);
	}

	@Override
	public void update(Entity entity) {
		// Bind vao for use
		GL30.glBindVertexArray(this.model.getVaoID());

		// Enable first vertex attrib array (Vertex data)
		GL20.glEnableVertexAttribArray(0);

		// Load shader
		this.shader.startUsingShader();

		// Setup transform matrix
		TransformationMatrixStack tms = Engine.MODEL_MATRIX_STACK;
		tms.push();
		tms.translate(entity.position);
		tms.rotateX(entity.rotX);
		tms.rotateY(entity.rotY);
		tms.rotateZ(entity.rotZ);
		tms.scale(entity.scale, entity.scale, entity.scale);

		// Upload relevant matricies to shader TODO: refactor
		this.shader.uploadUniforms();

		// Draw using our bound data
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.model.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);

		// Disable first vertex attrib array (Vertex data)
		GL20.glDisableVertexAttribArray(0);

		// Unbind vao
		GL30.glBindVertexArray(0);

		// Undo changes to ModelMatrix
		tms.pop();

		// Unload shader TODO: save overhead by managing shaders centrally
		this.shader.stopUsingShader();
	}

	@Override
	public void receiveEvent(BaseEvent event) {
		// Do nothing for now
	}

	@Override
	public void cleanup() {
		this.model.cleanup();
		this.shader.cleanup();
	}

}
