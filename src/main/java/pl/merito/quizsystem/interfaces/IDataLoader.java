package pl.merito.quizsystem.interfaces;

public interface IDataLoader<T> {
    T load(String path, Class<T> type);
}
