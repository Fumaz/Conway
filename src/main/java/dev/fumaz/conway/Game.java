package dev.fumaz.conway;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.commons.bukkit.misc.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

public class Game implements FListener {

    private final static int SIZE = 100;
    private final static World WORLD = Bukkit.getWorld("conway");
    private final static Location MINIMUM = new Location(WORLD, -99, 68, 0);
    private final static Location MAXIMUM = new Location(WORLD, 0, 68, 99);
    private final static BoundingBox BOX = BoundingBox.of(MINIMUM, MAXIMUM);

    private final JavaPlugin plugin;
    private boolean[][] board;
    private boolean running;
    private int speed;

    private int tick;

    public Game(JavaPlugin plugin) {
        this.plugin = plugin;
        this.board = new boolean[SIZE][SIZE];
        this.running = false;
        this.speed = 10;
        this.tick = 0;

        register(plugin);
        Scheduler.of(plugin).runTaskTimer(this::step, 0, 1);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isRunning() {
        return running;
    }

    public int getSpeed() {
        return speed;
    }

    public void randomize() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                board[x][y] = Math.random() < 0.5;

                Block block = WORLD.getBlockAt(MINIMUM.getBlockX() + x, 68, MINIMUM.getBlockZ() + y);
                block.setType(board[x][y] ? Material.BLACK_CONCRETE : Material.WHITE_CONCRETE);
            }
        }
    }

    public void clear() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                board[x][y] = false;

                Block block = WORLD.getBlockAt(MINIMUM.getBlockX() + x, 68, MINIMUM.getBlockZ() + y);
                block.setType(Material.WHITE_CONCRETE);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        boolean delete = event.getAction().isLeftClick();
        Block block = event.getPlayer().getTargetBlock(100);

        if (block == null || block.isEmpty()) {
            return;
        }

        if (block.getWorld() != WORLD) {
            return;
        }

        if (block.getX() < MINIMUM.getBlockX() || block.getX() > MAXIMUM.getBlockX() || block.getZ() < MINIMUM.getBlockZ() || block.getZ() > MAXIMUM.getBlockZ()) {
            return;
        }

        int x = block.getX() - MINIMUM.getBlockX();
        int z = block.getZ() - MINIMUM.getBlockZ();

        board[x][z] = !delete;
        block.setType(delete ? Material.WHITE_CONCRETE : Material.BLACK_CONCRETE);
    }

    private void step() {
        if (!running) {
            return;
        }

        if (++tick % speed != 0) {
            return;
        }

        boolean[][] next = new boolean[100][100];

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                int neighbors = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if ((x + i >= 0 && x + i < SIZE) && (y + j >= 0 && y + j < SIZE)) {
                            neighbors += board[x + i][y + j] ? 1 : 0;
                        }
                    }
                }

                neighbors -= board[x][y] ? 1 : 0;

                if (board[x][y]) {
                    next[x][y] = neighbors == 2 || neighbors == 3;
                } else {
                    next[x][y] = neighbors == 3;
                }
            }
        }

        board = next;

        display();
    }

    private void display() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                boolean value = board[x][y];

                WORLD.getBlockAt(MINIMUM.clone().add(x, 0, y)).setType(value ? Material.BLACK_CONCRETE : Material.WHITE_CONCRETE);
            }
        }
    }


}
