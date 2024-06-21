package com.minimart.product.repository;


import com.minimart.brand.entity.Brand;
import com.minimart.category.entity.ProductCategory;
import com.minimart.product.dto.request.ProductFilterDto;
import com.minimart.product.entity.Product;
import com.minimart.product.entity.ProductStatus;
import com.minimart.user.entity.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;


public class ProductSpecification implements Specification<Product> {
    private ProductFilterDto filter;

    public ProductSpecification(ProductFilterDto filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if(filter != null) {
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("title"), "%" + filter.getName() + "%"));
            }

            if (filter.getBrandId() > 0) {
                Join<Product, Brand> brandJoin = root.join("brand");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(brandJoin.get("id"), filter.getBrandId()));
            }

            if (filter.getCategoryId() > 0) {
                Join<Product, ProductCategory> categoryJoin = root.join("category");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(categoryJoin.get("id"), filter.getCategoryId()));
            }

            if (filter.getMaxPrice() > 0) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            if (filter.getMinPrice() > 0) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            if (filter.getSellerId() > 0) {
                Join<Product, User> sellerJoin = root.join("seller");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(sellerJoin.get("id"), filter.getSellerId()));
            }

            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("status"), ProductStatus.valueOf(filter.getStatus())));
            }
        }

        return predicate;
    }
}
