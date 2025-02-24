package org.modsen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PUBLIC)
@Table(name = Car.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"number"})
})
public class Car {

    public static final String TABLE_NAME = "cars";
    @Column(name = "color", nullable = false)
    String color;
    @Builder.Default
    @ManyToMany(mappedBy = "cars")
    Set<Driver> drivers = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "number", nullable = false, unique = true)
    private String number;
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
