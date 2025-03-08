package classification;

import java.util.List;
import java.util.Map;

public class Instance<F,L> {
    private List<F> input;
    private L output;

    public Instance(List<F> input, L output) {
        this.input = input;
        this.output = output;}

    public List<F> getInput() {return input;}
    public void setInput(List<F> input) { this.input = input; }

    public L getOutput() { return output; }
    public void setOutput(L output) { this.output = output; }

    public List<Number> getFeatures() {
        return (List<Number>) input;
    }
}

// F - stands for features
// L - stands for label (output) - patient has or not ( 1 or 0 ) diabetes