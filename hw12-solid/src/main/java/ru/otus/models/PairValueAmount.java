package ru.otus.models;

import ru.otus.exception.NotHaveMoneyException;

public class PairValueAmount {
    protected long amountBanknotes;

    public long getAmountBanknotes() {
        return amountBanknotes;
    }

    public void addOneRuble() {
        amountBanknotes++;
    }

    public void minusRubles(long amount) {
        if (amount > amountBanknotes) {
            throw new NotHaveMoneyException("Недостаточно средств!");
        }
        amountBanknotes = amountBanknotes - amount;
    }
}
