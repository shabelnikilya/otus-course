package ru.otus;

import ru.otus.models.Nominal;
import ru.otus.store.StoreBanknotes;

import java.util.List;

/**
 * Банкомат.
 */
public class ATMBankomat {
    private final StoreBanknotes<Nominal> store;

    public ATMBankomat(StoreBanknotes<Nominal> store) {
        this.store = store;
    }

    public void acceptBanknotes(List<Nominal> banknotes) {
        store.add(banknotes);
    }

    public List<Nominal> giveBanknotes(long value) {
        return store.giveBanknotes(value);
    }

    public long balance() {
        return store.balance();
    }
}
