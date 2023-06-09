package dat3.grocerydeliverysystem.service;

import dat3.grocerydeliverysystem.dto.ProductRequest;
import dat3.grocerydeliverysystem.dto.ProductResponse;
import dat3.grocerydeliverysystem.entity.Product;
import dat3.grocerydeliverysystem.repo.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

  ProductRepo productRepo;

  public ProductService(ProductRepo productRepo) {
    this.productRepo = productRepo;
  }
  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepo.findAll();
    return products.stream().map(product -> new ProductResponse(product)).toList();
  }

  public ProductResponse addProduct(ProductRequest body) {

    if(productRepo.existsById(body.getId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Varen findes allerede");
    }

    Product newProduct = ProductRequest.getProductEntity(body);
    productRepo.save(newProduct);
    return new ProductResponse(newProduct);
  }

  public ProductResponse findProductById(Long id) {
    Product product = productRepo.findById(id).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Varen findes ikke"));
    return new ProductResponse(product);
  }

  public ResponseEntity<Boolean> editProduct(ProductRequest body) {

    Product product = productRepo.findById(body.getId()).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Varen findes ikke"));

    Product productUpdated = ProductRequest.getProductEntity(body);

    product.setId(productUpdated.getId());
    product.setName(productUpdated.getName());
    product.setPrice(productUpdated.getPrice());
    product.setWeight(productUpdated.getWeight());

    productRepo.save(product);

    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  public ResponseEntity<Boolean> deleteProduct(Long id) {

    Product product = productRepo.findById(id).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Varen findes ikke"));

    productRepo.delete(product);

    return new ResponseEntity<>(true, HttpStatus.OK);
  }
}
