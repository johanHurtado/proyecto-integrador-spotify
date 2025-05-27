package infrastructure;
import java.util.List;

public interface GenderDAO {
    void create(GenderDAO gender);
    void update(GenderDAO gender);
    void delete(GenderDAO gender);
    List<GenderDAO>consultAll();
}
