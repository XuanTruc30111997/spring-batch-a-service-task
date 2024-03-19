package org.example.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductInput implements Serializable {
    String name;
    float price;
}
