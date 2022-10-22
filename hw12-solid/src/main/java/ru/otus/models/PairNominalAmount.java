package ru.otus.models;

public class PairNominalAmount extends Amount {
    private final Nominal nominal;

    private PairNominalAmount(Nominal ruble) {
        this.nominal = ruble;
    }

    public static PairNominalAmount of(Nominal ruble) {
        return new PairNominalAmount(ruble);
    }

    public Nominal getNominal() {
        return nominal;
    }

    public int getNominalValue() {
        return nominal.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairNominalAmount pair)) return false;

        if (amountBanknotes != pair.amountBanknotes) return false;
        return nominal == pair.nominal;
    }

    @Override
    public int hashCode() {
        int result = nominal != null ? nominal.hashCode() : 0;
        result = 31 * result + (int) (amountBanknotes ^ (amountBanknotes >>> 32));
        return result;
    }
}
