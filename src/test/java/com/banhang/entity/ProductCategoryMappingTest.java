package com.banhang.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.junit.jupiter.api.Test;

class ProductCategoryMappingTest {

  @Test
  void categoryCanBeSharedByMultipleProducts() throws NoSuchFieldException {
    ManyToOne productCategory =
        SanPham.class.getDeclaredField("danhMucSanPham").getAnnotation(ManyToOne.class);
    OneToMany categoryProducts =
        DanhMucSanPham.class.getDeclaredField("danhsachsanpham").getAnnotation(OneToMany.class);

    assertNotNull(productCategory);
    assertNotNull(categoryProducts);
    assertEquals("danhMucSanPham", categoryProducts.mappedBy());
  }
}
