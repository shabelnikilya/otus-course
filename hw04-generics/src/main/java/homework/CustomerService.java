package homework;

import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparing(Customer::getScores));
    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customers.firstEntry();
        Customer customer = entry.getKey();
        return Map.entry(new Customer(customer.getId(), customer.getName(), customer.getScores()), entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customers.higherEntry(customer);
        if (Objects.isNull(entry)) {
            return null;
        }
        Customer resultCustomer = entry.getKey();
        return Map.entry(new Customer(
                        resultCustomer.getId(),
                        resultCustomer.getName(),
                        resultCustomer.getScores()
        ), entry.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
