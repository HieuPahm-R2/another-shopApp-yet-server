package com.hustVN.otherShopYet.components.converters;

import com.hustVN.otherShopYet.model.entity.Category;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CategoryMessageConvert extends JsonMessageConverter {
    public CategoryMessageConvert() {
        super();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("com.hustVN.otherShopYet");
        typeMapper.setIdClassMapping(Collections.singletonMap("category", Category.class));
        this.setTypeMapper(typeMapper);
    }
}
