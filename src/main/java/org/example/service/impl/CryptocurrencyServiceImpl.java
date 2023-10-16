package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.CryptoMessage;
import org.example.domain.Cryptocurrency;
import org.example.domain.UserRequest;
import org.example.dto.CryptocurrencyDTO;
import org.example.repository.CryptocurrencyRepository;
import org.example.service.CryptocurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptocurrencyServiceImpl implements CryptocurrencyService {
    private final CryptocurrencyRepository repository;
    private final MessageServiceImpl messageServiceImpl;

    @Value("${bot.percent}")
    private double PERCENT;

    @Transactional
    @Override
    public void validateChangesForCryptocurrency(UserRequest request) {
        List<CryptocurrencyDTO> httpResponse = getResponseFromHTTP();
        Set<String> names = httpResponse.stream().map(CryptocurrencyDTO::getSymbol).collect(Collectors.toSet());
        List<Cryptocurrency> cryptocurrencies = repository.findAllByNameIn(names);
        httpResponse.stream().filter(dto -> priceChanged(cryptocurrencies, dto))
            .findAny()
            .ifPresent(dto-> sendMessageToUser(request, dto));
        updateCryptocurrencyInDb(httpResponse, cryptocurrencies);
    }

    private void updateCryptocurrencyInDb(List<CryptocurrencyDTO> httpResponse, List<Cryptocurrency> cryptocurrencies) {
        List<Cryptocurrency> updateList = httpResponse.stream()
                .map(dto-> findElementByName(dto, cryptocurrencies))
                .collect(Collectors.toList());
        repository.saveAll(updateList);
    }


    private Cryptocurrency findElementByName(CryptocurrencyDTO dto, List<Cryptocurrency> cryptocurrencies) {
        return cryptocurrencies.stream()
                .filter(c-> c.getName().equalsIgnoreCase(dto.getSymbol())).findAny()
                .orElse(new Cryptocurrency(dto));

    }

    private void sendMessageToUser(UserRequest request, CryptocurrencyDTO dto) {
        CryptoMessage message = CryptoMessage.prepareMessage(request,"Price for "+dto.getSymbol()+ " was changed to "+dto.getPrice());
        messageServiceImpl.sendMessage(message);
    }

    private boolean priceChanged(List<Cryptocurrency> cryptocurrencies, CryptocurrencyDTO dto) {
        return cryptocurrencies.stream()
            .anyMatch(cr -> cr.getName().equalsIgnoreCase(dto.getSymbol()) &&
                pricedChangeByPercent(dto.getPrice(), cr.getPrice(), PERCENT));

    }

    private boolean pricedChangeByPercent(Double newPrice, Double oldPrice, double percent) {
        boolean result = false;
        double percentDiff = 0;
        if (newPrice > oldPrice) {
            percentDiff = ((newPrice/oldPrice)-1)*100;
        } else if (oldPrice > newPrice) {
            percentDiff = ((oldPrice/newPrice)-1)*100;
        }
        if (percentDiff >= percent) result = true;
        return result;

    }

    private List<CryptocurrencyDTO> getResponseFromHTTP() {
        List<CryptocurrencyDTO> result = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "https://api.mexc.com/api/v3/ticker/price";
        ResponseEntity<CryptocurrencyDTO[]> response = restTemplate.getForEntity(resourceUrl, CryptocurrencyDTO[].class);
        if (response.hasBody()) {
            result = Arrays.stream(response.getBody()).collect(Collectors.toList());
        }
        return result;
    }

}
