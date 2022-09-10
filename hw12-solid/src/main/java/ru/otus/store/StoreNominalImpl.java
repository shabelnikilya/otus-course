package ru.otus.store;

import ru.otus.algorithm.Algorithm;
import ru.otus.exception.NotHaveMoneyException;
import ru.otus.models.PairNominalAmount;
import ru.otus.models.Nominal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StoreNominalImpl implements Store<Nominal> {
    private final Algorithm<Nominal, PairNominalAmount> algorithm;

    private final List<PairNominalAmount> pairNominalAmounts = new ArrayList<>();

    public StoreNominalImpl(Algorithm<Nominal, PairNominalAmount> algorithm) {
        this.algorithm = algorithm;
        initNominalValues();
    }

    @Override
    public void add(List<Nominal> moneys) {
        for (Nominal nominal : moneys) {
            for (PairNominalAmount pairNominalAmount : pairNominalAmounts) {
                if (pairNominalAmount.getNominal() == nominal) {
                    pairNominalAmount.addOneBanknote();
                }
            }
        }
    }

    @Override
    public List<Nominal> giveBanknotes(long value) {
        if (balance() < value) {
            throw new NotHaveMoneyException("Данного количества денег в АТМ нету!");
        }
        return new ArrayList<>(algorithm.calculationBanknotes(value, pairNominalAmounts));
    }

    @Override
    public long balance() {
        long result = 0;
        for (PairNominalAmount p : pairNominalAmounts) {
            result += p.getAmountBanknotes() * p.getNominal().getValue();
        }
        return result;
    }

    private void initNominalValues() {
        Nominal[] nominalValues = Nominal.values();
        Arrays.sort(nominalValues, (r1, r2) -> Integer.compare(r2.getValue(), r1.getValue()));
        List<PairNominalAmount> pairNominalAmounts = Arrays.stream(nominalValues)
                .map(PairNominalAmount::of)
                .collect(Collectors.toList());
        this.pairNominalAmounts.addAll(pairNominalAmounts);
    }
}
