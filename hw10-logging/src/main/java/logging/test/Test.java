package logging.test;

import logging.log.TestLogging;

public interface Test {

    void action();

    default void defaultAction(TestLogging logging) {
        logging.showParams();
        logging.showParams(1, 2);
        logging.showParams(10, 20, 30);
        System.out.println(logging.randomNumberInPeriod(10, 100));
        logging.notHaveAnnotation();
    }
}
