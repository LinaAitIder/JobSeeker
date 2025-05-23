package com.jobapp.service.impl;

import com.jobapp.model.OffreEmploi;
import com.jobapp.service.IARecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.time.Duration;
import java.util.*;
import java.util.stream.*;

@Service
public class IARecommendationServiceImpl implements IARecommendationService {
    private final WebClient webClient;
    private static final Logger log = LoggerFactory.getLogger(IARecommendationServiceImpl.class);

    @Autowired
    public IARecommendationServiceImpl(
            @Value("${ia.recommendation.api.url}") String apiUrl,
            @Value("${ia.recommendation.api.key}") String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    @Override
    public List<Long> getRankedOffers(String cvText, List<String> offersAsText,List<OffreEmploi> activeOffers){

        // Filter out empty offers before processing
        offersAsText = offersAsText.stream()
                .filter(offer -> offer != null && !offer.trim().isEmpty())
                .toList();

        if(offersAsText.isEmpty()) {
            log.warn("No valid job offers found for recommendations");
            return Collections.emptyList();
        }

        Map<String, Object> requestBody = Map.of(
                "inputs", Map.of(
                        "source_sentence", cvText,
                        "sentences", offersAsText
                )
        );
        try {
            List<Float> similarities = webClient.post()
                    .uri("/models/sentence-transformers/all-MiniLM-L6-v2")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Float>>() {})
                    .block(Duration.ofSeconds(120));

            return IntStream.range(0, similarities.size())
                    .boxed()
                    .sorted((i1, i2) -> Float.compare(similarities.get(i2), similarities.get(i1)))
                    .map(i -> activeOffers.get(i).getId())
                    .collect(Collectors.toList());
        } catch (WebClientResponseException e) {
            log.error("HTTP Error {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("API call failed", e);
            return Collections.emptyList();
        }
    }
}