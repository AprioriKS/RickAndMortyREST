package restapi.rickandmortyapp.dto.mapper;

import org.springframework.stereotype.Component;
import restapi.rickandmortyapp.dto.CharacterResponseDto;
import restapi.rickandmortyapp.dto.external.ApiCharacterDto;
import restapi.rickandmortyapp.model.Gender;
import restapi.rickandmortyapp.model.MovieCharacter;
import restapi.rickandmortyapp.model.Status;

@Component
public class MovieCharacterMapper {

    public MovieCharacter parseApiCharacterResponseDto(ApiCharacterDto dto) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setName(dto.getName());
        movieCharacter.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        movieCharacter.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        movieCharacter.setExternalId(dto.getId());
        return movieCharacter;
    }

    public CharacterResponseDto toResponseDto(MovieCharacter movieCharacter) {
        CharacterResponseDto dto = new CharacterResponseDto();
        dto.setId(movieCharacter.getId());
        dto.setExternalId(movieCharacter.getExternalId());
        dto.setName(movieCharacter.getName());
        dto.setStatus(movieCharacter.getStatus());
        dto.setGender(movieCharacter.getGender());
        return dto;
    }
}
