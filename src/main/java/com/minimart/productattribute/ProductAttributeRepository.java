package com.minimart.productattribute;

import com.minimart.productattribute.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {

// List<ProductAttribute> findAllByIds(Integer[] ids);
}
