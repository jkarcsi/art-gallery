package jkarcsi.configuration.logging;

import static jkarcsi.utils.constants.GeneralConstants.AUTHORIZATION;
import static jkarcsi.utils.constants.GeneralConstants.REPLACEMENT;
import static jkarcsi.utils.constants.GeneralConstants.WEBCLIENT_FAIL;
import static jkarcsi.utils.constants.GeneralConstants.WEBCLIENT_SUCCESS;
import static jkarcsi.utils.helpers.SampleData.SAMPLE_USERS_PASSWORD;
import static org.zalando.logbook.HeaderFilters.replaceHeaders;
import static org.zalando.logbook.QueryFilters.replaceQuery;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.zalando.logbook.Conditions;
import org.zalando.logbook.DefaultSink;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Strategy;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class LoggingConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder().condition(Conditions.exclude(Conditions.requestTo("**/.internal/**")))
                .headerFilter(replaceHeaders(AUTHORIZATION::equalsIgnoreCase, REPLACEMENT))
                .queryFilter(replaceQuery(SAMPLE_USERS_PASSWORD, REPLACEMENT)).strategy(new Strategy() {
                    @Override
                    public HttpResponse process(final HttpRequest request, final HttpResponse response) {
                        try {
                            return response.withBody();
                        } catch (IOException e) {
                            return response.withoutBody();
                        }
                    }
                }).sink(new DefaultSink(new JsonLogbookFormatter(), new InfoLevelHttpLogWriter())).build();
    }

    public static ExchangeFilterFunction logWebclientRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            final URL externalUrl;
            try {
                externalUrl = clientRequest.url().toURL();
                log.info(WEBCLIENT_SUCCESS, externalUrl);
            } catch (MalformedURLException e) {
                log.error(WEBCLIENT_FAIL, e.getMessage());
            }
            return Mono.just(clientRequest);
        });
    }

}
