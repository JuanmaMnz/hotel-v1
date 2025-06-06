package com.hotel.Security.RateLimiter;

import com.hotel.Common.Exception.CommonExceptions.RateLimitExceededException;
import com.hotel.Security.RateLimiter.Annotation.WithIpRateLimit;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.hotel.Common.Exception.ErrorCode.RATE_LIMIT_EXCEEDED;

@Aspect
@Component
public class RateLimitAspect {

    public static final String ERROR_MESSAGE =
            "Demasiadas solicitudes en el endpoint. Por favor, inténtelo de nuevo después de %d milisegundos.";

    @Before("@annotation(withIpRateLimit)")
    public void rateLimitByIp(WithIpRateLimit withIpRateLimit) {
        handleRateLimit(getClientIp(), withIpRateLimit.limit(), withIpRateLimit.duration());
    }

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Long>> requestCounts = new ConcurrentHashMap<>();

    private void handleRateLimit(String key, int limit, long duration) {
        long currentTime = System.currentTimeMillis();

        requestCounts.putIfAbsent(key, new CopyOnWriteArrayList<>());
        CopyOnWriteArrayList<Long> timestamps = requestCounts.get(key);

        timestamps.add(currentTime);
        cleanUpRequestCounts(key, currentTime, duration);

        if (timestamps.size() > limit) {
            throw new RateLimitExceededException(
                    RATE_LIMIT_EXCEEDED,
                    String.format(ERROR_MESSAGE, duration)
            );
        }
    }

    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest().getRemoteAddr();
    }

    private void cleanUpRequestCounts(String key, long currentTime, long duration) {
        CopyOnWriteArrayList<Long> timestamps = requestCounts.get(key);
        timestamps.removeIf(timestamp -> currentTime - timestamp > duration);
    }

}
