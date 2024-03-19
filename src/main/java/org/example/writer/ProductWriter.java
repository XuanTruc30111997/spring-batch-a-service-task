package org.example.writer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

@Slf4j
public class ProductWriter implements Tasklet, StepExecutionListener {
    private List<Product> productsTransformed;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Start writing");
        this.productsTransformed.forEach(product -> log.info("Writing product {}", product));

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("ProductWriter Before writing");
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.productsTransformed = (List<Product>) executionContext.get("productsTransformed");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("End writing Product");

        return ExitStatus.COMPLETED;
    }
}
