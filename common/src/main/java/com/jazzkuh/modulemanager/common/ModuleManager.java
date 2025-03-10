package com.jazzkuh.modulemanager.common;

import lombok.Getter;
import com.jazzkuh.modulemanager.common.loader.ModuleFinder;
import com.jazzkuh.modulemanager.common.modules.AbstractModule;
import com.jazzkuh.modulemanager.common.modules.components.ComponentRegistry;
import lombok.Setter;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
public class ModuleManager {

    public enum State {
        IDLE,
        LOAD,
        ENABLE,
        DISABLE
    }

    private final Logger logger;
    private State state;

    @Setter
    private boolean debug;

    @Getter protected ComponentRegistry componentRegistry;

    private final Map<Class<? extends AbstractModule<?>>, AbstractModule<?>> modules;

    public ModuleManager(Logger logger) {
        this.logger = logger;
        this.state = State.IDLE;
        this.modules = new LinkedHashMap<>();
        this.componentRegistry = new ComponentRegistry(this);
        this.debug = true;
    }

    public void prepare(AbstractModule<?> module) {
        if (modules.containsKey(module.getClass()))
            throw new IllegalArgumentException("Module is already registered");

        modules.put((Class<? extends AbstractModule<?>>) module.getClass(), module);
    }

    public void scanModules(Class<?> rootClass) {
        scanModules(rootClass.getPackage().getName());
    }

    public void scanModules(String rootPackage) {
        ModuleFinder moduleFinder = new ModuleFinder(this);
        for (Class<? extends AbstractModule<?>> moduleClass : moduleFinder.getModulesInOrder(rootPackage).keySet()) {
            AbstractModule<?> module = moduleFinder.getModulesInOrder(rootPackage).get(moduleClass);

            if (!module.shouldLoad()) continue;
            modules.put(moduleClass, module);
        }
    }

    public void load() {
        if (!state.equals(State.IDLE))
            throw new IllegalStateException("ModuleManager is not in IDLE state");
        state = State.LOAD;

        for (AbstractModule<?> module : modules.values()) {
            try {
                module.load();
            } catch (Throwable throwable) {
                logger.error(String.valueOf(throwable));
            }
        }
    }

    public void enable() {
        if (!state.equals(State.LOAD))
            throw new IllegalStateException("ModuleManager is not in LOAD state");
        state = State.ENABLE;

        for (AbstractModule<?> module : modules.values()) {
            try {
                module.enable();
            } catch (Throwable throwable) {
                logger.error(String.valueOf(throwable));
            }
        }
    }

    public void disable() {
        if (!state.equals(State.ENABLE))
            throw new IllegalStateException("ModuleManager is not in ENABLE state");
        state = State.DISABLE;

        LinkedList<AbstractModule<?>> reversedModules = new LinkedList<>(modules.values());
        Collections.reverse(reversedModules);

        for (AbstractModule<?> module : modules.values()) {
            try {
                module.disable();
            } catch (Throwable throwable) {
                logger.error(String.valueOf(throwable));
            }
        }
    }

    public <M extends AbstractModule<?>> M get(Class<M> clazz) {
        AbstractModule<?> module = modules.get(clazz);
        if (module == null || !module.getClass().equals(clazz))
            throw new IllegalArgumentException("Module is not registered");

        return clazz.cast(module);
    }
}
