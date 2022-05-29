package restapi.rickandmortyapp.dto;

import lombok.Data;
import restapi.rickandmortyapp.model.Gender;
import restapi.rickandmortyapp.model.Status;

@Data
public class CharacterResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private Status status;
    private Gender gender;
}
