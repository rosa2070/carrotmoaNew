package carrotmoa.carrotmoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "community_post")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPost extends BaseEntity {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "community_category_id")
    private Long communityCategoryId;

    public void updateCategory(Long communityCategoryId) {
        this.communityCategoryId = communityCategoryId;
    }
}
