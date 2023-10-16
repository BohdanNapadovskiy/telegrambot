package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.domain.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service

public class CommandDispatcher {
    private final CryptocurrencyService service;
    private final Map<String, Consumer<UserRequest>> dispatcher;
    private final ScheduledExecutorService scheduler;

    @Value("${bot.time}")
    private int time;

    public CommandDispatcher(CryptocurrencyService service) {
        this.service =service;
        this.dispatcher =new HashMap<>();
        this.scheduler = new ScheduledThreadPoolExecutor(1);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandDispatcher.class);

    public void executeCommandByRequest(UserRequest request) {
        this.dispatcher.get(request.getCommand()).accept(request);
    }


    @PostConstruct
    public CommandDispatcher init() {
        this.dispatcher.put("/start", startBot());
        this.dispatcher.put("/restart", restartBot());
        return this;
    }

    private Consumer<UserRequest> restartBot() {
        return request -> {
            scheduler.shutdown();
            this.scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    service.validateChangesForCryptocurrency(request);
                }
            }, 0, time, TimeUnit.MINUTES);
        };
    }

    private Consumer<UserRequest> startBot() {
        return request -> {
            this.scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    service.validateChangesForCryptocurrency(request);

                }
            }, 0, time, TimeUnit.MINUTES);
        };
    }


    public boolean exitCommand(UserRequest request) {
        return dispatcher.containsKey(request.getCommand());
    }



}
