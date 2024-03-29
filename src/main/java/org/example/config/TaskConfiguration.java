package org.example.config;
import org.example.constants.Constants;
import org.example.processor.ProductTransformProcessor;
import org.example.reader.ProductFileReader;
import org.example.writer.ProductWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TaskConfiguration {
    @Autowired
    ProductWriter productWriter;

    @Bean
    public ProductFileReader reader() {
        return new ProductFileReader();
    }

    @Bean
    public Step readProducts(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("readProducts", jobRepository)
                .tasklet(new ProductFileReader(), transactionManager)
                .build();
    }

    @Bean
    public Step writeProducts(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("writeProducts", jobRepository)
                .tasklet(productWriter, transactionManager)
                .build();
    }

    @Bean
    public Step processProducts(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processProducts", jobRepository)
                .tasklet(new ProductTransformProcessor(), transactionManager)
                .build();
    }

    @Bean(name = Constants.JOB_NAME)
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(Constants.JOB_NAME, jobRepository)
                .start(readProducts(jobRepository, transactionManager))
                .next(processProducts(jobRepository, transactionManager))
                .next(writeProducts(jobRepository, transactionManager))
                .build();
    }
}
