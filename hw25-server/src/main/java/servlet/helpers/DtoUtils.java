package servlet.helpers;

import crm.model.Client;
import crm.model.Phone;
import dto.ClientDto;

import java.util.List;
import java.util.Objects;

public final class DtoUtils {

    private DtoUtils() {
    }

    public static ClientDto toDto(Client client) {
        String stringTelephones = numbersToString(client);
        return ClientDto.builder()
                .name(client.getName())
                .address(client.getAddress() == null ? null : client.getAddress().getStreet())
                .phones(stringTelephones)
                .build();
    }

    private static String numbersToString(Client client) {
        StringBuilder stringTelephones = null;
        List<Phone> phoneList = client.getPhones();
        if (phoneList != null) {
            stringTelephones = new StringBuilder();
            String delimiter = "";
            for (Phone p : phoneList) {
                stringTelephones.append(delimiter)
                        .append(p.getNumber());
                delimiter = " / ";
            }
        }
        return Objects.requireNonNull(stringTelephones).toString();
    }
}
