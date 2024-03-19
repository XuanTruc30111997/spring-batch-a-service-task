package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@ToString
@Getter
public class Product implements Serializable {
    String id;
    String name;
    Float price;
}
