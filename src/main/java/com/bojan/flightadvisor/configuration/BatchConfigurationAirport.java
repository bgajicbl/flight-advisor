package com.bojan.flightadvisor.configuration;

import com.bojan.flightadvisor.dto.model.AirportDto;
import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.processor.AirportItemProcessor;
import com.bojan.flightadvisor.repository.AirportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class BatchConfigurationAirport {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private AirportRepository airportRepository;

    private static final Logger log = LoggerFactory.getLogger(BatchConfigurationAirport.class);

    @Bean
    @StepScope
    public FlatFileItemReader airportReader(@Value("#{jobParameters[filePath]}") final String filePath) {
        FlatFileItemReader reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(0);

        DefaultLineMapper lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "name", "city", "country", "iata", "icao", "latitude", "longitude", "altitude",
                "timezone", "dst", "tzOlson", "type", "source");

        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(AirportDto.class);

        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(tokenizer);
        reader.setLineMapper(lineMapper);

        return reader;
    }


    @Bean
    public JpaItemWriter airportWriter() {
        JpaItemWriter writer = new JpaItemWriter();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public ItemProcessor<AirportDto, Airport> airportProcessor() {
        return new AirportItemProcessor();
    }

    @Bean
    public Job importAirportJob(@Qualifier("airportListener") JobExecutionListener listener) {
        return jobBuilderFactory.get("importAirportJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(airportStep1())
                .end()
                .build();
    }

    @Bean
    public Step airportStep1() {
        return stepBuilderFactory.get("airportStep1")
                .<AirportDto, Airport>chunk(500)
                .reader(airportReader(null))
                .faultTolerant()
                .skip(Exception.class)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .processor(airportProcessor())
                .writer(airportWriter())
                .faultTolerant()
                .skip(Exception.class)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .build();
    }

    @Bean
    public JobExecutionListener airportListener() {
        return new JobExecutionListener() {


            @Override
            public void beforeJob(JobExecution jobExecution) {
                /**
                 * As of now empty but can add some before job conditions
                 */
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info("!!! JOB FINISHED! Time to verify the results");
                    /*airportRepository.findAll().
                            forEach(airport -> log.info("Found <" + airport + "> in the database."));*/
                }
            }
        };
    }
}