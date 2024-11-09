# part-21-panache-repository-methods-part-4


Pagination

Step-1
@Entity
public class SimCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long number;
    String provider;
    boolean isActive;

    //getter setter and toString
}

Step-2
@ApplicationScoped
public class SimCardRepository implements PanacheRepository<SimCard> {
}

Step-3
@RegisterForReflection
public class SimProjection {
    public Long number;
    public String provider;

    public SimProjection(Long number, String provider) {
        this.number = number;
        this.provider = provider;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}

Step-4


Step-5


