package com.jazzkuh.modulemanager.common.modules;

import com.jazzkuh.modulemanager.common.ModuleManager;
import lombok.Getter;
import com.jazzkuh.modulemanager.common.modules.components.ComponentRegistry;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractModule<M extends ModuleManager> {
    @Getter private boolean enabled;
    @Getter private final M owningManager;
    private final ComponentRegistry registry;
    private final List<Object> components;
    @Getter private final Logger logger;

    protected AbstractModule(M owningManager) {
        this.owningManager = owningManager;
        this.registry = owningManager.getComponentRegistry();
        this.components = new ArrayList<>();
        this.logger = LoggerFactory.getLogger(getClass().getSimpleName());
    }

    public final void load() {
        onLoad();
        registry.execute(ModuleManager.State.LOAD, components);
    }

    public void onLoad() {
        //Can be overwritten
    }

    public final void enable() {
        onEnable();
        registry.execute(ModuleManager.State.ENABLE, components);
        this.enabled = true;
    }

    public void onEnable() {
        //Can be overwritten
    }

    public final void disable() {
        onDisable();
        registry.execute(ModuleManager.State.DISABLE, components);
    }

    public void onDisable() {
        //Can be overwritten
    }

    public boolean shouldLoad() {
        return true;
    }

    public void registerComponent(Object component) {
        if (!registry.isClassRegistered(component.getClass()))
            throw new IllegalArgumentException("Component class is not registered in the registry");

        if (owningManager.isDebug()) getLogger().info("Registering component: " + component.getClass().getSimpleName());
        components.add(component);
    }

    public String getName() {
        return getClass().getSimpleName();
    }
}
