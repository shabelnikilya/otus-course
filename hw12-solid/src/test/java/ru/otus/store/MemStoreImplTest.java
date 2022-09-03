package ru.otus.store;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.algorithm.Algorithm;
import ru.otus.algorithm.NodeRubleAlgorithmImpl;
import ru.otus.exception.NotHaveMoneyException;
import ru.otus.exception.TypeBanknoteException;
import ru.otus.models.NodeRuble;
import ru.otus.models.Ruble;

import java.util.List;

@DisplayName("Тесты для объекта класса АТМ!")
class MemStoreImplTest {
    private Store<Ruble> rubleStore;

    @BeforeEach
    void init() {
        Algorithm<NodeRuble> algorithm = new NodeRubleAlgorithmImpl();
        rubleStore = new RubleStoreImpl(algorithm);
    }

    @DisplayName("Добавление валют в хранилище и проверка его баланса")
    @Test
    void whenAddRublesInStoreThenCorrectBalance() {
        Ruble firstRuble = Ruble.FIFTY;
        rubleStore.add(List.of(firstRuble));
        Assertions.assertThat(rubleStore.balance()).isEqualTo(firstRuble.getValue());

        Ruble secondRuble = Ruble.ONE_THOUSAND;
        Ruble thirdRuble = Ruble.ONE;
        rubleStore.add(List.of(secondRuble, thirdRuble));
        int expected = firstRuble.getValue() + secondRuble.getValue() + thirdRuble.getValue();
        Assertions.assertThat(rubleStore.balance()).isEqualTo(expected);
    }

    @DisplayName("Проверки получение валют минимальным количеством купюр")
    @Test
    void whenMinusRublesFromStoreThenReturnMinAmountBanknotes() {
        rubleStore.add(List.of(Ruble.ONE_THOUSAND, Ruble.ONE_HUNDRED, Ruble.FIFTY, Ruble.FIFTY));
        Assertions.assertThat(rubleStore.balance()).isEqualTo(1200);

        Assertions.assertThat(rubleStore.giveBanknotes(1000)).isEqualTo(List.of(Ruble.ONE_THOUSAND));
        Assertions.assertThat(rubleStore.balance()).isEqualTo(200);
        Assertions.assertThat(rubleStore.giveBanknotes(100)).isEqualTo(List.of(Ruble.ONE_HUNDRED));
        Assertions.assertThat(rubleStore.balance()).isEqualTo(100);

        rubleStore.add(List.of(Ruble.ONE_THOUSAND, Ruble.ONE_HUNDRED));
        Assertions.assertThat(rubleStore.giveBanknotes(200)).isEqualTo(
                List.of(
                        Ruble.ONE_HUNDRED,
                        Ruble.FIFTY,
                        Ruble.FIFTY
                ));
        Assertions.assertThat(rubleStore.balance()).isEqualTo(1000);
    }

    @DisplayName("Проверки запроса суммы больше, чем есть в АТМ")
    @Test
    void whenMinusRublesFromStoreThenNotHaveMoneyException() {
        rubleStore.add(List.of(Ruble.ONE_THOUSAND, Ruble.ONE_HUNDRED, Ruble.FIFTY, Ruble.FIFTY));
        int expected = 1200;
        Assertions.assertThat(rubleStore.balance()).isEqualTo(expected);

        Assertions.assertThatThrownBy(() -> rubleStore.giveBanknotes(1201))
                .isInstanceOf(NotHaveMoneyException.class);

        Assertions.assertThat(rubleStore.giveBanknotes(1200))
                .isEqualTo(
                        List.of(
                                Ruble.ONE_THOUSAND,
                                Ruble.ONE_HUNDRED,
                                Ruble.FIFTY,
                                Ruble.FIFTY
                        )
                );
        Assertions.assertThat(rubleStore.balance()).isEqualTo(0);
    }

    @DisplayName("Проверки запроса суммы, которая есть в АТМ, но невозможно выдать тк нету соответствующих банкнот")
    @Test
    void whenMinusRublesFromStoreThenTypeBanknoteException() {
        rubleStore.add(List.of(Ruble.ONE_THOUSAND));

        Assertions.assertThatThrownBy(() -> rubleStore.giveBanknotes(700)).isInstanceOf(TypeBanknoteException.class);
    }
}