package com.openglengine.renderer.gui;

import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.util.*;

/**
 * Manager for all gui related stuff
 * 
 * @author Dominik
 *
 */
public class GuiManager implements ResourceManager {
	/** all gui elements that are managed by this resource manager */
	private List<GuiElement> guiElements;

	/** we only need one guimodel */
	private final GuiModel model;

	/** we only need one gui shader for now TODO: refactor */
	private final GuiShader shader;

	public GuiManager() {
		this.guiElements = new ArrayList<>();
		this.shader = new GuiShader();
		this.model = new GuiModel(this.shader);

		// Engine.getGlobalEventManager().registerListenerForEvent(UpdateEvent.class,
		// e -> update(((UpdateEvent) e).getDeltatime()));

		// TODO: change system so that rendering is always in a guaranteed order (first entities, then gui)
		Engine.getGlobalEventManager().registerListenerForEvent(RenderEvent.class, e -> render());
	}

	/**
	 * Add and element to the gui system. This will use() the element
	 * 
	 * @param element
	 */
	public void addGuiElement(GuiElement element) {
		if (!this.guiElements.contains(element)) {
			this.guiElements.add(element);
			element.use();
		}
	}

	/**
	 * Remove an element from the gui. This will cleanup() the element
	 * 
	 * @param element
	 */
	public void removeGuiElement(GuiElement element) {
		if (this.guiElements.remove(element))
			element.cleanup();
	}

	// TODO: implement sophisticated gui system with input, update etc
	// private void update(double deltatime) {
	//
	// }

	private void render() {
		// Prepare drawing
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		this.shader.startUsingShader();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		// Render each gui element
		this.guiElements.forEach(e -> this.renderElement(e));

		// Quit drawing routine
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		this.shader.stopUsingShader();
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	private void renderElement(GuiElement element) {
		// Set texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.getTexture().getTextureID());

		// Set model matrix
		Engine.getModelMatrixStack().push();
		Engine.getModelMatrixStack().loadIdentity();
		Engine.getModelMatrixStack().translate(element.getPosition().x, element.getPosition().y, 0);
		Engine.getModelMatrixStack().scale(element.getScale().x, element.getScale().y, 1);

		// Upload
		this.shader.uploadModelMatrixUniform();

		// Draw
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getIndicesCount());

		// Pop model matrix
		Engine.getModelMatrixStack().pop();
	}

	@Override
	public void cleanup() {
		this.guiElements.forEach(e -> e.cleanup());
		this.shader.forceDelete();
		this.model.cleanup();
	}

}
