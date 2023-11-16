package restaurantes.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import restaurantes.models.Restaurant;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

    @Modifying
    @Query ("UPDATE Restaurant p Set p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);
    Optional<Restaurant> findByAdress(String adress);

}
