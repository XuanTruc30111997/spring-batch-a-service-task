package org.example.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductInput;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class ProductTransformProcessor implements Tasklet, StepExecutionListener {
    private List<ProductInput> products;
    private List<Product> productsTransformed;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("ProductTransformProcessor Before process");
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        this.products = (List<ProductInput>) executionContext.get("products");
        log.info("ProductTransformProcessor initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Start process products with: {} records", this.products.size());
        this.productsTransformed = this.products.stream().map(this::transformProduct).collect(Collectors.toList());

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("productsTransformed", this.productsTransformed);
        log.debug("ProductTransformProcessor end.");
        return ExitStatus.COMPLETED;
    }

    private Product transformProduct(ProductInput productInput) {
        UUID uuid = UUID.randomUUID();
        float price = productInput.getPrice() * 1000;

        return Product.builder()
                .id(uuid.toString())
                .name(productInput.getName())
                .price(price)
                .build();
    }
}
