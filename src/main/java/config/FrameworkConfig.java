package config;

public class FrameworkConfig {
    private static final String DEFAULT_BASE_URL = "http://3.68.165.45";
    private static final String BASE_URL_PROPERTY = "baseUrl";
    private static final String THREAD_COUNT_PROPERTY = "threadCount";
    private static final int DEFAULT_THREAD_COUNT = 3;

    public static String getBaseUrl() {
        String url = System.getProperty(BASE_URL_PROPERTY);
        return (url != null && !url.isEmpty()) ? url : DEFAULT_BASE_URL;
    }

    public static int getThreadCount() {
        String threads = System.getProperty(THREAD_COUNT_PROPERTY);
        return (threads != null && !threads.isEmpty()) ? Integer.parseInt(threads) : DEFAULT_THREAD_COUNT;
    }
}