package ie.dq.motorbike.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Motorbike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String make;

    private String model;

    private String type;

    private int engine;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEngine() {
        return engine;
    }

    public void setEngine(int engine) {
        this.engine = engine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Motorbike motorbike = (Motorbike) o;
        return engine == motorbike.engine &&
                make.equals(motorbike.make) &&
                model.equals(motorbike.model) &&
                type.equals(motorbike.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(make, model, type, engine);
    }
}
