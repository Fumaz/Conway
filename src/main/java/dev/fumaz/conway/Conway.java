package dev.fumaz.conway;

import dev.fumaz.conway.command.ConwayCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Conway extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("conway").setExecutor(new ConwayCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
