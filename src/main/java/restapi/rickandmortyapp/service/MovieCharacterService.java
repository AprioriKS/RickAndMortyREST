package restapi.rickandmortyapp.service;

import java.util.List;
import restapi.rickandmortyapp.model.MovieCharacter;

public interface MovieCharacterService {
    void syncExternalCharacter();

    MovieCharacter getRandomCharacter();

    List<MovieCharacter> findAllByNameContains(String namePart);
}
