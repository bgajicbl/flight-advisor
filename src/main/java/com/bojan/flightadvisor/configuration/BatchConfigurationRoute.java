package com.bojan.flightadvisor.configuration;

import com.bojan.flightadvisor.dto.model.RouteDto;
import com.bojan.flightadvisor.entity.Route;
import com.bojan.flightadvisor.processor.RouteItemProcessor;
import com.bojan.flightadvisor.repository.RouteRepository;
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
public class BatchConfigurationRoute {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private RouteRepository routeRepository;

    private static final Logger log = LoggerFactory.getLogger(BatchConfigurationRoute.class);

    @Bean
    @StepScope
    public FlatFileItemReader routeReader(@Value("#{jobParameters[filePath]}") String filePath) {
        FlatFileItemReader reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(0);

        DefaultLineMapper lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("airline", "airlineId", "sourceAirport", "sourceAirportId", "destinationAirport", "destinationAirportId",
                "codeShare", "stops", "equipment", "price");

        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(RouteDto.class);

        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(tokenizer);
        reader.setLineMapper(lineMapper);

        return reader;
    }


    @Bean
    public JpaItemWriter routeWriter() {
        JpaItemWriter writer = new JpaItemWriter();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public ItemProcessor<RouteDto, Route> routeProcessor() {
        return new RouteItemProcessor();
    }

    @Bean
    public Job importRouteJob(@Qualifier("routeListener") JobExecutionListener listener) {
        return jobBuilderFactory.get("importRouteJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(routeStep1())
                .end()
                .build();
    }

    @Bean
    public Step routeStep1() {
        return stepBuilderFactory.get("routeStep1")
                .<RouteDto, Route>chunk(1000)
                .reader(routeReader(null))
                .faultTolerant()
                .skip(Exception.class)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .processor(routeProcessor())
                .writer(routeWriter())
                .faultTolerant()
                .skip(Exception.class)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .build();
    }

    @Bean
    public JobExecutionListener routeListener() {
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
                    /*routeRepository.findAll().
                            forEach(route -> log.info("Found <" + route + "> in the database."));*/
                }
            }
        };
    }
}