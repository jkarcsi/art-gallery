package jkarcsi.configuration.logging;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Precorrelation;
import org.zalando.logbook.json.JsonHttpLogFormatter;

public class JsonLogbookFormatter implements HttpLogFormatter {

    private final JsonHttpLogFormatter delegate;

    public JsonLogbookFormatter() {
        this.delegate = new JsonHttpLogFormatter(new ObjectMapper());
    }

    public String format(@NotNull Precorrelation precorrelation, @NotNull HttpRequest request) throws IOException {
        Map<String, Object> content = delegate.prepare(precorrelation, request);
        removeFields(content);
        return delegate.format(content);
    }

    public String format(@NotNull Correlation correlation, @NotNull HttpResponse response) throws IOException {
        Map<String, Object> content = delegate.prepare(correlation, response);
        removeFields(content);
        return delegate.format(content);
    }

    private void removeFields(final Map<String, Object> content) {
        content.remove("headers");
        content.remove("origin");
        content.remove("correlation");
        content.remove("protocol");
        content.remove("scheme");
        content.remove("port");
        content.remove("remote");
    }

}
