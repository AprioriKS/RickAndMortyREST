package restapi.rickandmortyapp.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import restapi.rickandmortyapp.dto.external.ApiCharacterDto;
import restapi.rickandmortyapp.dto.external.ApiResponseDto;
import restapi.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import restapi.rickandmortyapp.model.MovieCharacter;
import restapi.rickandmortyapp.repository.MovieCharacterRepository;

@Log4j2
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper mapper;

    public MovieCharacterServiceImpl(HttpClient httpClient,
        MovieCharacterRepository movieCharacterRepository,
        MovieCharacterMapper mapper) {
        this.httpClient = httpClient;
        this.movieCharacterRepository = movieCharacterRepository;
        this.mapper = mapper;
    }

    @Scheduled(cron = "0 8 * * * ?")
    @Override
    public void syncExternalCharacter() {
        log.info("syncExternalCharacter was invoked at " + LocalDateTime.now());
        ApiResponseDto apiResponseDto = httpClient.get("https://rickandmortyapi.com/api/character",
            ApiResponseDto.class);

        saveDtosToDB(apiResponseDto);

        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient
                .get(apiResponseDto.getInfo().getNext(), ApiResponseDto.class);
            saveDtosToDB(apiResponseDto);
        }
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        long count = movieCharacterRepository.count();
        long randomId = (long) (Math.random() * count);
        return movieCharacterRepository.findById(randomId).get();
    }

    @Override
    public List<MovieCharacter> findAllByNameContains(String namePart) {
        return movieCharacterRepository.findAllByNameContains(namePart);
    }

    private void saveDtosToDB(ApiResponseDto responseDto) {

        Map<Long, ApiCharacterDto> externalDtos = Arrays.stream(responseDto.getResults())
            .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));

        Set<Long> externalIds = externalDtos.keySet();

        List<MovieCharacter> existingCharacters = movieCharacterRepository
            .findAllByExternalIdIn(externalIds);

        Map<Long, MovieCharacter> existingCharacterWithIds = existingCharacters.stream()
            .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));

        Set<Long> existingIds = existingCharacterWithIds.keySet();

        externalIds.removeAll(existingIds);

        List<MovieCharacter> charactersToSave = externalIds.stream()
            .map(i -> mapper.parseApiCharacterResponseDto(externalDtos.get(i)))
            .collect(Collectors.toList());
        movieCharacterRepository.saveAll(charactersToSave);
    }
}
