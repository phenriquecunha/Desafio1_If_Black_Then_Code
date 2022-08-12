package IBM.Group2.TudoDeBom.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
@Table(name = "tb_product")
public class ProductModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID product_id;
  @Column(nullable = false, unique = true)
  private String name;
  @Column(nullable = false)
  private Double price;
  @Column(nullable = false)
  private boolean generic;
  @Column(nullable = false)
  private int stock_amount;

  @Column(nullable = false)
  private LocalDateTime created_at;
  private LocalDateTime updated_at;
}
