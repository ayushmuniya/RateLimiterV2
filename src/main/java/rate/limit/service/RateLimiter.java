package rate.limit.service;

import rate.limit.model.RateLimit;

interface RateLimiter {
     void initialize(RateLimit[] config);
     boolean isAllowed(String apiName, String userId);
     void addRequest(String apiName, String userId);
}