package ru.otus.store;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.ATMBankomat;
import ru.otus.algorithm.AlgorithmNominalImpl;
import ru.otus.exception.NotHaveMoneyException;
import ru.otus.exception.TypeBanknoteException;
import ru.otus.models.Nominal;

import java.util.List;

@DisplayName("Тесты для объекта класса АТМ!")
class StoreImplTest {

    private ATMBankomat atmBankomat;

    @BeforeEach
    void init() {
        Store<Nominal> nominalStore = new StoreNominalImpl(new AlgorithmNominalImpl());
        atmBankomat = new ATMBankomat(nominalStore);
    }

    @DisplayName("Добавление валют в хранилище и проверка его баланса")
    @Test
    void whenAddRublesInStoreThenCorrectBalance() {
        Nominal firstRuble = Nominal.FIFTY;
        atmBankomat.acceptBanknotes(List.of(firstRuble));
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(firstRuble.getValue());

        Nominal secondRuble = Nominal.ONE_THOUSAND;
        Nominal thirdRuble = Nominal.ONE;
        atmBankomat.acceptBanknotes(List.of(secondRuble, thirdRuble));
        int expected = firstRuble.getValue() + secondRuble.getValue() + thirdRuble.getValue();
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(expected);
    }

    @DisplayName("Проверки получение валют минимальным количеством купюр")
    @Test
    void whenMinusRublesFromStoreThenReturnMinAmountBanknotes() {
        atmBankomat.acceptBanknotes(List.of(Nominal.ONE_THOUSAND, Nominal.ONE_HUNDRED, Nominal.FIFTY, Nominal.FIFTY));
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(1200);

        Assertions.assertThat(atmBankomat.giveBanknotes(1000)).isEqualTo(List.of(Nominal.ONE_THOUSAND));
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(200);
        Assertions.assertThat(atmBankomat.giveBanknotes(100)).isEqualTo(List.of(Nominal.ONE_HUNDRED));
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(100);

        atmBankomat.acceptBanknotes(List.of(Nominal.ONE_THOUSAND, Nominal.ONE_HUNDRED));
        Assertions.assertThat(atmBankomat.giveBanknotes(200)).isEqualTo(
                List.of(
                        Nominal.ONE_HUNDRED,
                        Nominal.FIFTY,
                        Nominal.FIFTY
                ));
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(1000);
    }

    @DisplayName("Проверки запроса суммы больше, чем есть в АТМ")
    @Test
    void whenMinusRublesFromStoreThenNotHaveMoneyException() {
        atmBankomat.acceptBanknotes(List.of(Nominal.ONE_THOUSAND, Nominal.ONE_HUNDRED, Nominal.FIFTY, Nominal.FIFTY));
        int expected = 1200;
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(expected);

        Assertions.assertThatThrownBy(() -> atmBankomat.giveBanknotes(1201))
                .isInstanceOf(NotHaveMoneyException.class);

        Assertions.assertThat(atmBankomat.giveBanknotes(1200))
                .isEqualTo(
                        List.of(
                                Nominal.ONE_THOUSAND,
                                Nominal.ONE_HUNDRED,
                                Nominal.FIFTY,
                                Nominal.FIFTY
                        )
                );
        Assertions.assertThat(atmBankomat.balance()).isEqualTo(0);
    }

    @DisplayName("Проверки запроса суммы, которая есть в АТМ, но невозможно выдать тк нету соответствующих банкнот")
    @Test
    void whenMinusRublesFromStoreThenTypeBanknoteException() {
        atmBankomat.acceptBanknotes(List.of(Nominal.ONE_THOUSAND));

        Assertions.assertThatThrownBy(() -> atmBankomat.giveBanknotes(700)).isInstanceOf(TypeBanknoteException.class);
    }
}