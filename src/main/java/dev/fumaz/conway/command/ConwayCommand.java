package dev.fumaz.conway.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.conway.Game;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ConwayCommand implements PlayerCommandExecutor {

    private final Game game;

    public ConwayCommand(JavaPlugin plugin) {
        this.game = new Game(plugin);
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] strings) {
        if (strings[0].equalsIgnoreCase("toggle")) {
            game.setRunning(!game.isRunning());
        } else if (strings[0].equalsIgnoreCase("speed")) {
            int speed = Integer.parseInt(strings[1]);
            game.setSpeed(speed);
        } else if (strings[0].equalsIgnoreCase("random")) {
            game.randomize();
        } else if (strings[0].equalsIgnoreCase("clear")) {
            game.clear();
        }
    }

}
