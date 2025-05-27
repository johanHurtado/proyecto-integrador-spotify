import java.util.List;

public interface GenderDAO {
    void create(Gender gender);
    void Update(Gender gender);
    void delete(Integer id);
    Gender idConsult(Integer id);
    List<Gender> consultAll();
}
