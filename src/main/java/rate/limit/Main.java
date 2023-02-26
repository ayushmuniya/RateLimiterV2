package rate.limit;

import rate.limit.model.RateLimit;
import rate.limit.model.TimeUnit;
import rate.limit.service.RateLimiterImpl;

public class Main {
    public static void main(String[] args) throws InterruptedException
    {
        RateLimit[] config = new RateLimit[]
                {
                    new RateLimit("API1", "USER1", 5, TimeUnit.Unit.SECONDS, 10),
                    new RateLimit("API2", "USER1", 10, TimeUnit.Unit.SECONDS, 20),
                    new RateLimit("API3", "USER2", 3, TimeUnit.Unit.SECONDS, 5),
                    new RateLimit("API4", "USER3", 2, TimeUnit.Unit.MINUTES, 1)
                };

        RateLimiterImpl rateLimiter = new RateLimiterImpl();
        rateLimiter.initialize(config);

        // allow the first 9 requests for API1 by USER1
        for (int i = 0; i < 9; i++) {
            boolean allowed = rateLimiter.isAllowed("API1", "USER1");
            System.out.println("Request " + (i + 1) + " for API1 by USER1: " + allowed);
            rateLimiter.addRequest("API1", "USER1");
        }

        // deny the 10th request for API1 by USER1
        boolean allowed = rateLimiter.isAllowed("API1", "USER1");
        System.out.println("Request 10 for API1 by USER1: " + allowed);
        rateLimiter.addRequest("API1", "USER1");

        // allow the first 19 requests for API2 by USER1
        for (int i = 0; i < 19; i++) {
            allowed = rateLimiter.isAllowed("API2", "USER1");
            System.out.println("Request " + (i + 1) + " for API2 by USER1: " + allowed);
            rateLimiter.addRequest("API2", "USER1");
        }

        // deny the 20th request for API2 by USER1
        allowed = rateLimiter.isAllowed("API2", "USER1");
        System.out.println("Request 20 for API2 by USER1: " + allowed);
        rateLimiter.addRequest("API2", "USER1");

        // allow the first 2 requests for API3 by USER2
        for (int i = 0; i < 2; i++) {
            allowed = rateLimiter.isAllowed("API3", "USER2");
            System.out.println("Request " + (i + 1) + " for API3 by USER2: " + allowed);
            rateLimiter.addRequest("API3", "USER2");
        }

        // deny the 3rd request for API3 by USER2
        allowed = rateLimiter.isAllowed("API3", "USER2");
        System.out.println("Request 3 for API3 by USER2: " + allowed);
        rateLimiter.addRequest("API3", "USER2");

        // deny the first request for API4 by USER3
        allowed = rateLimiter.isAllowed("API4", "USER3");
        System.out.println("Request 1 for API4 by USER3: " + allowed);
        rateLimiter.addRequest("API4", "USER3");

        // allow the second request for API4 by USER3 after 2 minute
        Thread.sleep(120000);
        allowed = rateLimiter.isAllowed("API4", "USER3");
        System.out.println("Request 2 for API4 by USER3: " + allowed);
        rateLimiter.addRequest("API4", "USER3");

        // allow the first request for API5 by USER3
        allowed = rateLimiter.isAllowed("API5", "USER3");
        System.out.println("Request 1 for API5 by USER3: " + allowed);
        rateLimiter.addRequest("API5", "USER3");

    }
}