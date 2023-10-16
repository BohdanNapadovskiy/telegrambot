package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.CryptocurrencyDTO;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cryptocurrency")
@Getter
public class Cryptocurrency implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name="price")
    private Double price;


    public Cryptocurrency(CryptocurrencyDTO dto) {
        this.name = dto.getSymbol();
        this.price = dto.getPrice();
    }
}
