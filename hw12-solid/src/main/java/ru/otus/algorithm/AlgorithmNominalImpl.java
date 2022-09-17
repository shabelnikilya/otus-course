package ru.otus.algorithm;

import ru.otus.exception.TypeBanknoteException;
import ru.otus.models.Nominal;
import ru.otus.models.PairNominalAmount;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmNominalImpl implements Algorithm<Nominal, PairNominalAmount> {

    @Override
    public List<Nominal> calculationBanknotes(long sum, List<PairNominalAmount> values) {
        List<Nominal> result = new ArrayList<>();
        long updateSum = sum;
        for (int i = 0; i < values.size() && updateSum != 0; i++) {
            PairNominalAmount pna = values.get(i);
            long amount = pna.getAmountBanknotes();
            if (amount > 0) {
                long div = updateSum / pna.getNominal().getValue();
                if (div > 0) {
                    long min = Math.min(div, amount);
                    pna.minusBanknotes(min);
                    updateSum = updateSum - min * pna.getNominal().getValue();
                    addResult(result, pna.getNominal(), min);
                }
            }
        }
        validateTypeBanknotes(updateSum);
        return result;
    }

    private static void validateTypeBanknotes(long updateSum) {
        if (updateSum > 0) {
            throw new TypeBanknoteException("Нету возможности выдать данную сумму тк нету банкнот необходимого номинала!");
        }
    }

    private static void addResult(List<Nominal> result, Nominal nominalValue, long min) {
        for (int i = 0; i < min; i++) {
            result.add(nominalValue);
        }
    }
}
