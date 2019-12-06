package tech.blend.service;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.blend.models.InMemoryUserAuthenticationCsv;

import java.io.*;
import java.util.List;

/**
 * This class is used to read the external CSV to get User authentication information.
 * Also it convert the each row into JavaBeans.
 */
@Slf4j
@Service
public class InMemoryUserAuthenticationCsvReader {

    @Value("${inMemory.csv.path}")
    private String inMemoryCsvAbsolutePath;

    /**
     * It will return all user authentication details (username, password, role) from a csv.
     * It use for in memory authentication.
     */
    public List<InMemoryUserAuthenticationCsv> getInMemoryUserAuthenticationDetails(){
        
        BeanListProcessor<InMemoryUserAuthenticationCsv> rowProcessor =
                new BeanListProcessor<>(InMemoryUserAuthenticationCsv.class);

        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setLineSeparator("\n");
        parserSettings.setProcessor(rowProcessor);
        parserSettings.setHeaderExtractionEnabled(true);

        CsvParser parser = new CsvParser(parserSettings);
        parser.parse(read(inMemoryCsvAbsolutePath));

        return rowProcessor.getBeans();
    }

    /**
     * It can reads large and small both type of files.
     * @param absoluteFilePath
     * @return
     */
    private Reader read(String absoluteFilePath) {
        File file = new File(absoluteFilePath);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            log.error("{}", e.getMessage());
        }
        return bufferedReader;
    }
}
