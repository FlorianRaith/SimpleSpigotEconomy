package me.dirantos.moneymaker.components.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class ConfigFile {

    private static Map<String, FileConfiguration> cachedConfigs = new HashMap<>();
    private static Map<String, File> cachedFiles = new HashMap<>();
    private final String yaml;
    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;

    public ConfigFile(JavaPlugin plugin, String yaml) {
        this.yaml = yaml;
        this.plugin = plugin;
        this.file = getFile();
        this.config = getFileConfiguration();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reload() {
        this.config = getFileConfiguration();
    }

    public boolean save() {
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private File getFile() {
        File folderFile = new File("plugins" + File.separator + plugin.getName());
        if(!folderFile.exists()) {
            folderFile.mkdir();
        }

        String path = folderFile.getPath() + File.separator + yaml;

        if (cachedFiles.containsKey(path)) {
            return cachedFiles.get(path);
        }
        File file = new File(path);
        File dir = file.getParentFile();
        if (dir.getName().equalsIgnoreCase("plugins") && !file.exists()) {
            file.mkdir();
            return file;
        }
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cachedFiles.put(path, file);
        return file;
    }

    private FileConfiguration getFileConfiguration() {
        if (cachedConfigs.containsKey(file.getPath())) {
            return cachedConfigs.get(file.getPath());
        }
        InputStreamReader in;
        try {
            in = new InputStreamReader(new FileInputStream(file), "UTF8");
            FileConfiguration n = YamlConfiguration.loadConfiguration(in);
            cachedConfigs.put(file.getPath(), n);
            return n;
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
