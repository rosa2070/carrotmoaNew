package carrotmoa.carrotmoa.model.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class CommunityCategoryResponses {
    private List<CommunityCategoryResponse> categories = new ArrayList<>();

    public CommunityCategoryResponses(List<CommunityCategoryResponse> categories) {
        this.categories = categories;
    }
}
