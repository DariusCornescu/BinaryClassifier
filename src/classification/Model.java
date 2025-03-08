package classification;
import java.util.List;

public interface Model<F,L > {
    void trainModel(List<Instance<F, L>> instances);
    void validate(List<Instance<F, L>> instances);
    void test(List<Instance<F, L>> instances);
}
