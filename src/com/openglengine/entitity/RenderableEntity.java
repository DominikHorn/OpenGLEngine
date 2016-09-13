package com.openglengine.entitity;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.renderer.texture.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * An entity which may be rendered
 * 
 * @author Dominik
 *
 */
public class RenderableEntity<ShaderClass extends Shader> implements RenderDelegate<ShaderClass>, ResourceManager {
	/** Entity's globally unique id */
	private int entityUID = 0;

	/** Entity's components */
	private List<RenderableEntityComponent> components;

	/** Event manager for component events */
	private EventManager<ComponentEvent> componentEventSystem;

	/** the entities model. may be null */
	private Model<?> model;

	/** index in texture atlas that should be used to render this entity */
	private int textureAtlasIndex;

	/** Entity's position in the game world */
	public Vector3f position;

	/** Entity's rotation on all three axis */
	public Vector3f rotation;

	/** Entity's scale in all three directions */
	public Vector3f scale;

	/**
	 * 
	 * @param model
	 */
	public RenderableEntity(Model<?> model) {
		this(model, new Vector3f());
	}

	/**
	 * 
	 * @param model
	 * @param position
	 */
	public RenderableEntity(Model<?> model, Vector3f position) {
		this(model, position, new Vector3f());
	}

	/**
	 * 
	 * @param model
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public RenderableEntity(Model<?> model, Vector3f position, Vector3f scale) {
		this(model, position, new Vector3f(), scale);
	}

	/**
	 * 
	 * @param model
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public RenderableEntity(Model<?> model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.entityUID = EntityUIDContainer.GLOBAL_ENTITY_ID_CNT++;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
		this.components = new ArrayList<>();
		this.textureAtlasIndex = 0;
		this.componentEventSystem = new EventManager<>();
	}

	/**
	 * retrieve the globally unique id of this entity
	 * 
	 * @return
	 */
	public int getEntityUID() {
		return this.entityUID;
	}

	/**
	 * Set the texture atlas index
	 * 
	 * @param textureAtlasIndex
	 */
	public void setTextureAtlasIndex(int textureAtlasIndex) {
		this.textureAtlasIndex = textureAtlasIndex;
	}

	/**
	 * Retrieve this entity's model
	 * 
	 * @return
	 */
	public Model<?> getModel() {
		return this.model;
	}

	/**
	 * Set this entity's model
	 * 
	 * @param model
	 */
	public void setModel(Model<?> model) {
		this.model = model;
	}

	/**
	 * add a component to this entity
	 * 
	 * @param component
	 * @return convenience self return to make addComponent().addComponent().addComponent()... possible
	 */
	public RenderableEntity<ShaderClass> addComponent(RenderableEntityComponent component) {
		// TODO: implement component reordering because some components might be execution order dependent
		component.init(this);
		this.components.add(component);

		return this;
	}

	/**
	 * remove a component from this entity
	 * 
	 * @param component
	 * @return convenience self return to make removeComponent().removeComponent().removeComponent()... possible
	 */
	public RenderableEntity<ShaderClass> removeComponent(RenderableEntityComponent component) {
		this.components.remove(component);

		return this;
	}

	/**
	 * Update this entity. Internally, this will update all components of this entity
	 */
	public void update() {
		this.components.forEach(c -> c.update(this));
	}

	/**
	 * Retrieve the event system of this entity
	 * 
	 * @return
	 */
	public EventManager<ComponentEvent> getComponentEventSystem() {
		return this.componentEventSystem;
	}

	@Override
	public void initRendercode(ShaderClass shader) {
		// Set model transform matrix
		TransformationMatrixStack tms = Engine.getModelMatrixStack();
		tms.push();
		tms.translate(this.position);
		tms.rotateX(this.rotation.x);
		tms.rotateY(this.rotation.y);
		tms.rotateZ(this.rotation.z);
		tms.scale(this.scale.x, this.scale.y, this.scale.z);
	}

	@Override
	public void deinitRendercode() {
		Engine.getModelMatrixStack().pop();
	}

	@Override
	public float getTextureOffsetX(Texture texture) {
		return (float) ((int) this.textureAtlasIndex % texture.getTextureAtlasRows())
				/ (float) texture.getTextureAtlasRows();
	}

	@Override
	public float getTextureOffsetY(Texture texture) {
		return (float) ((int) this.textureAtlasIndex / texture.getTextureAtlasRows())
				/ (float) texture.getTextureAtlasRows();
	}

	@Override
	public void cleanup() {
		this.components.forEach(c -> c.cleanup());

		this.model.cleanup();
	}
}
