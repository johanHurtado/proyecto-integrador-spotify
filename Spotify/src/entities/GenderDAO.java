
import java.util.List;

public interface GenderDAO {
    void create(Gender gender);

    void update(Gender gender);
    
    void delete(Gender gender);
    
    public List<GenderDAO>consultAll();
}
