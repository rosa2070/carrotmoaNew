package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.model.response.BestAccommodationResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BestAccommodationCustomRepository {
    List<BestAccommodationResponse> getBestAccommodations();
    List<BestAccommodationResponse> getTopBestAccommodations();
}

