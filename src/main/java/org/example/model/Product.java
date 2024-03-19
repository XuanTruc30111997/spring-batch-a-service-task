package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@ToString
public class Product implements Serializable {
    String id;
    String name;
    Float price;
}
