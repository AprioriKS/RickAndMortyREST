package restapi.rickandmortyapp.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import restapi.rickandmortyapp.dto.CharacterResponseDto;
import restapi.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import restapi.rickandmortyapp.model.MovieCharacter;
import restapi.rickandmortyapp.service.MovieCharacterService;

@RestController
@RequestMapping("/movie-characters")
public class MovieCharacterController {
    public final MovieCharacterService characterService;
    public final MovieCharacterMapper mapper;

    public MovieCharacterController(MovieCharacterService characterService,
                                    MovieCharacterMapper mapper) {
        this.characterService = characterService;
        this.mapper = mapper;
    }

    @GetMapping("/random")
    public CharacterResponseDto getRandom() {
        MovieCharacter randomCharacter =  characterService.getRandomCharacter();
        return mapper.toResponseDto(randomCharacter);
    }

    @GetMapping("/by-name")
    public List<CharacterResponseDto> findAllByName(@RequestParam("name") String namePart) {
        return characterService.findAllByNameContains(namePart).stream()
            .map(mapper::toResponseDto)
            .collect(Collectors.toList());
    }

}
