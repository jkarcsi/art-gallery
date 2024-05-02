package jkarcsi.configuration;

import static jkarcsi.configuration.logging.LoggingConfig.logWebclientRequest;

import javax.net.ssl.SSLException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.resolver.DefaultAddressResolverGroup;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.ArtworkPage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class Configs {

    @Value("${paths.host}")
    private String host;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule artworkDeserialization = new SimpleModule();
        artworkDeserialization.addDeserializer(Artwork.class, new ArtworkDeserializer());
        mapper.registerModule(artworkDeserialization);

        SimpleModule artworkPageDeserialization = new SimpleModule();
        artworkPageDeserialization.addDeserializer(ArtworkPage.class, new ArtworkPageDeserializer());
        mapper.registerModule(artworkPageDeserialization);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public WebClient webClient(final WebClient.Builder webClientBuilder) throws SSLException {
        final SslContext sslContext =
                SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        final HttpClient client = HttpClient.create().followRedirect(true).secure(t -> t.sslContext(sslContext))
                .resolver(DefaultAddressResolverGroup.INSTANCE);
        return webClientBuilder
                .baseUrl(host)
                .filters(exchangeFilterFunctions -> exchangeFilterFunctions.add(logWebclientRequest()))
                .clientConnector(new ReactorClientHttpConnector(
                        client))
                .build();
    }

}
