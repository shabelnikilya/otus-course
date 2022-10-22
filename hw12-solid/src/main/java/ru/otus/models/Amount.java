package ru.otus.models;

import ru.otus.exception.NotHaveMoneyException;

public class Amount {
    protected long amountBanknotes;

    public long getAmountBanknotes() {
        return amountBanknotes;
    }

    public void addOneBanknote() {
        amountBanknotes++;
    }

    public void minusBanknotes(long amount) {
        if (amount > amountBanknotes) {
            throw new NotHaveMoneyException("Недостаточно средств!");
        }
        amountBanknotes = amountBanknotes - amount;
    }
}
