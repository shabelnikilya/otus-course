package ru.otus.algorithm;

import ru.otus.exception.TypeBanknoteException;
import ru.otus.models.NodeRuble;

import java.util.ArrayList;
import java.util.List;

public class NodeRubleAlgorithmImpl implements Algorithm<NodeRuble> {

    @Override
    public List<NodeRuble> calculationBanknotes(long sum, NodeRuble node) {
        List<NodeRuble> result = new ArrayList<>();
        NodeRuble thisNode = node;
        long updateSum = sum;
        while (updateSum != 0 && thisNode != null) {
            long amount = thisNode.getAmountBanknote();
            if (amount > 0) {
                long div = updateSum / thisNode.getBanknoteValue();
                if (div > 0) {
                    long min = Math.min(div, amount);
                    thisNode.getValue().minusRubles(min);
                    updateSum = updateSum - min * thisNode.getBanknoteValue();
                    addResult(result, thisNode, min);
                }
            }
            thisNode = (NodeRuble) thisNode.getNext();
        }
        validateTypeBanknotes(updateSum);
        return result;
    }

    private static void validateTypeBanknotes(long updateSum) {
        if (updateSum > 0) {
            throw new TypeBanknoteException("Нету возможности выдать данную сумму тк нету банкнот необходимого номинала!");
        }
    }

    private static void addResult(List<NodeRuble> result, NodeRuble thisNode, long min) {
        for (int i = 0; i < min; i++) {
            result.add(thisNode);
        }
    }
}
