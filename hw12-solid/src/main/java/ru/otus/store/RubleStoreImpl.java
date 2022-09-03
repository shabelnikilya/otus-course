package ru.otus.store;

import ru.otus.algorithm.Algorithm;
import ru.otus.exception.NotHaveMoneyException;
import ru.otus.models.NodeRuble;
import ru.otus.models.PairRubleAmount;
import ru.otus.models.Ruble;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * АТМ
 */
public class RubleStoreImpl implements Store<Ruble> {
    private final Algorithm<NodeRuble> algorithm;
    private NodeRuble first;
    private NodeRuble last;

    public RubleStoreImpl(Algorithm<NodeRuble> algorithm) {
        this.algorithm = algorithm;
        initNodes();
    }

    @Override
    public void add(List<Ruble> moneys) {
        NodeRuble node = first;
        while (node != null) {
            for (Ruble r : moneys) {
                if (node.getValue().getRuble().equals(r)) {
                    node.getValue().addOneRuble();
                }
            }
            node = (NodeRuble) node.getNext();
        }
    }

    @Override
    public List<Ruble> giveBanknotes(long value) {
        if (balance() < value) {
            throw new NotHaveMoneyException("Данного количества денег в АТМ нету!");
        }
        return algorithm.calculationBanknotes(value, first).stream()
                .map(n -> n.getValue().getRuble())
                .collect(Collectors.toList());
    }

    @Override
    public long balance() {
        long result = 0;
        NodeRuble node = first;
        while (node != null) {
            PairRubleAmount pair = node.getValue();
            result += pair.getAmountBanknotes() * pair.getRuble().getValue();
            node = (NodeRuble) node.getNext();
        }
        return result;
    }

    private void initNodes() {
        Ruble[] rubles = Ruble.values();
        Arrays.sort(rubles, (r1, r2) -> Integer.compare(r2.getValue(), r1.getValue()));
        for (Ruble ruble : rubles) {
            if (first == null) {
                first = new NodeRuble(PairRubleAmount.of(ruble), null);
                last = first;
            } else {
                NodeRuble node = new NodeRuble(PairRubleAmount.of(ruble), null);
                last.setNext(node);
                last = node;
            }
        }
    }
}
