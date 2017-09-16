package me.dirantos.moneymaker.spigot.fetchers;

import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;
import org.bukkit.Bukkit;

import java.util.Set;
import java.util.function.Consumer;

public abstract class DataFetcher<T, I> {

    private final MySQLConnectionPool mySQL;
    private final MoneyMakerPlugin plugin;

    public DataFetcher(MySQLConnectionPool mySQL, MoneyMakerPlugin plugin) {
        this.mySQL = mySQL;
        this.plugin = plugin;
    }

    public abstract void createTableIfNotExists();

    public final void createTableIfNotExistsAsync() {
        runAsync(() -> createTableIfNotExists());
    }

    public abstract T fetchData(I id);

    public final void fetchDataAsync(I id, Consumer<T> callback) {
        runAsync(() -> callback.accept(fetchData(id)));
    }

    public abstract Set<T> fetchMultipleData(Set<I> ids);

    public final void fetchMultipleDataAsync(Set<I> ids, Consumer<Set<T>> callback) {
        runAsync(() -> callback.accept(fetchMultipleData(ids)));
    }

    public abstract void saveData(T data);

    public final void saveDataAsync(T data) {
        runAsync(() -> saveData(data));
    }

    public abstract void saveMultipleData(Set<T> data);

    public final void saveMultipleDataAsync(Set<T> data) {
        runAsync(() -> saveMultipleData(data));
    }

    public abstract void deleteData(I id);

    public final void deleteDataAsync(I id) {
        runAsync(() -> deleteData(id));
    }

    public final MySQLConnectionPool getMySQL() {
        return mySQL;
    }

    public final MoneyMakerPlugin getPlugin() {
        return plugin;
    }

    private final void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    protected final String multipleInsertBuilder(String query, String replace, int size) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < size; i++) {
            sb.append(replace + (i == (size-1) ? "" : ", "));
        }

        return query.replace("$values$", sb.toString());
    }

    protected final String multipleFetchBuilder(String query, int size) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < size; i++) {
            sb.append((i == (size-1) ? "?)" : "?, "));
        }

        return query.replace("$values$", sb.toString());
    }


}
