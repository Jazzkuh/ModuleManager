package com.jazzkuh.modulemanager.minestom;

import com.jazzkuh.modulemanager.common.ModuleManager;
import com.jazzkuh.modulemanager.common.tasks.ISteppingTask;
import com.jazzkuh.modulemanager.minestom.handlers.listeners.Listener;
import com.jazzkuh.modulemanager.minestom.handlers.listeners.ListenerComponentHandler;
import com.jazzkuh.modulemanager.minestom.handlers.listeners.MinestomListenerComponentHandler;
import com.jazzkuh.modulemanager.minestom.handlers.listeners.PacketListener;
import com.jazzkuh.modulemanager.minestom.handlers.listeners.PacketListenerComponentHandler;
import com.jazzkuh.modulemanager.minestom.handlers.tasks.SteppingTaskComponentHandler;
import com.jazzkuh.modulemanager.minestom.handlers.tasks.TaskComponentHandler;
import lombok.Getter;
import net.minestom.server.event.EventListener;
import org.slf4j.Logger;

@Getter
public final class MinestomModuleManager<S extends IMinestomPlatform> extends ModuleManager {

    private final S server;

    public MinestomModuleManager(S server, Logger logger) {
        super(logger);
        this.server = server;

        getComponentRegistry().registerComponentHandler(EventListener.class, new MinestomListenerComponentHandler());
        getComponentRegistry().registerComponentHandler(Listener.class, new ListenerComponentHandler());
        getComponentRegistry().registerComponentHandler(PacketListener.class, new PacketListenerComponentHandler());
        getComponentRegistry().registerComponentHandler(Runnable.class, new TaskComponentHandler());
        getComponentRegistry().registerComponentHandler(ISteppingTask.class, new SteppingTaskComponentHandler());
    }
}
