package com.openglengine.entitity;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;

/**
 * An entity which may be rendered
 * 
 * @author Dominik
 *
 */
public class RenderableEntity implements RenderDelegate, ResourceManager {
	/** Entity's globally unique id */
	private int entityUID = 0;

	/** Entity's components */
	private List<RenderableEntityComponent> components;

	/** Event manager for component events */
	private EventManager<ComponentEvent> componentEventSystem;

	/** Entity's position in the game world */
	public Vector3f position;

	/** Entity's rotation on all three axis */
	public Vector3f rotation;

	/** Entity's scale in all three directions */
	public Vector3f scale;

	/** the entities model. may be null */
	public Model model;

	/**
	 * 
	 * @param model
	 */
	public RenderableEntity(Model model) {
		this(model, new Vector3f());
	}

	/**
	 * 
	 * @param model
	 * @param position
	 */
	public RenderableEntity(Model model, Vector3f position) {
		this(model, position, new Vector3f());
	}

	/**
	 * 
	 * @param model
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public RenderableEntity(Model model, Vector3f position, Vector3f scale) {
		this(model, position, new Vector3f(), scale);
	}

	/**
	 * 
	 * @param model
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public RenderableEntity(Model model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.entityUID = EntityUIDContainer.GLOBAL_ENTITY_ID_CNT++;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
		this.components = new ArrayList<>();
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
	 * add a component to this entity
	 * 
	 * @param component
	 * @return convenience self return to make addComponent().addComponent().addComponent()... possible
	 */
	public RenderableEntity addComponent(RenderableEntityComponent component) {
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
	public RenderableEntity removeComponent(RenderableEntityComponent component) {
		this.components.remove(component);

		return this;
	}

	/**
	 * Update this entity. Internally, this will update all components of this entity
	 */
	public void update() {
		this.components.forEach(c -> c.update(this));
	}

	public EventManager<ComponentEvent> getComponentEventSystem() {
		return this.componentEventSystem;
	}

	@Override
	public void initRendercode() {
		// Set model transform
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
	public void cleanup() {
		this.components.forEach(c -> c.cleanup());

		this.model.cleanup();
	}
}
