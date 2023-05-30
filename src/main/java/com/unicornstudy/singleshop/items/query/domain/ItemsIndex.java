package com.unicornstudy.singleshop.items.query.domain;

import com.unicornstudy.singleshop.items.command.domain.ChildCategory;
import com.unicornstudy.singleshop.items.command.domain.Items;
import com.unicornstudy.singleshop.items.command.domain.ParentCategory;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "items")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ItemsIndex {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Keyword)
    private String description;

    @Field(type = FieldType.Integer)
    private Integer quantity;

    @Field(type = FieldType.Date)
    private String modifiedDate;

    @Field(type = FieldType.Date)
    private String createdDate;

    @Field(type = FieldType.Keyword)
    private ParentCategory parentCategory;

    @Field(type = FieldType.Keyword)
    private ChildCategory childCategory;

    public static ItemsIndex createElasticSearchItems(Items items) {
        return ItemsIndex.builder()
                .id(items.getId())
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .modifiedDate(items.getModifiedDate().toString())
                .createdDate(items.getCreatedDate().toString())
                .parentCategory(items.getParentCategory())
                .childCategory(items.getChildCategory())
                .build();
    }
}
