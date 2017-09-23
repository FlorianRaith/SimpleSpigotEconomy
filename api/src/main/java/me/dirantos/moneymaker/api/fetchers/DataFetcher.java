package me.dirantos.moneymaker.api.fetchers;

import java.util.Set;
import java.util.function.Consumer;

interface DataFetcher<T, I> {

    void createTableIfNotExists();

    void createTableIfNotExistsAsync();
    
    T fetchData(I id);

    void fetchDataAsync(I id, Consumer<T> callback);

    Set<T> fetchMultipleData(Set<I> ids);

    void fetchMultipleDataAsync(Set<I> ids, Consumer<Set<T>> callback);

    T saveData(T data);

    void saveDataAsync(T data, Consumer<T> callback);

    void saveMultipleData(Set<T> data);

    void saveMultipleDataAsync(Set<T> data);

    void deleteData(I id);

    void deleteDataAsync(I id);
    
}
