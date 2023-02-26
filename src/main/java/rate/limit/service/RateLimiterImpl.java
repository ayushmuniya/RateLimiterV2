package rate.limit.service;

import rate.limit.model.RateLimit;

import java.util.HashMap;
import java.util.Map;

public class RateLimiterImpl implements RateLimiter {
    private static final int WINDOW_SIZE = 10; // sliding window size
    private final Map<String, RateLimit> rateLimits = new HashMap<>();
    private final Map<String, long[]> slidingWindows = new HashMap<>();
    private final Map<String, Integer> requestCounts = new HashMap<>();

    @Override
    public void initialize(RateLimit[] config) {
        for (RateLimit rateLimit : config) {
            String key = rateLimit.getApiName() + ":" + rateLimit.getUserId();
            rateLimits.put(key, rateLimit);
            slidingWindows.put(key, new long[WINDOW_SIZE]);
            requestCounts.put(key, 0);
        }
    }

    @Override
    public boolean isAllowed(String apiName, String userId) {
        String key = apiName + ":" + userId;
        if (rateLimits.containsKey(key)) {
            RateLimit rateLimit = rateLimits.get(key);
            long[] slidingWindow = slidingWindows.get(key);
            int requestCount = requestCounts.get(key);
            long currentTime = System.currentTimeMillis() / 1000; // convert to seconds
            long windowStart = currentTime - WINDOW_SIZE * rateLimit.getTimeUnit().ordinal();
            int windowCount = 0;
            for (int i = 0; i < slidingWindow.length; i++) {
                if (slidingWindow[i] >= windowStart && slidingWindow[i] <= currentTime) {
                    windowCount++;
                }
            }
            return windowCount < rateLimit.getMaxRequests();
        } else {
            System.out.println("API/user combination not found");
            // API/user combination not found, use default rate limit
            for (Map.Entry<String, RateLimit> entry : rateLimits.entrySet()) {
                if (entry.getValue().getUserId().equals(userId)) {
                    RateLimit rateLimit = entry.getValue();
                    long[] slidingWindow = slidingWindows.get(entry.getKey());
                    int requestCount = requestCounts.get(entry.getKey());
                    long currentTime = System.currentTimeMillis() / 1000; // convert to seconds
                    long windowStart = currentTime - WINDOW_SIZE * rateLimit.getTimeUnit().ordinal();
                    int windowCount = 0;
                    for (int i = 0; i < slidingWindow.length; i++) {
                        if (slidingWindow[i] >= windowStart && slidingWindow[i] <= currentTime) {
                            windowCount++;
                        }
                    }
                    return windowCount < rateLimit.getDefaultMaxRequests();
                }
            }
            // default rate limit not found, disallow the request
            return false;
        }
    }
    @Override
    public void addRequest(String apiName, String userId) {
        String key = apiName + ":" + userId;
        long currentTime = System.currentTimeMillis() / 1000; // convert to seconds
        if(slidingWindows.get(key) == null){
            slidingWindows.put(key, new long[WINDOW_SIZE]);
            requestCounts.put(key, 0);
        }
        long[] slidingWindow = slidingWindows.get(key);
        slidingWindow[requestCounts.get(key) % WINDOW_SIZE] = currentTime;
        requestCounts.put(key, requestCounts.get(key) + 1);
    }
}