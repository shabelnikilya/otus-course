package ru.otus.store;

import ru.otus.algorithm.Algorithm;
import ru.otus.exception.NotHaveMoneyException;
import ru.otus.models.PairNominalAmount;
import ru.otus.models.Nominal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StoreBanknotesImpl implements StoreBanknotes<Nominal> {
    private final Algorithm<Nominal, PairNominalAmount> algorithm;
    private final List<PairNominalAmount> pairNominalAmounts = new ArrayList<>();

    public StoreBanknotesImpl(Algorithm<Nominal, PairNominalAmount> algorithm) {
        this.algorithm = algorithm;
        initNominalValues();
    }

    private void initNominalValues() {
        List<PairNominalAmount> pairNominalAmounts = Arrays.stream(Nominal.values())
                .map(PairNominalAmount::of)
                .sorted((p1, p2) -> p2.getNominalValue() - p1.getNominalValue())
                .collect(Collectors.toList());
        this.pairNominalAmounts.addAll(pairNominalAmounts);
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
        validateValueLessBalance(value);
        return new ArrayList<>(algorithm.calculationBanknotes(value, pairNominalAmounts));
    }

    private void validateValueLessBalance(long value) {
        if (balance() < value) {
            throw new NotHaveMoneyException("Данного количества денег в АТМ нету!");
        }
    }

    @Override
    public long balance() {
        long result = 0;
        for (PairNominalAmount p : pairNominalAmounts) {
            result += p.getAmountBanknotes() * p.getNominal().getValue();
        }
        return result;
    }


}
