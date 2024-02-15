package org.abg.batcher.config;

import lombok.RequiredArgsConstructor;
import org.abg.batcher.dto.BatchFileUser;
import org.abg.batcher.entities.BookstoreUser;
import org.abg.batcher.repositories.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;


    @Bean
    public Job updateUsersJob(Step importUsersStep, Step updateAgeTasklet) {
        return new JobBuilder("updateUsersJob", jobRepository).incrementer(new RunIdIncrementer())
                .start(importUsersStep)
                .next(updateAgeTasklet)
                .build();
    }

    @Bean
    public Step updateAgeTasklet() {
        return new StepBuilder("updateAgeTasklet", jobRepository).tasklet(((contribution, chunkContext) -> {
            List<BookstoreUser> users = userRepository.findAll();
            users.forEach(user -> user.setAge(ChronoUnit.YEARS.between(user.getBirthDate(), LocalDate.now())));
            return RepeatStatus.FINISHED;
        }), transactionManager).build();
    }

    @Bean
    public Step importUsersStep() {
        return new StepBuilder("importUsersStep", jobRepository).<BatchFileUser, BookstoreUser>chunk(3, transactionManager)
                .reader(reader())
                .processor(processor())
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<BatchFileUser> reader() {
        return new FlatFileItemReaderBuilder<BatchFileUser>()
                .name("readerStep")
                .resource(new ClassPathResource("users.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names("id", "name", "email", "birth_date")
                .targetType(BatchFileUser.class)
                .build();
    }

    @Bean
    public ItemProcessor<BatchFileUser, BookstoreUser> processor() {
        return item -> {
            Random random = new Random();
            String generatedPassword = random.ints(48, 122)
                    .limit(16)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            return new BookstoreUser(item.getId(), item.getEmail().split("@")[0], LocalDate.parse(item.getBirthDate()),

                    0
            );
        };
    }


    @Bean
    public ItemWriter<BookstoreUser> writer() {
        return userRepository::saveAll;
    }


}
