package com.minimart.productattribute;

import com.minimart.productattribute.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {
    @Query("SELECT pa FROM ProductAttribute pa WHERE pa.id IN :ids")
    List<ProductAttribute> findAllByIds(@Param("ids") List<Integer> ids);

}
