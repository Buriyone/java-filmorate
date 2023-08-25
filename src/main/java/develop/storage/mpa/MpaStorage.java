package develop.storage.mpa;


import develop.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa getById(int id);

    List<Mpa> get();
}
