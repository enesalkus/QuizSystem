package pl.merito.quizsystem.loader;

import com.google.gson.Gson;
import pl.merito.quizsystem.interfaces.IDataLoader;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class JsonDataLoader<T> implements IDataLoader<T> {

    private final Gson gson;

    public JsonDataLoader() {
        this.gson = new Gson();
    }

    @Override
    public T load(String path, Class<T> type) {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(path), "File not found: " + path))) {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
