package me.dirantos.moneymaker.components.config;

public abstract class AbstractConfig {

    private final ConfigFile config;

    public AbstractConfig(ConfigFile config) {
        this.config = config;
    }

    protected Object get(String path, Object def) {
        if(config.getConfig().isSet(path)) {
            return config.getConfig().get(path);
        } else {
            config.getConfig().set(path, def);
            config.save();
            return def;
        }
    }

    protected String getString(String path, String def) {
        return get(path, def).toString();
    }

    protected int getInt(String path, int def) {
        return (int) get(path, def);
    }

    protected double getDouble(String path, double def) {
        return (double) get(path, def);
    }

}
