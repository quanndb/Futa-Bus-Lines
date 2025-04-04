//package com.fasfood.web.support;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Random;
//
//@Slf4j
//@Component
//public class IamServiceLocator {
//
//    @Value(value = "${spring.application.name}")
//    private String appName;
//    @Value(value = "${app.client.iam}")
//    private String iamHost;
//    @Value(value = "${spring.profiles.active:default}")
//    private String profile;
//
//    private final DiscoveryClient discoveryClient;
//    private final Random random = new Random();
//
//    public IamServiceLocator(DiscoveryClient discoveryClient) {
//        this.discoveryClient = discoveryClient;
//    }
//
//    public String getIamServiceUrl() {
//        log.error("Profile : {}", this.profile);
//        if(!Objects.equals("iam", this.appName) && Objects.equals("dev", this.profile)) {
//            List<ServiceInstance> instances = this.discoveryClient.getInstances("iam");
//            if (instances.isEmpty()) {
//                throw new IllegalStateException("No IAM service instance available!");
//            }
//            ServiceInstance instance = instances.get(this.random.nextInt(instances.size()));
//            return instance.getUri().toString();
//        }
//        return iamHost;
//    }
//}
