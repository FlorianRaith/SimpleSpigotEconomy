package me.dirantos.economy.spigot;

import me.dirantos.economy.api.DataFetcher;
import me.dirantos.economy.api.DataModel;
import org.bukkit.Bukkit;

import java.util.Set;
import java.util.function.Consumer;

public abstract class DataFetcherImpl<T extends DataModel, I> implements DataFetcher<T, I> {

    private final MySQLConnectionPool mySQL;
    private final EconomyPlugin plugin;
    private final ModelCache cache;

    public DataFetcherImpl(MySQLConnectionPool mySQL, EconomyPlugin plugin, ModelCache cache) {
        this.mySQL = mySQL;
        this.plugin = plugin;
        this.cache = cache;
    }

    @Override
    public final void createTableIfNotExistsAsync() {
        runAsync(this::createTableIfNotExists);
    }

    @Override
    public final void fetchDataAsync(I id, Consumer<T> callback) {
        runAsync(() -> callback.accept(fetchData(id)));
    }

    @Override
    public final void fetchMultipleDataAsync(Set<I> ids, Consumer<Set<T>> callback) {
        runAsync(() -> callback.accept(fetchMultipleData(ids)));
    }

    @Override
    public final void saveDataAsync(T data, Consumer<T> callback) {
        runAsync(() -> callback.accept(saveData(data)));
    }

    @Override
    public final void saveMultipleDataAsync(Set<T> data) {
        runAsync(() -> saveMultipleData(data));
    }

    @Override
    public final void deleteDataAsync(I id) {
        runAsync(() -> deleteData(id));
    }

    public final MySQLConnectionPool getMySQL() {
        return mySQL;
    }

    public final EconomyPlugin getPlugin() {
        return plugin;
    }

    private final void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    protected ModelCache getCache() {
        return cache;
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

        return query.replace("$values$", size == 0 ? "()" : sb.toString());
    }


}
