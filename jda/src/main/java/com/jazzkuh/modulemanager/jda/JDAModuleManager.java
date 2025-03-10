package com.jazzkuh.modulemanager.jda;

import com.jazzkuh.modulemanager.common.ModuleManager;
import com.jazzkuh.modulemanager.jda.handlers.listeners.ListenerComponentHandler;
import com.jazzkuh.modulemanager.jda.handlers.tasks.TaskComponentHandler;
import lombok.Getter;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.TimerTask;
import org.slf4j.Logger;

public final class JDAModuleManager<I extends IDiscordBot> extends ModuleManager {

    @Getter private final I botInstance;

    public JDAModuleManager(I botInstance, Logger logger) {
        super(logger);
        this.botInstance = botInstance;

        getComponentRegistry().registerComponentHandler(TimerTask.class, new TaskComponentHandler());
        getComponentRegistry().registerComponentHandler(ListenerAdapter.class, new ListenerComponentHandler());
    }
}
