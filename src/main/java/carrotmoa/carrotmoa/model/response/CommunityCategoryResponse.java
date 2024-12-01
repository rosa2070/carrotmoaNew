package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.CommunityCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCategoryResponse {
    private Long id;
    private String name;
    private Long parentId;

    public CommunityCategoryResponse(CommunityCategory categories) {
        this.id = categories.getId();
        this.name = categories.getName();
        this.parentId = categories.getParentId();
    }
}
