package org.example.reader;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductFileReader implements Tasklet, StepExecutionListener {
    private List<ProductInput> products= new ArrayList<>();

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Before reading");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Start reading");
        this.products.add(ProductInput.builder().name("Testing 1").price(123).build());
        this.products.add(ProductInput.builder().name("Testing 2").price(1111).build());

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("After reading");

        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("products", this.products);
        return ExitStatus.COMPLETED;
    }
}
