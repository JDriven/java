package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Container extends Element {

    private SoftwareSystem parent;
    private String technology;

    private Set<Component> components = new HashSet<>();

    Container() {
        addTags(Tags.CONTAINER);
    }

    @JsonIgnore
    public SoftwareSystem getParent() {
        return parent;
    }

    void setParent(SoftwareSystem parent) {
        this.parent = parent;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public void uses(Container destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    public void uses(SoftwareSystem destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    public void uses(SoftwareSystem destination, String description, String protocol, int port, String version) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    /**
     * Adds a unidirectional "uses" style relationship between this container
     * and a component (within a container).
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     */
    public void uses(Component destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    public Component addComponentOfType(String interfaceType, String implementationType, String description, String technology) {
        Component component = getModel().addComponentOfType(this, interfaceType, implementationType, description);
        component.setTechnology(technology);

        return component;
    }

    public Component addComponent(String name, String description) {
        return getModel().addComponent(this, name, description);
    }

    public Component addComponent(String name, String description, String technology) {
        Component c = getModel().addComponent(this, name, description);
        c.setTechnology(technology);
        return c;
    }

    void add(Component component) {
        if (getComponentWithName(component.getName()) == null) {
            components.add(component);
        }
    }

    public Set<Component> getComponents() {
        return components;
    }

    public Component getComponentWithName(String name) {
        if (name == null) {
            return null;
        }

        Optional<Component> component = components.stream().filter(c -> name.equals(c.getName())).findFirst();

        if (component.isPresent()) {
            return component.get();
        } else {
            return null;
        }
    }

    public Component getComponentOfType(String type) {
        if (type == null) {
            return null;
        }

        Optional<Component> component = components.stream().filter(c -> type.equals(c.getInterfaceType())).findFirst();

        if (component.isPresent()) {
            return component.get();
        } else {
            component = components.stream().filter(c -> type.equals(c.getImplementationType())).findFirst();
            if (component.isPresent()) {
                return component.get();
            } else {
                return null;
            }
        }
    }

    @Override
    public ElementType getType() {
        return ElementType.Container;
    }

    @Override
    public String getCanonicalName() {
        return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + getName();
    }

}
