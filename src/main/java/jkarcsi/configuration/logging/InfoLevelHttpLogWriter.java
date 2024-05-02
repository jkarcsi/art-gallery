package jkarcsi.configuration.logging;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

@Slf4j
public class InfoLevelHttpLogWriter implements HttpLogWriter {

    @Override
    public void write(final @NotNull Precorrelation precorrelation, final @NotNull String request) {
        log.info(request);
    }

    @Override
    public void write(final @NotNull Correlation correlation, final @NotNull String response) {
        log.info(response);
    }
}
