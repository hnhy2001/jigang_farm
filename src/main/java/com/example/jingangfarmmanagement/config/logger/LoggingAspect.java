//package com.example.jingangfarmmanagement.config.logger;
//
//import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
//import com.example.jingangfarmmanagement.service.Impl.LogServiceImpl;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//@Aspect
//@Component
//public class LoggingAspect {
//
//    @Autowired
//    private LogServiceImpl logService;
//
//    @Pointcut("execution(* com.example.jingangfarmmanagement.service.*.*(..))")
//    public void serviceLayer() {}
//
//    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
//    public void logAfterReturning(JoinPoint joinPoint, Object result) {
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//
//        // Retrieve the Loggable annotation and its value
//        ELogType logType = getLogType(joinPoint);
//
//        String logMessage = String.format("Hàm %s đầu vào: %s and kết quả là: %s",
//                methodName, args, result);
//        logService.logAction(logType, logMessage, "success");
//    }
//
//    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//
//        // Retrieve the Loggable annotation and its value
//        ELogType logType = getLogType(joinPoint);
//
//        String logMessage = String.format("Hàm %s đầu vào: %s and kết quả là: %s",
//                methodName, args, exception.getMessage());
//        logService.logAction(logType, logMessage, "fail");
//    }
//
//    private ELogType getLogType(JoinPoint joinPoint) {
//        // Get the method signature
//        String methodName = joinPoint.getSignature().getName();
//        // Check if the method has the Loggable annotation
//        try {
//            Loggable loggable = joinPoint.getTarget().getClass()
//                    .getMethod(methodName, ((MethodSignature) joinPoint.getSignature()).getParameterTypes())
//                    .getAnnotation(Loggable.class);
//            return loggable != null ? loggable.value() : ELogType.DEFAULT;
//        } catch (NoSuchMethodException e) {
//            // Handle the exception
//            return ELogType.DEFAULT;
//        }
//    }
//}
