package org.example.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.domain.UserRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CommandDispatcher {
    private final CryptocurrencyService service;
    private final Map<String, Consumer<UserRequest>> dispatcher = new HashMap<>();

//    private static final Logger LOGGER = LoggerFactory.getLogger(CommandDispatcher.class);

    public void executeCommandByRequest(UserRequest request) {
        this.dispatcher.get(request.getCommand()).accept(request);
    }


    @PostConstruct
    public CommandDispatcher init() {
        this.dispatcher.put("/start", startBot());
//        this.dispatcher.put("/restart", restartBot());
        return this;
    }

    private Consumer<UserRequest> startBot() {
        return request -> {
            ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    service.updateCryptocurrencyFromHttpRequest(request);

                }
            }, 0, 5, TimeUnit.MINUTES);
        };
    }


    public boolean exitCommand(UserRequest request) {
        return dispatcher.containsKey(request.getCommand());
    }



}
