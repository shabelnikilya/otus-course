package ru.otus.models;

import ru.otus.exception.NotHaveMoneyException;

public class PairRubleAmount extends PairValueAmount {
    private final Ruble ruble;

    private PairRubleAmount(Ruble ruble) {
        this.ruble = ruble;
    }

    public static PairRubleAmount of(Ruble ruble) {
        return new PairRubleAmount(ruble);
    }

    public Ruble getRuble() {
        return ruble;
    }

    public void minusRubles(long amount) {
        if (amount > amountBanknotes) {
            throw new NotHaveMoneyException("Недостаточно средств!");
        }
        amountBanknotes = amountBanknotes - amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairRubleAmount pair)) return false;

        if (amountBanknotes != pair.amountBanknotes) return false;
        return ruble == pair.ruble;
    }

    @Override
    public int hashCode() {
        int result = ruble != null ? ruble.hashCode() : 0;
        result = 31 * result + (int) (amountBanknotes ^ (amountBanknotes >>> 32));
        return result;
    }
}
