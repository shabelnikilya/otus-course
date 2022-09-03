package ru.otus.models;

public class NodeRuble extends Node {
    private PairRubleAmount value;

    public NodeRuble(PairRubleAmount pairRubleAmount, Node next) {
        super(next);
        this.value = pairRubleAmount;
    }

    public PairRubleAmount getValue() {
        return value;
    }

    public int getBanknoteValue() {
        return this.value.getRuble().getValue();
    }

    public long getAmountBanknote() {
        return this.value.getAmountBanknotes();
    }
}
