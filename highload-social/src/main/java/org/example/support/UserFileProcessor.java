package org.example.support;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class UserFileProcessor {

    public void process() {
        try (Stream<String> lines = Files.lines(Paths.get("names.csv"), Charset.defaultCharset())) {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("names2.csv"));
            lines.forEach(
                    data -> {
                        String[] all = data.split(",");

                        String[] result = new String[5];
                        result[0] = all[0];
                        result[1] = all[1];
                        result[2] = all[2];
                        result[3] = all[3];
                        result[4] = String.valueOf((int)(Math.random() * 1000));

                        try {
                            writer.write(String.join(",", result));
                            writer.newLine();
                        } catch (IOException e) {
                            log.error("Exception when data was writing to file", e);
                        }
                    }
            );
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        log.info("Completed");
    }
}
