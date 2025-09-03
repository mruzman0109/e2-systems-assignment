package hr.e2systems.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MetarFetchService {

    Logger L = Logger.getLogger(MetarFetchService.class.getName());
    private final SubscriptionService subscriptionService;
    private final MetarService metarService;

    @Value("${metar.fetch.base-url:https://tgftp.nws.noaa.gov/data/observations/metar/stations/}")
    private String baseUrl;

    @Scheduled(fixedRateString = "${metar.fetch.fixed-rate:600000}")
    public void fetchAndStoreMetars() {
        subscriptionService.list().forEach(sub -> {
            try {
                String icao = sub.getIcaoCode();
                String metarUrl = baseUrl + icao + ".TXT";
                URL url = new URL(metarUrl);
                L.log(Level.INFO, "Fetching METAR for " + icao + " from " + metarUrl);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    String line;
                    StringBuilder metarData = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        metarData.append(line).append("\n");
                    }
                    metarService.saveMetar(icao, metarData.toString().trim());
                }
            } catch (Exception e) {
                // log error but continue
                L.log(Level.SEVERE, "Failed to fetch METAR for " + sub.getIcaoCode() + ": " + e.getMessage());
            }
        });
    }
}