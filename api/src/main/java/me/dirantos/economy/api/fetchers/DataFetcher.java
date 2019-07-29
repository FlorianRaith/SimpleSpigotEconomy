package me.dirantos.economy.api.fetchers;

import me.dirantos.economy.api.models.MMApiModel;

import java.util.Set;
import java.util.function.Consumer;

public interface DataFetcher<M extends MMApiModel, I> {

    void createTableIfNotExists();

    void createTableIfNotExistsAsync();
    
    M fetchData(I id);

    void fetchDataAsync(I id, Consumer<M> callback);

    Set<M> fetchMultipleData(Set<I> ids);

    void fetchMultipleDataAsync(Set<I> ids, Consumer<Set<M>> callback);

    M saveData(M data);

    void saveDataAsync(M data, Consumer<M> callback);

    void saveMultipleData(Set<M> data);

    void saveMultipleDataAsync(Set<M> data);

    void deleteData(I id);

    void deleteDataAsync(I id);
    
}
