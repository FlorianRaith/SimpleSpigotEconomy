package me.dirantos.economy.api.cache;

import me.dirantos.economy.api.models.MMApiModel;

import java.util.HashMap;
import java.util.Map;

public final class Cache<M extends MMApiModel, I> {

    private Map<I, M> cache = new HashMap<>();

    public boolean has(I id) {
        return cache.containsKey(id);
    }

    public void add(I id, M model) {
        cache.put(id, model);
    }

    public M get(I id) {
        return has(id) ? cache.get(id) : null;
    }

    public void remove(I id) {
        if(has(id)) cache.remove(id);
    }

}
