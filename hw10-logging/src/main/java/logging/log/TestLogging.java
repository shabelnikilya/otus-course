package logging.log;


public interface TestLogging {
    void showParams();

    void showParams(int first, int second);

    void showParams(int first, int second, int third);

    int randomNumberInPeriod(int startPeriod, int endPeriod);

    void notHaveAnnotation();
}
