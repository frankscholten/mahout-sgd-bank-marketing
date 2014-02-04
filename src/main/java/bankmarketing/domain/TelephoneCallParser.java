package bankmarketing.domain;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.io.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Parses semi-colon separated data as TelephoneCall's
 */
public class TelephoneCallParser implements Iterable<TelephoneCall> {
    private final Splitter onSemi = Splitter.on(";").trimResults(CharMatcher.anyOf("\" ;"));
    private String resourceName;

    public TelephoneCallParser(String resourceName) throws IOException {
        this.resourceName = resourceName;
    }

    @Override
    public Iterator<TelephoneCall> iterator() {
        try {
            return new AbstractIterator<TelephoneCall>() {
                BufferedReader input = new BufferedReader(new InputStreamReader(Resources.getResource(resourceName).openStream()));
                Iterable<String> fieldNames = onSemi.split(input.readLine());

                @Override
                protected TelephoneCall computeNext() {
                    try {
                        String line = input.readLine();
                        if (line == null) {
                            return endOfData();
                        }

                        return new TelephoneCall(fieldNames, onSemi.split(line));
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading data", e);
                    }
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Error reading data", e);
        }
    }
}
