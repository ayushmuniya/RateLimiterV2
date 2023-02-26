package rate.limit.model;


public class RateLimit {
    private final String apiName;
    private final String userId;
    private final int maxRequests;
    private final TimeUnit.Unit timeUnit;
    private final int defaultMaxRequests;

    public RateLimit(String apiName, String userId, int maxRequests, TimeUnit.Unit timeUnit, int defaultMaxRequests) {
        this.apiName = apiName;
        this.userId = userId;
        this.maxRequests = maxRequests;
        this.timeUnit = timeUnit;
        this.defaultMaxRequests = defaultMaxRequests;
    }

    public String getApiName() {
        return apiName;
    }

    public String getUserId() {
        return userId;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public TimeUnit.Unit getTimeUnit() {
        return timeUnit;
    }

    public int getDefaultMaxRequests() {
        return defaultMaxRequests;
    }
}
