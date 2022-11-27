package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class ClientDto {

    private final String name;
    private final String address;
    private final String phones;
}
