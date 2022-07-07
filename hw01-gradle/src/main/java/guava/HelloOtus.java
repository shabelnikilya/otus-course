package guava;

import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {
        Integer inceptionArray = 1;
        Integer[] numbersArray = {2, 3, 4, 5};

        List<Integer> numbersList = Lists.asList(inceptionArray, numbersArray);
        numbersList.forEach(System.out::println);
    }
}
